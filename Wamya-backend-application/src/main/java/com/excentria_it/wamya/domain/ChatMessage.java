package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

	private WebSocketMessageType type = WebSocketMessageType.CHAT_MESSAGE;

	private MessageDto messageDto;
	private Long discussionId;

	public ChatMessage(MessageDto messageDto, Long discussionId) {
		super();
		this.messageDto = messageDto;
		this.discussionId = discussionId;
	}

}
