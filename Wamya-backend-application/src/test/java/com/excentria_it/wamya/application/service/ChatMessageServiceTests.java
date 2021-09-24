package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static com.excentria_it.wamya.test.data.common.MessageTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand.LoadMessagesCommandBuilder;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadMessagesPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SendMessageNotificationPort;
import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.application.port.out.UpdateMessagePort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.application.utils.DiscussionUtils;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;
import com.excentria_it.wamya.domain.LoadMessagesResult;
import com.excentria_it.wamya.domain.UserAccount;

@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTests {
	@Mock
	private LoadDiscussionsPort loadDiscussionsPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Mock
	private AddMessageToDiscussionPort addMessageToDiscussionPort;
	@Mock
	private SendMessagePort sendMessagePort;
	@Mock
	private LoadMessagesPort loadMessagesPort;

	@Mock
	private UpdateMessagePort updateMessagePort;
	@Mock
	private SendMessageNotificationPort sendMessageNotificationPort;

	@Spy
	private DateTimeHelper dateTimeHelper;

	@InjectMocks
	private ChatMessageService chatMessageService;

	@Test
	void givenClientUsername_WhenSendMessage_ThenReturnSentMessage() {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of("Africa/Tunis");
		ZoneId receiverZoneId = ZoneId.of("Europe/Helsinki");

		doReturn(senderZoneId).when(dateTimeHelper).findUserZoneId(clientUserAccount.getEmail());

		doReturn(receiverZoneId).when(dateTimeHelper).findUserZoneId(loadDiscussionsOutput.getTransporter().getEmail());

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead());

//		MessageDto toSenderMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
//				messageOutput.getContent(),
//				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
//				messageOutput.getRead());

		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(loadDiscussionsOutput.getTransporter().getEmail()));
//		then(sendMessagePort).should(times(1)).sendMessage(eq(toSenderMessageDto), eq(loadDiscussionsOutput.getId()),
//				eq(clientUserAccount.getEmail()));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
	}

	@Test
	void givenTransporterUsername_WhenSendMessage_ThenReturnSentMessage() {
		// given
		SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
		SendMessageCommand command = commandBuilder.build();
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultClientLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		MessageOutput messageOutput = defaultTransporter1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of("Africa/Tunis");
		ZoneId receiverZoneId = ZoneId.of("Europe/Helsinki");

		doReturn(senderZoneId).when(dateTimeHelper).findUserZoneId(transporterUserAccount.getEmail());

		doReturn(receiverZoneId).when(dateTimeHelper).findUserZoneId(loadDiscussionsOutput.getClient().getEmail());

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				transporterUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				transporterUserAccount.getOauthId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead());

//		MessageDto toSenderMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
//				messageOutput.getContent(),
//				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
//				messageOutput.getRead());

		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(loadDiscussionsOutput.getClient().getEmail()));
//		then(sendMessagePort).should(times(1)).sendMessage(eq(toSenderMessageDto), eq(loadDiscussionsOutput.getId()),
//				eq(transporterUserAccount.getEmail()));

		assertEquals(messageOutput.getId(), messageDto.getId());
		assertEquals(messageOutput.getAuthorId(), messageDto.getAuthorId());
		assertEquals(messageOutput.getContent(), messageDto.getContent());
		assertEquals(dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageDto.getDateTime());

		assertEquals(messageOutput.getRead(), messageDto.getRead());
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
				.filter(m -> !m.getRead() && !m.getAuthorId().equals(clientUserAccount.getOauthId())).map(m -> m.getId())
				.collect(Collectors.toList());

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

}
