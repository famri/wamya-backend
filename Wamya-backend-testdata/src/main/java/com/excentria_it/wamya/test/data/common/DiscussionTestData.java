package com.excentria_it.wamya.test.data.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand.CreateDiscussionCommandBuilder;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand.FindDiscussionByClientIdAndTransporterIdCommandBuilder;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand.FindDiscussionByIdCommandBuilder;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand.LoadDiscussionsCommandBuilder;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.InterlocutorDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.InterlocutorOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;

public class DiscussionTestData {

	private static final Instant instant1 = ZonedDateTime.of(2021, 03, 19, 20, 00, 00, 0, ZoneId.of("UTC")).toInstant();
	private static final Instant instant2 = ZonedDateTime.of(2021, 03, 14, 10, 30, 00, 0, ZoneId.of("UTC")).toInstant();

	private static final List<MessageOutput> clientDiscussion1Messages = List.of(
			new MessageOutput(1L, 100L, "Hello!", instant1.plusSeconds(10), false),
			new MessageOutput(1L, 200L, "Hi! Can I help you?", instant1.plusSeconds(15), false));

	private static final List<MessageOutput> clientDiscussion2Messages = List.of(
			new MessageOutput(1L, 100L, "Hello!", instant1.plusSeconds(10), false),
			new MessageOutput(1L, 300L, "Hello Sir! How can I help you?", instant2.plusSeconds(15), false));

	private static final InterlocutorOutput clientDiscussionClientInterlocutor = InterlocutorOutput.builder().id(100L)
			.email("client1@gmail.com").mobileNumber("+216_96111111").name("Client 1")
			.photoUrl("https://path/to/client1/photo").build();

	private static final InterlocutorOutput clientDiscussion1TransporterInterlocutor = InterlocutorOutput.builder()
			.id(200L).email("transporter1@gmail.com").mobileNumber("+216_96222222").name("Transporter 1")
			.photoUrl("https://path/to/transporter1/photo").build();

	private static final InterlocutorOutput clientDiscussion2TransporterInterlocutor = InterlocutorOutput.builder()
			.id(300L).email("transporter2@gmail.com").mobileNumber("+216_96333333").name("Transporter 2")
			.photoUrl("https://path/to/transporter2/photo").build();

	private static final List<LoadDiscussionsOutput> loadDiscussionsOutputList = List.of(
			new LoadDiscussionsOutput(1L, true, instant1, clientDiscussion1Messages.get(1),
					clientDiscussionClientInterlocutor, clientDiscussion1TransporterInterlocutor),
			new LoadDiscussionsOutput(1L, true, instant2, clientDiscussion2Messages.get(1),
					clientDiscussionClientInterlocutor, clientDiscussion2TransporterInterlocutor));

	public static LoadDiscussionsOutputResult defaultLoadDiscussionsOutputResult() {

		return new LoadDiscussionsOutputResult(1, 2, 0, 25, false, loadDiscussionsOutputList);
	}

	public static LoadDiscussionsOutput defaultClientLoadDiscussionsOutput() {

		return loadDiscussionsOutputList.get(0);
	}

	public static LoadDiscussionsOutput defaultTransporterLoadDiscussionsOutput() {

		return loadDiscussionsOutputList.get(0);
	}

	public static LoadDiscussionsDto defaultLoadDiscussionsDto() {

		return new LoadDiscussionsDto(loadDiscussionsOutputList.get(0).getId(),
				loadDiscussionsOutputList.get(0).getActive(),
				loadDiscussionsOutputList.get(0).getDateTime().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
				new MessageDto(loadDiscussionsOutputList.get(0).getLatestMessage().getId(),
						loadDiscussionsOutputList.get(0).getLatestMessage().getAuthorId(),
						loadDiscussionsOutputList.get(0).getLatestMessage().getContent(),
						loadDiscussionsOutputList.get(0).getLatestMessage().getDateTime()
								.atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
						loadDiscussionsOutputList.get(0).getLatestMessage().getRead()),
				new InterlocutorDto(loadDiscussionsOutputList.get(0).getClient().getId(),
						loadDiscussionsOutputList.get(0).getClient().getName(),
						loadDiscussionsOutputList.get(0).getClient().getMobileNumber(),
						loadDiscussionsOutputList.get(0).getClient().getPhotoUrl()),
				new InterlocutorDto(loadDiscussionsOutputList.get(0).getTransporter().getId(),
						loadDiscussionsOutputList.get(0).getTransporter().getName(),
						loadDiscussionsOutputList.get(0).getTransporter().getMobileNumber(),
						loadDiscussionsOutputList.get(0).getTransporter().getPhotoUrl()));
	}

	public static List<MessageOutput> defaultMessageOutputList() {
		return clientDiscussion1Messages;
	}

	public static MessageOutput defaultClient1MessageOutput() {
		return clientDiscussion1Messages.get(0);
	}

	public static MessageOutput defaultTransporter1MessageOutput() {
		return clientDiscussion1Messages.get(1);
	}

	public static LoadDiscussionsCommandBuilder defaultLoadDiscussionsCommandBuilder() {
		return LoadDiscussionsCommand.builder().username("client1@gmail.com").pageNumber(0).pageSize(25)
				.sortingCriterion(new SortCriterion("date-time", "desc"))
				.filteringCriterion(new FilterCriterion("active", "true"));
	}

	public static FindDiscussionByClientIdAndTransporterIdCommandBuilder defaultFindDiscussionByClientIdAndTransporterIdCommandBuilder() {
		return FindDiscussionByClientIdAndTransporterIdCommand.builder().clientId(100L).transporterId(200L)
				.username("client1@gmail.com");
	}

	public static FindDiscussionByIdCommandBuilder defaultFindDiscussionByIdCommandBuilder() {
		return FindDiscussionByIdCommand.builder().discussionId(100L).username("client1@gmail.com");
	}

	public static CreateDiscussionCommandBuilder defaultCreateDiscussionCommandBuilder() {
		return CreateDiscussionCommand.builder().clientId(100L).transporterId(200L);
	}

	public static LoadDiscussionsResult defaultLoadDiscussionsResult() {

		return new LoadDiscussionsResult(1, 2, 0, 25, false,
				loadDiscussionsOutputList.stream()
						.map(ldo -> new LoadDiscussionsDto(ldo.getId(), ldo.getActive(),
								ldo.getDateTime().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime(),
								new MessageDto(ldo.getLatestMessage().getId(), ldo.getLatestMessage().getAuthorId(),
										ldo.getLatestMessage().getContent(),
										ldo.getLatestMessage().getDateTime().atZone(ZoneId.of("Africa/Tunis"))
												.toLocalDateTime(),
										ldo.getLatestMessage().getRead()),
								new InterlocutorDto(ldo.getClient().getId(), ldo.getClient().getName(),
										ldo.getClient().getMobileNumber(), ldo.getClient().getPhotoUrl()),
								new InterlocutorDto(ldo.getTransporter().getId(), ldo.getTransporter().getName(),
										ldo.getTransporter().getMobileNumber(), ldo.getTransporter().getPhotoUrl())))
						.collect(Collectors.toList()));
	}

	public static LoadMessagesOutputResult defaultLoadMessagesOutputResult() {
		LoadMessagesOutputResult result = new LoadMessagesOutputResult(1, 2, 0, 25, false, clientDiscussion1Messages);
		return result;
	}

}
