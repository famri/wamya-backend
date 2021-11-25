package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static com.excentria_it.wamya.test.data.common.MessageTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.excentria_it.wamya.application.port.in.CountMessagesUseCase.CountMessagesCommand;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase.LoadMessagesCommand.LoadMessagesCommandBuilder;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase.UpdateMessageReadStatusCommand;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadMessagesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SendMessageNotificationPort;
import com.excentria_it.wamya.application.port.out.SynchronousMessageSendingPort;
import com.excentria_it.wamya.application.port.out.UpdateMessagePort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
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
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTests {
	@Mock
	private LoadDiscussionsPort loadDiscussionsPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Mock
	private AddMessageToDiscussionPort addMessageToDiscussionPort;
	@Mock
	private AsynchronousMessagingPort messagingPort;
	@Mock
	private LoadMessagesPort loadMessagesPort;

	@Mock
	private UpdateMessagePort updateMessagePort;
	@Mock
	private SendMessageNotificationPort sendMessageNotificationPort;
	@Mock
	private SynchronousMessageSendingPort sendMessagePort;
	@Mock
	private ServerUrlProperties serverUrlProperties;
	@Mock
	private MessageSource messageSource;

	@Spy
	private DateTimeHelper dateTimeHelper;

	@Spy
	private ObjectMapper mapper;

	@Spy
	@InjectMocks
	private ChatMessageService chatMessageService;

	@Test
	void givenTransporterDeviceTokenExistsAndTransporterIsWebSocketConnected_WhenSendMessageToTransporter_ThenSendWebSocketMessageAndDontSendPushNotificationMessageAndReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder()
				.deviceRegistrationToken("SOME_DEVICE_REGISTRATION_TOKEN").isWebSocketConnected(true).build();

		given(loadUserAccountPort.loadUserAccountByUsername(clientUserAccount.getEmail()))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(loadDiscussionsOutput.getTransporter().getEmail()))
				.willReturn(Optional.of(transporterUserAccount));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));
		ZoneId receiverZoneId = ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead(), true);

		then(messagingPort).should(never()).sendPushMessage(any(PushMessage.class));
		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(transporterUserAccount.getEmail()));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void givenTransporterDeviceTokenExistsAndTransporterWebSocketIsNotConnected_WhenSendMessageToTransporter_ThenSendPushNotificationMessageAndDontSendWebSocketMessageAndReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder()
				.deviceRegistrationToken("SOME_DEVICE_REGISTRATION_TOKEN").isWebSocketConnected(false).build();

		given(loadUserAccountPort.loadUserAccountByUsername(clientUserAccount.getEmail()))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(loadDiscussionsOutput.getTransporter().getEmail()))
				.willReturn(Optional.of(transporterUserAccount));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));
		ZoneId receiverZoneId = ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead(), true);

		String messageNotification = mapper
				.writeValueAsString(new MessageNotification(toReceiverMessageDto, loadDiscussionsOutput.getId()));

		PushMessage pushMessage = PushMessage.builder().to(transporterUserAccount.getDeviceRegistrationToken())
				.template(PushTemplate.MESSAGE_RECEIVED)
				.params(Map.of(PushTemplate.MESSAGE_RECEIVED.getTemplateParams().get(0),
						clientUserAccount.getFirstname() + " " + clientUserAccount.getLastname()))
				.data(Map.of("type", "message", "content", messageNotification)).language("fr_FR").build();

		then(messagingPort).should(times(1)).sendPushMessage(eq(pushMessage));
		then(sendMessagePort).should(never()).sendMessage(any(MessageDto.class), any(Long.class), any(String.class));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void givenTransporterDeviceTokenExistsAndTransporterWebSockedIsNotConnectedAndJsonProcessingException_WhenSendMessageToTransporter_ThenReturnNotSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder()
				.deviceRegistrationToken("SOME_DEVICE_TOKEN").isWebSocketConnected(false).build();

		given(loadUserAccountPort.loadUserAccountByUsername(clientUserAccount.getEmail()))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(loadDiscussionsOutput.getTransporter().getEmail()))
				.willReturn(Optional.of(transporterUserAccount));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));
		// ZoneId receiverZoneId =
		// ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		doThrow(JsonProcessingException.class).when(mapper).writeValueAsString(any(Object.class));

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getOauthId(), command.getContent());

		then(messagingPort).should(never()).sendPushMessage(any(PushMessage.class));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(false, messageDto.getSent());
	}

	@Test
	void givenTransporterDeviceTokenNonExistentAndTransporterIsNotWebSocketConnected_WhenSendMessageToTransporter_ThenDontSendWebSocketMessageAndDontSendPushNotificationMessageAndSendEmailMessageAndReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().deviceRegistrationToken(null)
				.isWebSocketConnected(false).build();

		given(loadUserAccountPort.loadUserAccountByUsername(clientUserAccount.getEmail()))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(loadDiscussionsOutput.getTransporter().getEmail()))
				.willReturn(Optional.of(transporterUserAccount));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		givenServerUrlProperties();
		String discussionLink = givenPatchUrl();

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getOauthId(), command.getContent());

		then(messagingPort).should(never()).sendPushMessage(any(PushMessage.class));
		then(sendMessagePort).should(never()).sendMessage(any(MessageDto.class), any(Long.class), any(String.class));

		ArgumentCaptor<EmailMessage> emailMessageCaptor = ArgumentCaptor.forClass(EmailMessage.class);

		then(messagingPort).should(times(1)).sendEmailMessage(emailMessageCaptor.capture());

		assertThat(emailMessageCaptor.getValue().getTo()).isEqualTo(transporterUserAccount.getEmail());

		assertThat(emailMessageCaptor.getValue().getTemplate()).isEqualTo(EmailTemplate.RECEIVED_MESSAGE);

		assertThat(not(emailMessageCaptor.getValue().getParams().isEmpty()));

		assertNotNull(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(0)));

		assertThat(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(0))).isEqualTo(
						clientUserAccount.getFirstname() + " " + clientUserAccount.getLastname().toUpperCase());

		assertNotNull(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(1)));

		assertThat(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(1))).isEqualTo(discussionLink);

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void givenClientDeviceTokenExistsAndClientIsWebSocketConnected_WhenSendMessageToClient_ThenSendWebSocketMessageAndDontSendPushNotificationMessageAndReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder()
				.deviceRegistrationToken("SOME_DEVICE_REGISTRATION_TOKEN").isWebSocketConnected(true).build();

		given(loadUserAccountPort.loadUserAccountByUsername(eq(transporterUserAccount.getEmail())))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(eq(loadDiscussionsOutput.getClient().getEmail())))
				.willReturn(Optional.of(clientUserAccount));

		MessageOutput messageOutput = defaultTransporter1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));
		ZoneId receiverZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				transporterUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				transporterUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead(), true);

		then(messagingPort).should(never()).sendPushMessage(any(PushMessage.class));
		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(clientUserAccount.getEmail()));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void givenClientDeviceTokenExistsAndClientWebSocketIsNotConnected_WhenSendMessageToClient_ThenSendPushNotificationMessageAndDontSendWebSocketMessageReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder()
				.deviceRegistrationToken("SOME_DEVICE_REGISTRATION_TOKEN").isWebSocketConnected(false).build();

		given(loadUserAccountPort.loadUserAccountByUsername(eq(transporterUserAccount.getEmail())))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(eq(loadDiscussionsOutput.getClient().getEmail())))
				.willReturn(Optional.of(clientUserAccount));

		MessageOutput messageOutput = defaultTransporter1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));
		ZoneId receiverZoneId = ZoneId.of(clientUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				transporterUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				transporterUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead(), true);

		String messageNotification = mapper
				.writeValueAsString(new MessageNotification(toReceiverMessageDto, loadDiscussionsOutput.getId()));

		PushMessage pushMessage = PushMessage.builder().to(clientUserAccount.getDeviceRegistrationToken())
				.template(PushTemplate.MESSAGE_RECEIVED)
				.params(Map.of(PushTemplate.MESSAGE_RECEIVED.getTemplateParams().get(0),
						transporterUserAccount.getFirstname() + " " + transporterUserAccount.getLastname()))
				.data(Map.of("type", "message", "content", messageNotification)).language("fr_FR").build();

		then(messagingPort).should(times(1)).sendPushMessage(eq(pushMessage));
		then(sendMessagePort).should(never()).sendMessage(any(MessageDto.class), any(Long.class), any(String.class));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void givenClientDeviceTokenNonExistentAndClientIsNotWebSocketConnected_WhenSendMessageToClient_ThenDontSendWebSocketMessageAndDontSendPushNotificationMessageAndSendEmailMessageAndReturnSentMessage()
			throws JsonProcessingException {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().deviceRegistrationToken(null)
				.isWebSocketConnected(false).build();

		given(loadUserAccountPort.loadUserAccountByUsername(clientUserAccount.getEmail()))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		given(loadUserAccountPort.loadUserAccountByUsername(loadDiscussionsOutput.getTransporter().getEmail()))
				.willReturn(Optional.of(transporterUserAccount));

		MessageOutput messageOutput = defaultTransporter1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of(transporterUserAccount.getPreferences().get(UserPreferenceKey.TIMEZONE));

		givenServerUrlProperties();
		String discussionLink = givenPatchUrl();

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				transporterUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				transporterUserAccount.getOauthId(), command.getContent());

		then(messagingPort).should(never()).sendPushMessage(any(PushMessage.class));
		then(sendMessagePort).should(never()).sendMessage(any(MessageDto.class), any(Long.class), any(String.class));

		ArgumentCaptor<EmailMessage> emailMessageCaptor = ArgumentCaptor.forClass(EmailMessage.class);

		then(messagingPort).should(times(1)).sendEmailMessage(emailMessageCaptor.capture());

		assertThat(emailMessageCaptor.getValue().getTo()).isEqualTo(clientUserAccount.getEmail());

		assertThat(emailMessageCaptor.getValue().getTemplate()).isEqualTo(EmailTemplate.RECEIVED_MESSAGE);

		assertThat(not(emailMessageCaptor.getValue().getParams().isEmpty()));

		assertNotNull(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(0)));

		assertThat(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(0)))
						.isEqualTo(transporterUserAccount.getFirstname() + " "
								+ transporterUserAccount.getLastname().toUpperCase());

		assertNotNull(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(1)));

		assertThat(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.RECEIVED_MESSAGE.getTemplateParams().get(1))).isEqualTo(discussionLink);

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
		assertEquals(true, messageDto.getSent());
	}

	@Test
	void testPatch() {
		String generatedLink = chatMessageService.patchURL("${protocol}://${host}:${port}/discussions", "https",
				"localhost", "8443");
		assertEquals("https://localhost:8443/discussions", generatedLink);
	}

	@Test
	void givenInexistentDiscussionId_WhenSendMessage_ThenThrowDiscussionNotFoundException() {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.empty());

		// when
		// then
		assertThrows(DiscussionNotFoundException.class,
				() -> chatMessageService.sendMessage(command, 1L, clientUserAccount.getEmail()));

	}

	@Test
	void givenAuthenticatedClientIdIsDifferentFromDiscussionClientId_WhenSendMessage_ThenThrowDiscussionNotFoundException() {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().oauthId(400L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
		// when
		// then
		assertThrows(OperationDeniedException.class,
				() -> chatMessageService.sendMessage(command, 1L, clientUserAccount.getEmail()));

	}

	@Test
	void givenAuthenticatedTransporterIdIsDifferentFromDiscussionTransporterId_WhenSendMessage_ThenThrowDiscussionNotFoundException() {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().oauthId(600L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
		// when
		// then
		assertThrows(OperationDeniedException.class,
				() -> chatMessageService.sendMessage(command, 1L, transporterUserAccount.getEmail()));

	}

	@Test
	void givenInexistentDiscussion_WhenLoadMessages_ThenThrowDiscussionNotFoundException() {
		// given

		LoadMessagesCommandBuilder commandBuilder = defaultLoadMessagesCommandBuilder();
		LoadMessagesCommand command = commandBuilder.build();

		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.empty());
		// when

		// then
		assertThrows(DiscussionNotFoundException.class, () -> chatMessageService.loadMessages(command));
	}

	@Test
	void givenAuthenticatedClientIsDifferentThanDiscussionClient_WhenLoadMessages_ThenThrowOperationDeniedException() {
		// given

		LoadMessagesCommandBuilder commandBuilder = defaultLoadMessagesCommandBuilder();
		LoadMessagesCommand command = commandBuilder.build();

		UserAccount clientUserAccount = defaultClientUserAccountBuilder().oauthId(400L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
		// when

		// then
		assertThrows(OperationDeniedException.class, () -> chatMessageService.loadMessages(command));
	}

	@Test
	void givenAuthenticatedTransporterIsDifferentThanDiscussionTransporter_WhenLoadMessages_ThenThrowOperationDeniedException() {
		// given

		LoadMessagesCommandBuilder commandBuilder = defaultLoadMessagesCommandBuilder();
		LoadMessagesCommand command = commandBuilder.build();

		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().oauthId(600L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
		// when

		// then
		assertThrows(OperationDeniedException.class, () -> chatMessageService.loadMessages(command));
	}

	@Test
	void testLoadTransporterMessages() {
		// given

		LoadMessagesCommandBuilder commandBuilder = defaultLoadMessagesCommandBuilder();
		LoadMessagesCommand command = commandBuilder.build();

		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultTransporterLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

		LoadMessagesOutputResult messagesOutputResult = defaultLoadMessagesOutputResult();
		given(loadMessagesPort.loadMessages(command.getDiscussionId(), command.getPageNumber(), command.getPageSize(),
				command.getSortingCriterion())).willReturn(messagesOutputResult);
		// when
		LoadMessagesResult result = chatMessageService.loadMessages(command);
		// then
		List<Long> messageIds = messagesOutputResult.getContent().stream()
				.filter(m -> !m.getRead() && !m.getAuthorId().equals(transporterUserAccount.getOauthId()))
				.map(m -> m.getId()).collect(Collectors.toList());

		then(updateMessagePort).should(times(1)).updateRead(messageIds, true);

		then(sendMessageNotificationPort).should(times(1)).sendReadNotification(
				loadDiscussionsOutput.getClient().getEmail(), loadDiscussionsOutput.getId(), messageIds);

		assertEquals(command.getPageNumber(), result.getPageNumber());
		assertEquals(command.getPageSize(), result.getPageSize());
		assertEquals(messagesOutputResult.getTotalElements(), result.getTotalElements());
		assertEquals(messagesOutputResult.getTotalPages(), result.getTotalPages());
		assertEquals(messagesOutputResult.isHasNext(), result.isHasNext());
		assertEquals(messagesOutputResult.getContent().stream()
				.map(m -> DiscussionUtils.mapToMessageDto(dateTimeHelper, m, userZoneId)).collect(Collectors.toList()),
				result.getContent());
	}

	@Test
	void testLoadClientMessages() {
		// given

		LoadMessagesCommandBuilder commandBuilder = defaultLoadMessagesCommandBuilder();
		LoadMessagesCommand command = commandBuilder.build();

		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

		LoadMessagesOutputResult messagesOutputResult = defaultLoadMessagesOutputResult();
		given(loadMessagesPort.loadMessages(command.getDiscussionId(), command.getPageNumber(), command.getPageSize(),
				command.getSortingCriterion())).willReturn(messagesOutputResult);
		// when
		LoadMessagesResult result = chatMessageService.loadMessages(command);
		// then
		List<Long> messageIds = messagesOutputResult.getContent().stream()
				.filter(m -> !m.getRead() && !m.getAuthorId().equals(clientUserAccount.getOauthId()))
				.map(m -> m.getId()).collect(Collectors.toList());

		then(updateMessagePort).should(times(1)).updateRead(messageIds, true);

		then(sendMessageNotificationPort).should(times(1)).sendReadNotification(
				loadDiscussionsOutput.getTransporter().getEmail(), loadDiscussionsOutput.getId(), messageIds);

		assertEquals(command.getPageNumber(), result.getPageNumber());
		assertEquals(command.getPageSize(), result.getPageSize());
		assertEquals(messagesOutputResult.getTotalElements(), result.getTotalElements());
		assertEquals(messagesOutputResult.getTotalPages(), result.getTotalPages());
		assertEquals(messagesOutputResult.isHasNext(), result.isHasNext());
		assertEquals(messagesOutputResult.getContent().stream()
				.map(m -> DiscussionUtils.mapToMessageDto(dateTimeHelper, m, userZoneId)).collect(Collectors.toList()),
				result.getContent());
	}

	@Test
	void givenNonExistentDiscussion_whenUpdateMessageReadStatus_thenThrowDiscussionNotFoundException() {
		// given
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.empty());
		UserAccount userAccount = defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();
		// when // then

		assertThrows(DiscussionNotFoundException.class,
				() -> chatMessageService.updateMessageReadStatus(1L, 2L, TestConstants.DEFAULT_EMAIL, command));

	}

	@Test
	void givenTransporterAndNotTransporterDiscussion_whenUpdateMessageReadStatus_thenThrowOperationDeniedException() {
		// given
		LoadDiscussionsOutput discussionOutput = defaultTransporterLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(discussionOutput));
		UserAccount userAccount = defaultTransporterUserAccountBuilder()
				.oauthId(discussionOutput.getTransporter().getId() + 1).build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();
		// when // then

		assertThrows(OperationDeniedException.class,
				() -> chatMessageService.updateMessageReadStatus(1L, 2L, TestConstants.DEFAULT_EMAIL, command));

	}

	@Test
	void givenClientAndNotClientDiscussion_whenUpdateMessageReadStatus_thenThrowOperationDeniedException() {
		// given
		LoadDiscussionsOutput discussionOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(discussionOutput));
		UserAccount userAccount = defaultClientUserAccountBuilder().oauthId(discussionOutput.getClient().getId() + 1)
				.build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();
		// when // then

		assertThrows(OperationDeniedException.class,
				() -> chatMessageService.updateMessageReadStatus(1L, 2L, TestConstants.DEFAULT_EMAIL, command));

	}

	@Test
	void givenClientAndClientDiscussion_whenUpdateMessageReadStatus_thenUpdateMessageReadToTrue() {
		// given
		LoadDiscussionsOutput discussionOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(discussionOutput));
		UserAccount userAccount = defaultClientUserAccountBuilder().oauthId(discussionOutput.getClient().getId())
				.build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();
		// when

		chatMessageService.updateMessageReadStatus(discussionOutput.getId(),
				discussionOutput.getLatestMessage().getId(), TestConstants.DEFAULT_EMAIL, command);

		// then

		then(updateMessagePort).should(times(1)).updateRead(List.of(discussionOutput.getLatestMessage().getId()),
				Boolean.valueOf(command.getIsRead()));

	}

	@Test
	void givenCountMessageCommand_whenCountMessages_thenReturnMessagesCount() {
		// given
		CountMessagesCommand command = CountMessagesCommand.builder().username(TestConstants.DEFAULT_EMAIL)
				.read("false").build();
		given(loadMessagesPort.countMessages(any(String.class), any(Boolean.class), any(Boolean.class)))
				.willReturn(10L);
		UserAccount userAccount = defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));
		// When
		Long count = chatMessageService.countMessages(command);
		// then
		assertEquals(10L, count);
	}

	private String givenPatchUrl() {
		String result = "SOME_EMAIL_VALIDATION_LINK";

		doReturn(result).when(chatMessageService).patchURL(any(String.class), any(String.class), any(String.class),
				any(String.class));
		return result;
	}

	private void givenServerUrlProperties() {
		given(serverUrlProperties.getHost()).willReturn("HOST");
		given(serverUrlProperties.getPort()).willReturn("PORT");
		given(serverUrlProperties.getProtocol()).willReturn("PROTOCOL");

	}
}
