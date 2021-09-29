package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand.LoadMessagesCommandBuilder;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadMessagesResult;

public class MessageTestData {

	private static final List<MessageDto> messages = List.of(
			new MessageDto(1L, 1L, "Hello!", Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(), true, true),
			new MessageDto(2L, 2L, "Hello Sir!How can I help you?",
					Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(), false, true));

	public static SendMessageCommandBuilder defaultSendMessageCommandBuilder() {
		return SendMessageCommand.builder().content("Hello!");
	}

	public static LoadMessagesCommandBuilder defaultLoadMessagesCommandBuilder() {
		return LoadMessagesCommand.builder().discussionId(1L).pageNumber(0).pageSize(25)
				.username(TestConstants.DEFAULT_EMAIL).sortingCriterion(new SortCriterion("date-time", "desc"));
	}

	public static MessageDto defaultMessageDto() {

		return messages.get(0);
	}

	public static LoadMessagesResult defaultLoadMessagesResult() {
		return new LoadMessagesResult(1, 2, 0, 25, false, messages);
	}
}
