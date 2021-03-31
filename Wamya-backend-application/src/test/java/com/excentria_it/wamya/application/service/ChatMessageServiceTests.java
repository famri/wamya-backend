package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static com.excentria_it.wamya.test.data.common.MessageTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.exception.DiscussionNotFoundException;
import com.excentria_it.wamya.common.exception.OperationDeniedException;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
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

		LoadDiscussionsOutput loadDiscussionsOutput = defaultLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		MessageOutput messageOutput = defaultClient1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of("Africa/Tunis");
		ZoneId receiverZoneId = ZoneId.of("Europe/Helsinki");

		doReturn(senderZoneId).when(dateTimeHelper).findUserZoneId(clientUserAccount.getEmail());

		doReturn(receiverZoneId).when(dateTimeHelper).findUserZoneId(loadDiscussionsOutput.getTransporter().getId());

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				clientUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(clientUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				clientUserAccount.getId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead());

		MessageDto toSenderMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageOutput.getRead());

		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(loadDiscussionsOutput.getTransporter().getId()));
		then(sendMessagePort).should(times(1)).sendMessage(eq(toSenderMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(clientUserAccount.getId()));

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

		LoadDiscussionsOutput loadDiscussionsOutput = defaultLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));

		MessageOutput messageOutput = defaultTransporter1MessageOutput();
		given(addMessageToDiscussionPort.addMessage(any(Long.class), any(Long.class), any(String.class)))
				.willReturn(messageOutput);

		ZoneId senderZoneId = ZoneId.of("Africa/Tunis");
		ZoneId receiverZoneId = ZoneId.of("Europe/Helsinki");

		doReturn(senderZoneId).when(dateTimeHelper).findUserZoneId(transporterUserAccount.getEmail());

		doReturn(receiverZoneId).when(dateTimeHelper).findUserZoneId(loadDiscussionsOutput.getClient().getId());

		// when
		MessageDto messageDto = chatMessageService.sendMessage(command, loadDiscussionsOutput.getId(),
				transporterUserAccount.getEmail());
		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(transporterUserAccount.getEmail());
		then(loadDiscussionsPort).should(times(1)).loadDiscussionById(loadDiscussionsOutput.getId());
		then(addMessageToDiscussionPort).should(times(1)).addMessage(loadDiscussionsOutput.getId(),
				transporterUserAccount.getId(), command.getContent());

		MessageDto toReceiverMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), receiverZoneId),
				messageOutput.getRead());

		MessageDto toSenderMessageDto = new MessageDto(messageOutput.getId(), messageOutput.getAuthorId(),
				messageOutput.getContent(),
				dateTimeHelper.systemToUserLocalDateTime(messageOutput.getDateTime(), senderZoneId),
				messageOutput.getRead());

		then(sendMessagePort).should(times(1)).sendMessage(eq(toReceiverMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(loadDiscussionsOutput.getClient().getId()));
		then(sendMessagePort).should(times(1)).sendMessage(eq(toSenderMessageDto), eq(loadDiscussionsOutput.getId()),
				eq(transporterUserAccount.getId()));

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
		UserAccount clientUserAccount = defaultClientUserAccountBuilder().id(100L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(clientUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultLoadDiscussionsOutput();
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
		UserAccount transporterUserAccount = defaultTransporterUserAccountBuilder().id(200L).build();

		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class)))
				.willReturn(Optional.of(transporterUserAccount));

		LoadDiscussionsOutput loadDiscussionsOutput = defaultLoadDiscussionsOutput();
		given(loadDiscussionsPort.loadDiscussionById(any(Long.class))).willReturn(Optional.of(loadDiscussionsOutput));
		// when
		// then
		assertThrows(OperationDeniedException.class,
				() -> chatMessageService.sendMessage(command, 1L, transporterUserAccount.getEmail()));

	}

}
