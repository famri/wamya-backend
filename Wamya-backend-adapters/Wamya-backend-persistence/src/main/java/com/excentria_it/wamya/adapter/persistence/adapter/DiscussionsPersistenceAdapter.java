package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.port.out.CreateDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PersistenceAdapter
public class DiscussionsPersistenceAdapter implements LoadDiscussionsPort, CreateDiscussionPort {

    private final DiscussionRepository discussionsRepository;

    private final DiscussionMapper discussionMapper;

    private final ClientRepository clientRepository;

    private final TransporterRepository transporterRepository;

    @Override
    public LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
                                                       Integer pageSize, SortCriterion sortingCriterion) {

        Sort sort = convertToSort(sortingCriterion);

        Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

        Page<DiscussionJpaEntity> discussionsPage = null;
        if (isTransporter) {
            discussionsPage = discussionsRepository.findByTransporter_Id(userAccountId, pagingSort);
        } else {
            discussionsPage = discussionsRepository.findByClient_Id(userAccountId, pagingSort);
        }

        return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
                discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
                discussionsPage.getContent().stream().map(d -> discussionMapper.mapToLoadDiscussionsOutput(d))
                        .collect(Collectors.toList()));

    }

    @Override
    public LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
                                                       Integer pageSize, FilterCriterion filter, SortCriterion sortingCriterion) {

        Sort sort = convertToSort(sortingCriterion);

        Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

        Page<DiscussionJpaEntity> discussionsPage = null;
        DiscussionFilterField filterField = convertToFilterField(filter);

        if (isTransporter) {
            if (filterField != null) {
                switch (filterField) {

                    case ACTIVE:
                        Boolean filterValue = Boolean.valueOf(filter.getValue());
                        discussionsPage = discussionsRepository.findByTransporter_IdAndActive(userAccountId, filterValue,
                                pagingSort);
                        break;
                    default:
                        discussionsPage = null;
                        break;
                }
            } else {
                discussionsPage = null;
            }

        } else {
            if (filterField != null) {
                switch (filterField) {
                    case ACTIVE:
                        Boolean filterValue = Boolean.valueOf(filter.getValue());
                        discussionsPage = discussionsRepository.findByClient_IdAndActive(userAccountId, filterValue,
                                pagingSort);
                        break;
                    default:
                        discussionsPage = null;
                        break;
                }
            } else {
                discussionsPage = null;
            }

        }
        if (discussionsPage == null) {
            return new LoadDiscussionsOutputResult(0, 0, pageNumber, pageSize, false, Collections.emptyList());
        }

        return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
                discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
                discussionsPage.getContent().stream().map(d -> discussionMapper.mapToLoadDiscussionsOutput(d))
                        .collect(Collectors.toList()));

    }

    protected Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
    }

    protected DiscussionFilterField convertToFilterField(FilterCriterion filterCriterion) {
        String fieldString = ParameterUtils.kebabToSnakeCase(filterCriterion.getField());
        for (DiscussionFilterField filterField : DiscussionFilterField.values()) {
            if (filterField.name().equals(fieldString.toUpperCase())) {
                return filterField;
            }

        }
        return null;

    }


    @Override
    public Optional<LoadDiscussionsOutput> loadDiscussionById(Long discussionId) {
        Optional<DiscussionJpaEntity> discussionOptional = discussionsRepository.findById(discussionId);
        if (discussionOptional.isEmpty())
            return Optional.empty();
        return Optional.of(discussionMapper.mapToLoadDiscussionsOutput(discussionOptional.get()));
    }

    public enum DiscussionFilterField {
        ACTIVE;
    }

    @Override
    public Optional<LoadDiscussionsOutput> loadDiscussionByClientIdAndTransporterId(String clientOauthId,
                                                                                    String transporterOauthId) {
        Optional<DiscussionJpaEntity> discussionOptional = discussionsRepository
                .findByClient_OauthIdAndTransporter_OauthId(clientOauthId, transporterOauthId);

        if (discussionOptional.isEmpty())
            return Optional.empty();

        return Optional.of(discussionMapper.mapToLoadDiscussionsOutput(discussionOptional.get()));
    }

    @Override
    public LoadDiscussionsOutput createDiscussion(String clientOauthId, String transporterOauthId) {

        Optional<ClientJpaEntity> clientAccount = clientRepository.findByOauthId(clientOauthId);
        if (clientAccount.isEmpty()) {
            return null;
        }
        Optional<TransporterJpaEntity> transporterAccount = transporterRepository.findByOauthId(transporterOauthId);
        if (transporterAccount.isEmpty()) {
            return null;
        }
        DiscussionJpaEntity discussionJpaEntity = new DiscussionJpaEntity(clientAccount.get(), transporterAccount.get(),
                true, Instant.now(), null);

        return discussionMapper.mapToLoadDiscussionsOutput(discussionsRepository.save(discussionJpaEntity));
    }


}
