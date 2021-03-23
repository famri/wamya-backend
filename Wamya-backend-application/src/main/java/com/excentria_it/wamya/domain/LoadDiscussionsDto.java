package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;
import java.util.List;

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

	private List<MessageDto> messages;

	private Interlocutor interlocutor;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class MessageDto {
		private Long id;
		private String authorEmail;
		private String authorMobileNumber;
		private String content;
		private LocalDateTime dateTime;
		private Boolean read;
	}
}
