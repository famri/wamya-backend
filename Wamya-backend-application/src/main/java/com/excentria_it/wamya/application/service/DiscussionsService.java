package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

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
	public LoadDiscussionsDto findDiscussion(FindDiscussionCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort
				.loadUserAccountByUsername(command.getUsername());

		if (userAccountOptional.get().getId() != command.getClientId()
				&& userAccountOptional.get().getId() != command.getTransporterId()) {
			throw new DiscussionNotFoundException(
					String.format("Discussion not found by clientId %d and transporterId %d", command.getClientId(),
							command.getTransporterId()));
		}
		boolean isTransporter = userAccountOptional.get().getIsTransporter();

		Optional<LoadDiscussionsOutput> result = loadDiscussionsPort.loadDiscusssion(command.getClientId(),
				command.getTransporterId(), isTransporter);
		if (result.isEmpty()) {
			throw new DiscussionNotFoundException(
					String.format("Discussion not found by clientId %d and transporterId %d", command.getClientId(),
							command.getTransporterId()));
		}
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());
		return DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, result.get(), userZoneId);

	}

	@Override
	public LoadDiscussionsDto createDiscussion(CreateDiscussionCommand command, String username) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

		boolean isTransporter = userAccountOptional.get().getIsTransporter();

		if ((isTransporter && (userAccountOptional.get().getId() != command.getTransporterId()))
				|| (!isTransporter && (userAccountOptional.get().getId() != command.getClientId()))) {

			throw new OperationDeniedException("User cannot create a discussion for another user.");
		}

		if ((isTransporter && !loadUserAccountPort.existsById(command.getClientId()))
				|| (!isTransporter && !loadUserAccountPort.existsById(command.getTransporterId()))) {

			throw new OperationDeniedException(String.format("Cannot create a discussion with inexistent user."));

		}

		LoadDiscussionsOutput loadDiscussionOutput = createDiscussionPort.createDiscussion(command.getClientId(),
				command.getTransporterId(), isTransporter);

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(username);

		return DiscussionUtils.mapToLoadDiscussionsDto(dateTimeHelper, loadDiscussionOutput, userZoneId);

	}

}
