package com.excentria_it.wamya.domain;

import java.time.Instant;
import java.util.List;

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

	private List<MessageOutput> messages;

	private Interlocutor interlocutor;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	public static class MessageOutput {
		private Long id;
		private String authorEmail;
		private String authorMobileNumber;
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
		private String email;
		private String mobileNumber;
		private String photoUrl;

	}
}
