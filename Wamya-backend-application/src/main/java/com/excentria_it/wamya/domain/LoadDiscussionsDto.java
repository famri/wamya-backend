package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import com.excentria_it.wamya.domain.LoadDiscussionsOutput.Interlocutor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadDiscussionsDto {
	private Long id;

	private Boolean active;

	private LocalDateTime dateTime;

	private MessageDto latestMessage;

	private Interlocutor client;

	private Interlocutor transporter;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class MessageDto {
		private Long id;
		private Long authorId;
		private String content;
		private LocalDateTime dateTime;
		private Boolean read;
	}
}
