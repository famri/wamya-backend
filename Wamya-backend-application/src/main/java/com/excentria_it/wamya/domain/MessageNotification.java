package com.excentria_it.wamya.domain;

import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageNotification {

	private MessageDto message;

	private Long discussionId;
}
