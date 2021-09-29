package com.excentria_it.wamya.application.service;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadMessagesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.SendMessageNotificationPort;
import com.excentria_it.wamya.application.port.out.UpdateMessagePort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.PushTemplate;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;
import com.excentria_it.wamya.domain.LoadMessagesResult;
import com.excentria_it.wamya.domain.MessageNotification;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserPreferenceKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService implements SendMessageUseCase, LoadMessagesCommandUseCase {

	private final LoadDiscussionsPort loadDiscussionsPort;
	private final LoadUserAccountPort loadUserAccountPort;
	private final AddMessageToDiscussionPort addMessageToDiscussionPort;
	private final MessagingPort messagingPort;

	private final LoadMessagesPort loadMessagesPort;
	private final UpdateMessagePort updateMessagePort;
	private final DateTimeHelper dateTimeHelper;
	private final SendMessageNotificationPort sendMessageNotificationPort;

	private final ObjectMapper mapper;

	@Override
	public MessageDto sendMessage(SendMessageCommand command, Long discussionId, String senderUsername) {

		Optional<UserAccount> senderAccountOptional = loadUserAccountPort.loadUserAccountByUsername(senderUsername);

		Boolean isTransporter = senderAccountOptional.get().getIsTransporter();

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

		if ((isTransporter
				&& !loadDiscussionsOutput.getTransporter().getId().equals(senderAccountOptional.get().getOauthId()))
				|| (!isTransporter && !loadDiscussionsOutput.getClient().getId()
						.equals(senderAccountOptional.get().getOauthId()))) {
			throw new OperationDeniedException("Discussion does not belong to user.");
		}

		Optional<UserAccount> receiverAccountOptional = loadUserAccountPort.loadUserAccountByUsername(receiverUsername);

		ZoneId senderZoneId = ZoneId.of(senderAccountOptional.get().getPreferences().get(UserPreferenceKey.TIMEZONE));
		ZoneId receiverZoneId = ZoneId
				.of(receiverAccountOptional.get().getPreferences().get(UserPreferenceKey.TIMEZONE));

		MessageOutput messageOutput = addMessageToDiscussionPort.addMessage(discussionId,
				senderAccountOptional.get().getOauthId(), command.getContent());

		MessageDto.MessageDtoBuilder toReceiverMessageDtoBuilder = MessageDto.builder().id(messageOutput.getId())
				.authorId(messageOutput.getAuthorId()).content(messageOutput.getContent())
				.dateTime(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId))
				.read(false);

		MessageDto.MessageDtoBuilder toSenderMessageDtoBuilder = MessageDto.builder().id(messageOutput.getId())
				.authorId(messageOutput.getAuthorId()).content(messageOutput.getContent())
				.dateTime(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId))
				.read(messageOutput.getRead());

		try {
			String messageNotification = mapper.writeValueAsString(
					new MessageNotification(toReceiverMessageDtoBuilder.sent(true).build(), discussionId));

			PushMessage pushMessage = PushMessage.builder()
					.to(receiverAccountOptional.get().getDeviceRegistrationToken())
					.template(PushTemplate.MESSAGE_RECEIVED)
					.params(Map.of(PushTemplate.MESSAGE_RECEIVED.getTemplateParams().get(0),
							senderAccountOptional.get().getFirstname() + " "
									+ senderAccountOptional.get().getLastname()))
					.data(Map.of("type", "message", "content", messageNotification))
					.language(receiverAccountOptional.get().getPreferences().get(UserPreferenceKey.LOCALE)).build();

			messagingPort.sendPushMessage(pushMessage);

			return toSenderMessageDtoBuilder.sent(true).build();
		} catch (JsonProcessingException e) {
			log.error("Exception converting object to json", e);
			return toSenderMessageDtoBuilder.sent(false).build();
		}

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
		if ((isTransporter
				&& !userAccountOptional.get().getOauthId().equals(loadDiscussionsOutput.getTransporter().getId()))
				|| (!isTransporter
						&& !userAccountOptional.get().getOauthId().equals(loadDiscussionsOutput.getClient().getId()))) {

			throw new OperationDeniedException(String.format("Cannot load messages of another user discussion."));
		}

		ZoneId userZoneId = dateTimeHelper.findUserZoneId(command.getUsername());

		LoadMessagesOutputResult messagesOutputResult = loadMessagesPort.loadMessages(command.getDiscussionId(),
				command.getPageNumber(), command.getPageSize(), command.getSortingCriterion());

		List<Long> messagesIds = messagesOutputResult.getContent().stream()
				.filter(m -> !m.getRead() && !m.getAuthorId().equals(userAccountOptional.get().getOauthId()))
				.map(m -> m.getId()).collect(Collectors.toList());
		if (!messagesIds.isEmpty()) {

			updateMessagePort.updateRead(messagesIds, true);

			if (isTransporter) {
				sendMessageNotificationPort.sendReadNotification(loadDiscussionsOutput.getClient().getEmail(),
						command.getDiscussionId(), messagesIds);
			} else {
				sendMessageNotificationPort.sendReadNotification(loadDiscussionsOutput.getTransporter().getEmail(),
						command.getDiscussionId(), messagesIds);
			}
		}

		return new LoadMessagesResult(messagesOutputResult.getTotalPages(), messagesOutputResult.getTotalElements(),
				messagesOutputResult.getPageNumber(), messagesOutputResult.getPageSize(),
				messagesOutputResult.isHasNext(),
				messagesOutputResult.getContent().stream()
						.map(m -> DiscussionUtils.mapToMessageDto(dateTimeHelper, m, userZoneId))
						.collect(Collectors.toList()));
	}

}
