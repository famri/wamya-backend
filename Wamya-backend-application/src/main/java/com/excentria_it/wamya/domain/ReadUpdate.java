package com.excentria_it.wamya.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadUpdate {

	private WebSocketMessageType type = WebSocketMessageType.READ_MESSAGE;

	private Long discussionId;
	private List<Long> messagesIds;

	public ReadUpdate(Long discussionId, List<Long> messagesIds) {
		super();
		this.discussionId = discussionId;
		this.messagesIds = messagesIds;
	}

}
