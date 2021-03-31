package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ChatMessageService implements SendMessageUseCase {

	private final LoadDiscussionsPort loadDiscussionsPort;
	private final LoadUserAccountPort loadUserAccountPort;
	private final AddMessageToDiscussionPort addMessageToDiscussionPort;
	private final SendMessagePort sendMessagePort;
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

		Long receiverUserId = null;

		if (isTransporter) {
			receiverUserId = loadDiscussionsOutput.getClient().getId();

		} else {
			receiverUserId = loadDiscussionsOutput.getTransporter().getId();

		}

		if ((isTransporter && (loadDiscussionsOutput.getTransporter().getId() != userAccountOptional.get().getId()))
				|| (!isTransporter
						&& (loadDiscussionsOutput.getClient().getId() != userAccountOptional.get().getId()))) {
			throw new OperationDeniedException("Discussion does not belong to user.");
		}

		ZoneId senderZoneId = dateTimeHelper.findUserZoneId(username);
		ZoneId receiverZoneId = dateTimeHelper.findUserZoneId(receiverUserId);

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

		sendMessagePort.sendMessage(toReceiverMessageDto, loadDiscussionsOutput.getId(), receiverUserId);

		sendMessagePort.sendMessage(toSenderMessageDto, loadDiscussionsOutput.getId(),
				userAccountOptional.get().getId());

		return toSenderMessageDto;
	}

}
