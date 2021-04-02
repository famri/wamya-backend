package com.excentria_it.wamya.adapter.messaging.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.excentria_it.wamya.domain.ChatMessage;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class ChatMessageAdapterTests {
	@Mock
	private SimpMessageSendingOperations simpMessagingTemplate;

	@InjectMocks
	private ChatMessageAdapter chatMessageAdapter;

	@Test
	void testSendMessage() {
		// given
		ArgumentCaptor<ChatMessage> chatMessageCaptor = ArgumentCaptor.forClass(ChatMessage.class);

		MessageDto messageDto = new MessageDto(1L, 1L, "some message",
				Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(), false);
		// When
		chatMessageAdapter.sendMessage(messageDto, 1L, TestConstants.DEFAULT_EMAIL);
		// Then
		then(simpMessagingTemplate).should(times(1)).convertAndSendToUser(eq(TestConstants.DEFAULT_EMAIL),
				eq("/queue/messages"), chatMessageCaptor.capture());

		assertEquals(messageDto, chatMessageCaptor.getValue().getMessageDto());
		assertEquals(1L, chatMessageCaptor.getValue().getDiscussionId());
	}
}
