package com.excentria_it.wamya.domain;

import org.springframework.stereotype.*;

import lombok.*;

@NoArgsConstructor
@Component
public class MessageBuilder {

	private String content;

	public MessageBuilder withContent(String content) {
		this.content = content;
		return this;
	}

	public Message build() {
		return new Message(this.content);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Message {
		@Getter
		private final String content;
	}

}
