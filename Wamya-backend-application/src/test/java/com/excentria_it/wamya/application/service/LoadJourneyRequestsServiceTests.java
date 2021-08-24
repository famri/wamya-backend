package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

@ExtendWith(MockitoExtension.class)
public class LoadJourneyRequestsServiceTests {

	@Mock
	private LoadClientJourneyRequestsPort loadClientJourneyRequestsPort;

	@Mock
	private DateTimeHelper dateTimeHelper;

	@Spy
	@InjectMocks
	private LoadJourneyRequestsService loadJourneyRequestsService;

	@Test
	void givenLoadJourneyRequestsCommand_WhenLoadJourneyRequests_Then_Succeed() {
		// given
		LoadJourneyRequestsCommand command = defaultLoadJourneyRequestsCommandBuilder().build();
		ClientJourneyRequestsOutput clientJourneyRequests = defaultClientJourneyRequestsOutput();
		// when

		ArgumentCaptor<LoadClientJourneyRequestsCriteria> criteriaCaptor = ArgumentCaptor
				.forClass(LoadClientJourneyRequestsCriteria.class);

		given(loadClientJourneyRequestsPort.loadClientJourneyRequests(any(LoadClientJourneyRequestsCriteria.class)))
				.willReturn(clientJourneyRequests);
		given(dateTimeHelper.findUserZoneId(command.getClientUsername())).willReturn(ZoneId.of("Africa/Tunis"));
		// when
		loadJourneyRequestsService.loadJourneyRequests(command, "en_US");

		// then
		then(loadClientJourneyRequestsPort).should(times(1)).loadClientJourneyRequests(criteriaCaptor.capture());

		assertThat(criteriaCaptor.getValue().getClientUsername()).isEqualTo(command.getClientUsername());
		assertThat(criteriaCaptor.getValue().getPeriodCriterion()).isEqualTo(command.getPeriodCriterion());
		assertThat(criteriaCaptor.getValue().getSortingCriterion()).isEqualTo(command.getSortingCriterion());
		assertThat(criteriaCaptor.getValue().getPageNumber()).isEqualTo(command.getPageNumber());
		assertThat(criteriaCaptor.getValue().getPageSize()).isEqualTo(command.getPageSize());
		assertThat(criteriaCaptor.getValue().getLocale()).isEqualTo("en_US");
	}
}
