package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CountMessagesUseCase;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase;
import com.excentria_it.wamya.application.port.out.*;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.PushTemplate;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.context.MessageSource;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService
        implements SendMessageUseCase, LoadMessagesUseCase, UpdateMessageReadStatusUseCase, CountMessagesUseCase {

    private final LoadDiscussionsPort loadDiscussionsPort;
    private final LoadUserAccountPort loadUserAccountPort;
    private final AddMessageToDiscussionPort addMessageToDiscussionPort;
    private final AsynchronousMessagingPort asynchronousMessagingPort;
    private final SynchronousMessageSendingPort synchronousMessageSendingPort;

    private final LoadMessagesPort loadMessagesPort;
    private final UpdateMessagePort updateMessagePort;
    private final DateTimeHelper dateTimeHelper;
    private final SendMessageNotificationPort sendMessageNotificationPort;
    private final ServerUrlProperties serverUrlProperties;

    private final ObjectMapper mapper;

    private final MessageSource messageSource;

    private static final String DISCUSSIONS_URL_TEMPLATE = "${protocol}://${host}:${port}/discussions";

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

        // Save the message to the database
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

        // send the message through websocket if receiver is connected
        if (receiverAccountOptional.get().getIsWebSocketConnected() != null
                && receiverAccountOptional.get().getIsWebSocketConnected()) {
            synchronousMessageSendingPort.sendMessage(toReceiverMessageDtoBuilder.sent(true).build(), discussionId,
                    receiverUsername);
        } else if (receiverAccountOptional.get().getDeviceRegistrationToken() != null) {

            // if receiver was connected at least once through smart phone, send him a push
            // notification

            String receiverLocale = receiverAccountOptional.get().getPreferences().get(UserPreferenceKey.LOCALE);
            try {
                String messageNotification = mapper.writeValueAsString(
                        new MessageNotification(toReceiverMessageDtoBuilder.sent(true).build(), discussionId));

                PushMessage pushMessage = PushMessage.builder()
                        .to(receiverAccountOptional.get().getDeviceRegistrationToken())
                        .template(PushTemplate.MESSAGE_RECEIVED)
                        .params(Map.of(PushTemplate.MESSAGE_RECEIVED.getTemplateParams().get(0),
                                senderAccountOptional.get().getFirstname() + " "
                                        + senderAccountOptional.get().getLastname()))
                        .data(Map.of("type", "message", "content", messageNotification)).language(receiverLocale)
                        .build();

                asynchronousMessagingPort.sendPushMessage(pushMessage);

            } catch (JsonProcessingException e) {
                log.error("Exception converting object to json", e);
                return toSenderMessageDtoBuilder.sent(false).build();
            }
        } else {
            String receiverLocale = receiverAccountOptional.get().getPreferences().get(UserPreferenceKey.LOCALE);
            String[] receiverLanguageAndCountry = receiverLocale.split("_");

            String discussionsLink = patchURL(DISCUSSIONS_URL_TEMPLATE, serverUrlProperties.getProtocol(),
                    serverUrlProperties.getHost(), serverUrlProperties.getPort());

            Map<String, String> emailTemplateParams = Map.of(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(0),
                    senderAccountOptional.get().getFirstname() + " "
                            + senderAccountOptional.get().getLastname().toUpperCase(),
                    EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(1), discussionsLink);

            EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.FRETTO_TEAM).to(receiverUsername)
                    .subject(messageSource.getMessage(EmailSubject.RECEIVED_MESSAGE, null,
                            new Locale(receiverLanguageAndCountry[0], receiverLanguageAndCountry[1])))
                    .template(EmailTemplate.RECEIVED_MESSAGE).params(emailTemplateParams).language(receiverLocale)
                    .build();

            asynchronousMessagingPort.sendEmailMessage(emailMessage);
        }

        return toSenderMessageDtoBuilder.sent(true).build();

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

    @Override
    public void updateMessageReadStatus(Long discussionId, Long messageId, String username,
                                        UpdateMessageReadStatusCommand command) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

        Boolean isTransporter = userAccountOptional.get().getIsTransporter();

        Optional<LoadDiscussionsOutput> loadDiscussionsOutputOptional = loadDiscussionsPort
                .loadDiscussionById(discussionId);

        if (loadDiscussionsOutputOptional.isEmpty()) {
            throw new DiscussionNotFoundException(
                    String.format("Discussion not found by discussionId %d", discussionId));
        }

        LoadDiscussionsOutput loadDiscussionsOutput = loadDiscussionsOutputOptional.get();

        // check that authenticated user is loading his own discussion messages
        if ((isTransporter
                && !userAccountOptional.get().getOauthId().equals(loadDiscussionsOutput.getTransporter().getId()))
                || (!isTransporter
                && !userAccountOptional.get().getOauthId().equals(loadDiscussionsOutput.getClient().getId()))) {

            throw new OperationDeniedException(String.format("Cannot update messages of another user discussion."));
        }

        updateMessagePort.updateRead(List.of(messageId), Boolean.valueOf(command.getIsRead()));

    }

    protected String patchURL(String url, String protocol, String host, String port) {

        Map<String, String> data = new HashMap<>();
        data.put("protocol", protocol);
        data.put("host", host);
        data.put("port", port);

        String discussionsUrl = StrSubstitutor.replace(url, data);

        return discussionsUrl;
    }

    @Override
    public Long countMessages(CountMessagesCommand command) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort
                .loadUserAccountByUsername(command.getUsername());

        Boolean isTransporter = userAccountOptional.get().getIsTransporter();

        return loadMessagesPort.countMessages(command.getUsername(), Boolean.valueOf(command.getRead()), isTransporter);
    }
}
