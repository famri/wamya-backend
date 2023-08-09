package com.excentria_it.wamya.domain;

import java.util.List;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Generated
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
