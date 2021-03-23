package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand.LoadDiscussionsCommandBuilder;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.Interlocutor;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;

public class DiscussionTestData {

	private static final Instant instant1 = ZonedDateTime.of(2021, 03, 19, 20, 00, 00, 0, ZoneId.of("UTC")).toInstant();
	private static final Instant instant2 = ZonedDateTime.of(2021, 03, 14, 10, 30, 00, 0, ZoneId.of("UTC")).toInstant();

	private static final List<MessageOutput> clientDiscussion1Messages = List.of(
			new MessageOutput(1L, "client1@gmail.com", "+216_22111111", "Hello!", instant1.plusSeconds(10), false),
			new MessageOutput(1L, "transporter1@gmail.com", "+216_96111111", "Hi! Can I help you?",
					instant1.plusSeconds(15), false));

	private static final List<MessageOutput> clientDiscussion2Messages = List.of(
			new MessageOutput(1L, "client1@gmail.com", "+216_22111111", "Hello!", instant1.plusSeconds(10), false),
			new MessageOutput(1L, "transporter2@gmail.com", "+216_96222222", "Hello Sir! How can I help you?",
					instant2.plusSeconds(15), false));

	private static final Interlocutor clientDiscussion1Interlocutor = Interlocutor.builder().id(1L)
			.email("transporter1@gmail.com").mobileNumber("+216_96111111").name("Transporter 1")
			.photoUrl("https://path/to/transporter1/photo").build();

	private static final Interlocutor clientDiscussion2Interlocutor = Interlocutor.builder().id(2L)
			.email("transporter2@gmail.com").mobileNumber("+216_96222222").name("Transporter 2")
			.photoUrl("https://path/to/transporter2/photo").build();

	private static final List<LoadDiscussionsOutput> loadDiscussionsOutputList = List.of(
			new LoadDiscussionsOutput(1L, true, instant1, clientDiscussion1Messages, clientDiscussion1Interlocutor),
			new LoadDiscussionsOutput(1L, true, instant2, clientDiscussion2Messages, clientDiscussion2Interlocutor));

	public static LoadDiscussionsOutputResult defaultLoadDiscussionsOutputResult() {

		return new LoadDiscussionsOutputResult(1, 2, 0, 25, false, loadDiscussionsOutputList);
	}

	public static List<MessageOutput> defaultMessageOutputList() {
		return clientDiscussion1Messages;
	}

	public static LoadDiscussionsCommandBuilder defaultLoadDiscussionsCommandBuilder() {
		return LoadDiscussionsCommand.builder().username("client1@gmail.com").pageNumber(0).pageSize(25)
				.sortingCriterion(new SortCriterion("date-time", "desc"))
				.filteringCriterion(new FilterCriterion("active", "true"));
	}

	public static LoadDiscussionsResult defaultLoadDiscussionsResult() {

		return new LoadDiscussionsResult(1, 2, 0, 25, false,
				loadDiscussionsOutputList
						.stream().map(
								ldo -> new LoadDiscussionsDto(ldo.getId(), ldo.getActive(),
										ldo.getDateTime().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
										ldo.getMessages().stream()
												.map(m -> new MessageDto(m.getId(), m.getAuthorEmail(),
														m.getAuthorMobileNumber(), m.getContent(),
														m.getDateTime().atZone(ZoneId.of("Africa/Tunis"))
																.toLocalDateTime(),
														m.getRead()))
												.collect(Collectors.toList()),
										ldo.getInterlocutor()))
						.collect(Collectors.toList()));
	}
}
