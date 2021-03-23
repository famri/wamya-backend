package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionsRepository;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.FilterField;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class DiscussionsPersistenceAdapter implements LoadDiscussionsPort {

	private final DiscussionsRepository discussionsRepository;
	private final DiscussionMapper discussionMapper;

	@Override
	public LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
			Integer pageSize, SortCriterion sortingCriterion) {

		Sort sort = convertToSort(sortingCriterion);

		Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

		Page<DiscussionJpaEntity> discussionsPage = null;

		if (isTransporter) {

			discussionsPage = discussionsRepository.findByTransporter_IdWithMessages(userAccountId, pagingSort);
			return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
					discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
					discussionsPage.getContent().stream()
							.map(d -> discussionMapper.mapToTransporterLoadDiscussionsOutput(d))
							.collect(Collectors.toList()));

		} else {

			discussionsPage = discussionsRepository.findByClient_IdWithMessages(userAccountId, pagingSort);

			return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
					discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
					discussionsPage.getContent().stream().map(d -> discussionMapper.mapToClientLoadDiscussionsOutput(d))
							.collect(Collectors.toList()));

		}

	}

	@Override
	public LoadDiscussionsOutputResult loadDiscussions(Long userAccountId, Boolean isTransporter, Integer pageNumber,
			Integer pageSize, FilterCriterion filter, SortCriterion sortingCriterion) {

		Sort sort = convertToSort(sortingCriterion);

		Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);

		Page<DiscussionJpaEntity> discussionsPage = null;
		FilterField filterField = convertToFilterField(filter);

		if (isTransporter) {
			if (filterField != null) {
				switch (filterField) {

				case ACTIVE:
					Boolean filterValue = Boolean.valueOf(filter.getValue());
					discussionsPage = discussionsRepository.findByTransporter_IdAndActiveWithMessages(userAccountId,
							filterValue, pagingSort);
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
					discussionsPage = discussionsRepository.findByClient_IdAndActiveWithMessages(userAccountId,
							filterValue, pagingSort);
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

		if (isTransporter) {
			return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
					discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
					discussionsPage.getContent().stream()
							.map(d -> discussionMapper.mapToTransporterLoadDiscussionsOutput(d))
							.collect(Collectors.toList()));
		}

		return new LoadDiscussionsOutputResult(discussionsPage.getTotalPages(), discussionsPage.getTotalElements(),
				discussionsPage.getNumber(), discussionsPage.getSize(), discussionsPage.hasNext(),
				discussionsPage.getContent().stream().map(d -> discussionMapper.mapToClientLoadDiscussionsOutput(d))
						.collect(Collectors.toList()));
	}

	protected Sort convertToSort(SortCriterion sortingCriterion) {

		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}

	protected FilterField convertToFilterField(FilterCriterion filterCriterion) {
		String fieldString = ParameterUtils.kebabToSnakeCase(filterCriterion.getField());
		for (FilterField filterField : FilterField.values()) {
			if (filterField.name().equals(fieldString.toUpperCase())) {
				return filterField;
			}

		}
		return null;

	}

}
