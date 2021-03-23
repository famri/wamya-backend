package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class DiscussionsService implements LoadDiscussionsUseCase {

	private final LoadDiscussionsPort loadDiscussionsPort;

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
				discussionsOutputResult.getContent().stream().map(d -> this.mapToLoadDiscussionsDto(d, userZoneId)

				).collect(Collectors.toList()));

	}

	private LoadDiscussionsDto mapToLoadDiscussionsDto(LoadDiscussionsOutput ldo, ZoneId userZoneId) {
		return LoadDiscussionsDto.builder().id(ldo.getId()).active(ldo.getActive())
				.dateTime(dateTimeHelper.systemToUserLocalDateTime(ldo.getDateTime(), userZoneId))
				.messages(ldo.getMessages().stream()
						.map(m -> MessageDto.builder().id(m.getId()).authorEmail(m.getAuthorEmail())
								.authorMobileNumber(m.getAuthorMobileNumber()).content(m.getContent())
								.dateTime(dateTimeHelper.systemToUserLocalDateTime(m.getDateTime(), userZoneId))
								.read(m.getRead()).build())
						.collect(Collectors.toList()))
				.interlocutor(ldo.getInterlocutor()).build();
	}

}
