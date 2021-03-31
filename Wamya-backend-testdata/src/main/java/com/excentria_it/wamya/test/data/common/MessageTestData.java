package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.time.ZoneId;

import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

public class MessageTestData {
	public static SendMessageCommandBuilder defaultSendMessageCommandBuilder() {
		return SendMessageCommand.builder().content("Hello!");
	}

	public static MessageDto defaultMessageDto() {

		return new MessageDto(1L, 1L, "Hello!", Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
				false);
	}
}
