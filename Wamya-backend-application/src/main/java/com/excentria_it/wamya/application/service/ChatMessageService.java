package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadMessagesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;
import com.excentria_it.wamya.domain.LoadMessagesResult;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ChatMessageService implements SendMessageUseCase, LoadMessagesCommandUseCase {

	private final LoadDiscussionsPort loadDiscussionsPort;
	private final LoadUserAccountPort loadUserAccountPort;
	private final AddMessageToDiscussionPort addMessageToDiscussionPort;
	private final SendMessagePort sendMessagePort;

	private final LoadMessagesPort loadMessagesPort;
	private final DateTimeHelper dateTimeHelper;

	@Override
	public MessageDto sendMessage(SendMessageCommand command, Long discussionId, String username) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

		Boolean isTransporter = userAccountOptional.get().getIsTransporter();

		Optional<LoadDiscussionsOutput> loadDiscussionsOutputOptional = loadDiscussionsPort
				.loadDiscussionById(discussionId);

		if (loadDiscussionsOutputOptional.isEmpty()) {
			throw new DiscussionNotFoundException(
					String.format("Discussion not found by discussionId %d", discussionId));
		}

		LoadDiscussionsOutput loadDiscussionsOutput = loadDiscussionsOutputOptional.get();

		String receiverUsername = null;

		if (isTransporter) {
			receiverUsername = loadDiscussionsOutput.getClient().getEmail();

		} else {
			receiverUsername = loadDiscussionsOutput.getTransporter().getEmail();

		}

		if ((isTransporter && (loadDiscussionsOutput.getTransporter().getId() != userAccountOptional.get().getId()))
				|| (!isTransporter
						&& (loadDiscussionsOutput.getClient().getId() != userAccountOptional.get().getId()))) {
			throw new OperationDeniedException("Discussion does not belong to user.");
		}

		ZoneId senderZoneId = dateTimeHelper.findUserZoneId(username);
		ZoneId receiverZoneId = dateTimeHelper.findUserZoneId(receiverUsername);

		MessageOutput messageOutput = addMessageToDiscussionPort.addMessage(discussionId,
				userAccountOptional.get().getId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead());

		MessageDto toSenderMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageOutput.getRead());

		sendMessagePort.sendMessage(toReceiverMessageDto, loadDiscussionsOutput.getId(), receiverUsername);

		sendMessagePort.sendMessage(toSenderMessageDto, loadDiscussionsOutput.getId(), username);

		return toSenderMessageDto;
	}

	@Override
	public LoadMessagesResult loadMessages(LoadMessagesCommand command) {
		Optional<UserAccount> userAccountOptional = loadUserAccountPort
				.loadUserAccountByUsername(command.getUsername());

		Boolean isTransporter = userAccountOptional.get().getIsTransporter();

		Optional<LoadDiscussionsOutput> loadDiscussionsOutputOptional = loadDiscussionsPort
				.loadDiscussionById(command.getDiscussionId());

		if (loadDiscussionsOutputOptional.isEmpty()) {
			throw new DiscussionNotFoundException(
					String.format("Discussion not found by discussionId %d", command.getDiscussionId()));
		}

		LoadDiscussionsOutput loadDiscussionsOutput = loadDiscussionsOutputOptional.get();

		// check that authenticated user is loading his own discussion messages
		if ((isTransporter && !userAccountOptional.get().getId().equals(loadDiscussionsOutput.getTransporter().getId()))
				|| (!isTransporter
						&& !userAccountOptional.get().getId().equals(loadDiscussionsOutput.getClient().getId()))) {

			throw new OperationDeniedException(String.format("Cannot load messages of another user discussion."));
		}

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());

		LoadMessagesOutputResult messagesOutputResult = loadMessagesPort.loadMessages(command.getDiscussionId(),
				command.getPageNumber(), command.getPageSize(), command.getSortingCriterion());

		return new LoadMessagesResult(messagesOutputResult.getTotalPages(), messagesOutputResult.getTotalElements(),
				messagesOutputResult.getPageNumber(), messagesOutputResult.getPageSize(),
				messagesOutputResult.isHasNext(),
				messagesOutputResult.getContent().stream()
						.map(m -> DiscussionUtils.mapToMessageDto(dateTimeHelper, m, userZoneId))
						.collect(Collectors.toList()));
	}

}
