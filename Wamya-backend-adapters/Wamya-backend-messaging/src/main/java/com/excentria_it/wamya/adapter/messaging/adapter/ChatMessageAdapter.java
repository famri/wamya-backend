package com.excentria_it.wamya.adapter.messaging.adapter;

import org.springframework.messaging.simp.SimpMessageSendingOperations;

import com.excentria_it.wamya.application.port.out.SendMessagePort;
import com.excentria_it.wamya.common.annotation.WebSocketAdapter;
import com.excentria_it.wamya.domain.ChatMessage;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.RequiredArgsConstructor;

@WebSocketAdapter
@RequiredArgsConstructor
public class ChatMessageAdapter implements SendMessagePort {

	private final SimpMessageSendingOperations simpMessagingTemplate;

	@Override
	public void sendMessage(MessageDto messageDto, Long discussionId, Long receiverUserId) {
		simpMessagingTemplate.convertAndSendToUser(receiverUserId.toString(), "/queue/messages",
				new ChatMessage(messageDto, discussionId));

	}

}
