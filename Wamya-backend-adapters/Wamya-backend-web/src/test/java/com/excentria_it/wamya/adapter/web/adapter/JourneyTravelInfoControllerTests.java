package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.LoadJourneyTravelInfoUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;
import com.excentria_it.wamya.test.data.common.TestConstants;


@ActiveProfiles(profiles = { "web-local" })
@Import(value = { JourneyTravelInfoController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = JourneyTravelInfoController.class)
public class JourneyTravelInfoControllerTests {
	@MockBean
	private LoadJourneyTravelInfoUseCase loadJourneyTravelInfoUseCase;

	@Autowired
	private MockMvcSupport api;

	@Test
	void testLoadJourneyTravelInfo() throws Exception {
		// given
		Optional<JourneyTravelInfo> journeyTravelInfo = Optional.of(defaultJourneyTravelInfo());
		given(loadJourneyTravelInfoUseCase.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(journeyTravelInfo);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/travel-info").param("departure-id", "1").param("departure-type", "department")
						.param("arrival-id", "2").param("arrival-type", "department"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(journeyTravelInfo.get(), JourneyTravelInfo.class));
		// then

		then(loadJourneyTravelInfoUseCase).should(times(1)).loadTravelInfo(eq(1L), eq(PlaceType.DEPARTMENT), eq(2L),
				eq(PlaceType.DEPARTMENT));
	}

	@Test
	void givenBadDepartureType_WhenLoadJourneyTravelInfo_ThenBadRequest() throws Exception {
		// given

		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/travel-info").param("departure-id", "1").param("departure-type", "city")
						.param("arrival-id", "2").param("arrival-type", "department"))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("Invalid place type: city")));
		// then

		then(loadJourneyTravelInfoUseCase).should(never()).loadTravelInfo(any(Long.class), any(PlaceType.class),
				any(Long.class), any(PlaceType.class));
	}

	@Test
	void givenBadArrivalType_WhenLoadJourneyTravelInfo_ThenBadRequest() throws Exception {
		// given

		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/travel-info").param("departure-id", "1").param("departure-type", "department")
						.param("arrival-id", "2").param("arrival-type", "city"))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("Invalid place type: city")));
		// then

		then(loadJourneyTravelInfoUseCase).should(never()).loadTravelInfo(any(Long.class), any(PlaceType.class),
				any(Long.class), any(PlaceType.class));
	}
}
