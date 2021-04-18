package com.excentria_it.wamya.domain;

import java.io.Serializable;

import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage{

	
	private MessageDto messageDto;
	private Long discussionId;

}
