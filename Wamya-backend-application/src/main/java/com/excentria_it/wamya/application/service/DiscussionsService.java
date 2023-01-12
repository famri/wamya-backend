package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase;
import com.excentria_it.wamya.application.port.out.CreateDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.*;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

@UseCase
@Transactional
@RequiredArgsConstructor
public class DiscussionsService implements LoadDiscussionsUseCase, FindDiscussionUseCase, CreateDiscussionUseCase {

    private final LoadDiscussionsPort loadDiscussionsPort;

    private final CreateDiscussionPort createDiscussionPort;

    private final LoadUserAccountPort loadUserAccountPort;

    private final DateTimeHelper dateTimeHelper;

    @Override
    public LoadDiscussionsResult loadDiscussions(LoadDiscussionsCommand command) {
        Optional<UserAccount> userAccountOptional = loadUserAccountPort
                .loadUserAccountByUsername(command.getUsername());
        if (userAccountOptional.isEmpty()) {
            throw new UserAccountNotFoundException(
                    String.format("User account not found by username: %s", command.getUsername()));
        }
        UserAccount userAccount = userAccountOptional.get();

        ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());
        LoadDiscussionsOutputResult discussionsOutputResult;
        if (command.getFilteringCriterion() == null) {
            discussionsOutputResult = loadDiscussionsPort.loadDiscussions(userAccount.getId(),
                    userAccount.getIsTransporter(), command.getPageNumber(), command.getPageSize(),
                    command.getSortingCriterion());
        } else {
            discussionsOutputResult = loadDiscussionsPort.loadDiscussions(userAccount.getId(),
                    userAccount.getIsTransporter(), command.getPageNumber(), command.getPageSize(),
                    command.getFilteringCriterion(), command.getSortingCriterion());
        }

        return new LoadDiscussionsResult(discussionsOutputResult.getTotalPages(),
                discussionsOutputResult.getTotalElements(), discussionsOutputResult.getPageNumber(),
                discussionsOutputResult.getPageSize(), discussionsOutputResult.isHasNext(),
                discussionsOutputResult.getContent().stream()
                        .map(d -> DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, d, userZoneId)

                        ).collect(Collectors.toList()));

    }

    @Override
    public LoadDiscussionsDto findDiscussionByClientIdAndTransporterId(
            FindDiscussionByClientIdAndTransporterIdCommand command) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort
                .loadUserAccountByUsername(command.getUsername());

        if (!userAccountOptional.get().getOauthId().equals(command.getClientId())
                && !userAccountOptional.get().getOauthId().equals(command.getTransporterId())) {
            throw new DiscussionNotFoundException(
                    String.format("Discussion not found by clientOauthId %s and transporterOauthId %s",
                            command.getClientId(), command.getTransporterId()));
        }

        Optional<LoadDiscussionsOutput> result = loadDiscussionsPort
                .loadDiscussionByClientIdAndTransporterId(command.getClientId(), command.getTransporterId());
        if (result.isEmpty()) {
            throw new DiscussionNotFoundException(
                    String.format("Discussion not found by clientOauthId %s and transporterOauthId %s",
                            command.getClientId(), command.getTransporterId()));
        }
        ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());
        return DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, result.get(), userZoneId);

    }

    @Override
    public LoadDiscussionsDto findDiscussionById(FindDiscussionByIdCommand command) {

        Optional<LoadDiscussionsOutput> result = loadDiscussionsPort.loadDiscussionById(command.getDiscussionId());

        if (result.isEmpty()) {
            throw new DiscussionNotFoundException(
                    String.format("Discussion not found by discussionId %d", command.getDiscussionId()));
        }

        if (!result.get().getClient().getEmail().equals(command.getUsername())
                && !result.get().getTransporter().getEmail().equals(command.getUsername())) {
            throw new DiscussionNotFoundException(
                    String.format("Discussion not found by discussionId %d", command.getDiscussionId()));
        }

        ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());
        return DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, result.get(), userZoneId);

    }

    @Override
    public LoadDiscussionsDto createDiscussion(CreateDiscussionCommand command, String username) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

        boolean isTransporter = userAccountOptional.get().getIsTransporter();

        if ((isTransporter && !userAccountOptional.get().getOauthId().equals(command.getTransporterId()))
                || (!isTransporter && !userAccountOptional.get().getOauthId().equals(command.getClientId()))) {

            throw new OperationDeniedException("User cannot create a discussion for another user.");
        }

        if ((isTransporter && !loadUserAccountPort.existsByOauthId(command.getClientId()))
                || (!isTransporter && !loadUserAccountPort.existsByOauthId(command.getTransporterId()))) {

            throw new OperationDeniedException(String.format("Cannot create a discussion with non-existent user."));

        }

        LoadDiscussionsOutput loadDiscussionOutput = createDiscussionPort.createDiscussion(command.getClientId(),
                command.getTransporterId());

        ZoneId userZoneId = dateTimeHelper.findUserZoneId(username);

        return DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, loadDiscussionOutput, userZoneId);

    }

}
