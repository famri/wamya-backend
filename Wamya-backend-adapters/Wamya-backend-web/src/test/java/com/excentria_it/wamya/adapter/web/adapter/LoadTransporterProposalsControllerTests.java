package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterProposals;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadTransporterProposalsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadTransporterProposalsController.class)
public class LoadTransporterProposalsControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadTransporterProposalsUseCase loadTransporterProposalsUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenLoadTransporterProposals_ThenReturnTransporterProposals() throws Exception {

		// given
		LoadTransporterProposalsCommand command = JourneyProposalTestData
				.defaultLoadTransporterProposalsCommandBuilder().build();

		TransporterProposals transporterProposals = JourneyProposalTestData.defaultTransporterProposals();

		given(loadTransporterProposalsUseCase.loadProposals(any(LoadTransporterProposalsCommand.class),
				any(String.class))).willReturn(transporterProposals);

		ArgumentCaptor<LoadTransporterProposalsCommand> commandCaptor = ArgumentCaptor
				.forClass(LoadTransporterProposalsCommand.class);
		// when

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_offer:write"))
				.perform(get("/users/me/proposals").param("lang", "en_US")
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString())
						.param("sort",
								command.getSortingCriterion().getField().toLowerCase().concat(",")
										.concat(command.getSortingCriterion().getDirection().toLowerCase()))
						.param("period", "m1").param("statuses",
								Arrays.stream(JourneyProposalStatusCode.values()).map(s -> s.toString())
										.collect(Collectors.toSet()).toArray(String[]::new)))

				.andExpect(status().isOk()).andReturn();

		TransporterProposals response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				TransporterProposals.class);
		// then

		then(loadTransporterProposalsUseCase).should(times(1)).loadProposals(commandCaptor.capture(), eq("en_US"));

		assertThat(commandCaptor.getValue().getTransporterUsername()).isEqualTo(command.getTransporterUsername());
		assertThat(commandCaptor.getValue().getPageNumber()).isEqualTo(command.getPageNumber());
		assertThat(commandCaptor.getValue().getPageSize()).isEqualTo(command.getPageSize());
		assertThat(commandCaptor.getValue().getSortingCriterion().getField())
				.isEqualTo(command.getSortingCriterion().getField());
		assertThat(commandCaptor.getValue().getSortingCriterion().getDirection())
				.isEqualTo(command.getSortingCriterion().getDirection());
		assertThat(commandCaptor.getValue().getStatusCodes()).containsAll(command.getStatusCodes());
		assertThat(commandCaptor.getValue().getStatusCodes().size()).isEqualTo(command.getStatusCodes().size());

		assertEquals(transporterProposals, response);

	}

}
