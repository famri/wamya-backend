package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase.LoadMessagesCommand.LoadMessagesCommandBuilder;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadMessagesResult;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public class MessageTestData {
    private static final String UUID_1 = "2b3d79f5-7e43-42e4-b246-cc5db4194db9";
    private static final String UUID_2 = "388444a0-3085-4cc2-a4a6-0070667a2cb7";
    private static final List<MessageDto> messages = List.of(
            new MessageDto(1L, UUID_1, "Hello!", Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(), true, true),
            new MessageDto(2L, UUID_2, "Hello Sir!How can I help you?",
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
