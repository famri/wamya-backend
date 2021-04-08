package com.excentria_it.wamya.adapter.messaging.adapter;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.excentria_it.wamya.application.port.out.SendMessageNotificationPort;
import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.common.annotation.WebSocketAdapter;
import com.excentria_it.wamya.domain.ChatMessage;
import com.excentria_it.wamya.domain.ReadUpdate;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.RequiredArgsConstructor;

@WebSocketAdapter
@RequiredArgsConstructor
public class ChatMessageAdapter implements SendMessagePort, SendMessageNotificationPort {

	private final SimpMessageSendingOperations simpMessagingTemplate;

	@Override
	public void sendMessage(MessageDto messageDto, Long discussionId, String receiverUsername) {
		simpMessagingTemplate.convertAndSendToUser(receiverUsername, "/queue/messages",
				new ChatMessage(messageDto, discussionId));

	}

	@Override
	public void sendReadNotification(String receiverUsername, Long discussionId, List<Long> messagesIds) {
		simpMessagingTemplate.convertAndSendToUser(receiverUsername, "/queue/read-messages",
				new ReadUpdate(discussionId, messagesIds));

	}

}
