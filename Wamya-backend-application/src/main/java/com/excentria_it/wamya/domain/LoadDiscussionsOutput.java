package com.excentria_it.wamya.domain;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadDiscussionsOutput {
	private Long id;

	private Boolean active;

	private Instant dateTime;

	private MessageOutput latestMessage;

	private Interlocutor client;

	private Interlocutor transporter;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class MessageOutput {
		private Long id;
		private Long authorId;
		private String content;
		private Instant dateTime;
		private Boolean read;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class Interlocutor {
		private Long id;
		private String name;
		private String mobileNumber;
		private String photoUrl;

	}
}
