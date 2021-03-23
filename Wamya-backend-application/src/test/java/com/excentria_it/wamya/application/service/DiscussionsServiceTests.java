package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand.LoadDiscussionsCommandBuilder;
import com.excentria_it.wamya.application.port.out.LoadDiscussionsPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
import com.excentria_it.wamya.domain.UserAccount;

@ExtendWith(MockitoExtension.class)
public class DiscussionsServiceTests {

	@Mock
	private LoadDiscussionsPort loadDiscussionsPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Spy
	private DateTimeHelper dateTimeHelper;

	@InjectMocks
	private DiscussionsService discussionsService;

	@Test
	void testLoadDiscussions() {
		// given
		UserAccount userAccount = defaultUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

		LoadDiscussionsOutputResult expectedResult = defaultLoadDiscussionsOutputResult();
		given(loadDiscussionsPort.loadDiscussions(any(Long.class), any(Boolean.class), any(Integer.class),
				any(Integer.class), any(FilterCriterion.class), any(SortCriterion.class))).willReturn(expectedResult);

		// when

		LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();
		LoadDiscussionsResult loadDiscussionsResult = discussionsService.loadDiscussions(commandBuilder.build());

		// then

		assertEquals(expectedResult.getPageSize(), loadDiscussionsResult.getPageSize());
		assertEquals(expectedResult.getPageNumber(), loadDiscussionsResult.getPageNumber());
		assertEquals(expectedResult.getTotalElements(), loadDiscussionsResult.getTotalElements());
		assertEquals(expectedResult.getTotalPages(), loadDiscussionsResult.getTotalPages());
		assertEquals(expectedResult.getContent().size(), loadDiscussionsResult.getContent().size());

		for (int i = 0; i < loadDiscussionsResult.getContent().size(); i++) {
			assertEquals(expectedResult.getContent().get(i).getId(), loadDiscussionsResult.getContent().get(i).getId());
			assertEquals(expectedResult.getContent().get(i).getActive(),
					loadDiscussionsResult.getContent().get(i).getActive());
			assertEquals(expectedResult.getContent().get(i).getInterlocutor(),
					loadDiscussionsResult.getContent().get(i).getInterlocutor());
			assertEquals(expectedResult.getContent().get(i).getDateTime(),
					loadDiscussionsResult.getContent().get(i).getDateTime().atZone(userZoneId).toInstant());

			for (int j = 0; j < expectedResult.getContent().get(i).getMessages().size(); j++) {
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getId(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getId());
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getAuthorEmail(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getAuthorEmail());
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getAuthorMobileNumber(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getAuthorMobileNumber());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getContent(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getContent());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getRead(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getRead());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getDateTime(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getDateTime().atZone(userZoneId)
								.toInstant());

			}
		}

	}

	@Test
	void testLoadDiscussionsWithNullFilterCriterion() {
		// given
		UserAccount userAccount = defaultUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		doReturn(userZoneId).when(dateTimeHelper).findUserZoneId(any(String.class));

		LoadDiscussionsOutputResult expectedResult = defaultLoadDiscussionsOutputResult();
		given(loadDiscussionsPort.loadDiscussions(any(Long.class), any(Boolean.class), any(Integer.class),
				any(Integer.class), any(SortCriterion.class))).willReturn(expectedResult);

		// when

		LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();
		LoadDiscussionsResult loadDiscussionsResult = discussionsService
				.loadDiscussions(commandBuilder.filteringCriterion(null).build());

		// then

		assertEquals(expectedResult.getPageSize(), loadDiscussionsResult.getPageSize());
		assertEquals(expectedResult.getPageNumber(), loadDiscussionsResult.getPageNumber());
		assertEquals(expectedResult.getTotalElements(), loadDiscussionsResult.getTotalElements());
		assertEquals(expectedResult.getTotalPages(), loadDiscussionsResult.getTotalPages());
		assertEquals(expectedResult.getContent().size(), loadDiscussionsResult.getContent().size());

		for (int i = 0; i < loadDiscussionsResult.getContent().size(); i++) {
			assertEquals(expectedResult.getContent().get(i).getId(), loadDiscussionsResult.getContent().get(i).getId());
			assertEquals(expectedResult.getContent().get(i).getActive(),
					loadDiscussionsResult.getContent().get(i).getActive());
			assertEquals(expectedResult.getContent().get(i).getInterlocutor(),
					loadDiscussionsResult.getContent().get(i).getInterlocutor());
			assertEquals(expectedResult.getContent().get(i).getDateTime(),
					loadDiscussionsResult.getContent().get(i).getDateTime().atZone(userZoneId).toInstant());

			for (int j = 0; j < expectedResult.getContent().get(i).getMessages().size(); j++) {
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getId(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getId());
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getAuthorEmail(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getAuthorEmail());
				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getAuthorMobileNumber(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getAuthorMobileNumber());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getContent(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getContent());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getRead(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getRead());

				assertEquals(expectedResult.getContent().get(i).getMessages().get(j).getDateTime(),
						loadDiscussionsResult.getContent().get(i).getMessages().get(j).getDateTime().atZone(userZoneId)
								.toInstant());

			}
		}

	}

	@Test
	void givenBadUsername_WhenLoadDiscussions_ThenThrowUserAccountNotFoundException() {
		// given
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.empty());

		LoadDiscussionsCommandBuilder commandBuilder = defaultLoadDiscussionsCommandBuilder();

		// when
		// then

		assertThrows(UserAccountNotFoundException.class,
				() -> discussionsService.loadDiscussions(commandBuilder.build()));

	}
}
