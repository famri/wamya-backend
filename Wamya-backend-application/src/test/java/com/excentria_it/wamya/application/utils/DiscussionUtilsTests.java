package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DiscussionUtilsTests {
	@Mock
	private DateTimeHelper dateTimeHelper;

	@Test
	void givenNullMessageOutput_WhenMapToMessageDto_ThenReturnNull() {

		assertNull(DiscussionUtils.mapToMessageDto(dateTimeHelper, null, ZoneId.of("Africa/Tunis")));
	}

	@Test
	void givenNullInterlocutorOutput_WhenMapToInterlocutorDto_ThenReturnNull() {

		assertNull(DiscussionUtils.mapToInterlocutorDto(null));
	}
}
