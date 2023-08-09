package com.excentria_it.wamya.adapter.messaging.adapter;

import com.excentria_it.wamya.domain.ChatMessage;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.ReadUpdate;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

@ExtendWith(MockitoExtension.class)
public class SimpChatMessageAdapterTests {
    @Mock
    private SimpMessageSendingOperations simpMessagingTemplate;

    @InjectMocks
    private SimpChatMessageAdapter chatMessageAdapter;

    @Test
    void testSendMessage() {
        // given
        ArgumentCaptor<ChatMessage> chatMessageCaptor = ArgumentCaptor.forClass(ChatMessage.class);

        MessageDto messageDto = new MessageDto(1L, "1", "some message",
                Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(), false, true);
        // When
        chatMessageAdapter.sendMessage(messageDto, 1L, TestConstants.DEFAULT_EMAIL);
        // Then
        then(simpMessagingTemplate).should(times(1)).convertAndSendToUser(eq(TestConstants.DEFAULT_EMAIL),
                eq("/exchange/amq.direct/messages"), chatMessageCaptor.capture());

        assertEquals(messageDto, chatMessageCaptor.getValue().getMessageDto());
        assertEquals(1L, chatMessageCaptor.getValue().getDiscussionId());
    }

    @Test
    void testSendReadNotification() {
        // given
        List<Long> messageIds = List.of(1L, 2L, 3L);
        ArgumentCaptor<ReadUpdate> readUpdateCaptor = ArgumentCaptor.forClass(ReadUpdate.class);

        // When
        chatMessageAdapter.sendReadNotification(TestConstants.DEFAULT_EMAIL, 1L, messageIds);
        // Then
        then(simpMessagingTemplate).should(times(1)).convertAndSendToUser(eq(TestConstants.DEFAULT_EMAIL),
                eq("/exchange/amq.direct/read-messages"), readUpdateCaptor.capture());

        assertEquals(1L, readUpdateCaptor.getValue().getDiscussionId());
        assertEquals(messageIds, readUpdateCaptor.getValue().getMessagesIds());
    }

}
