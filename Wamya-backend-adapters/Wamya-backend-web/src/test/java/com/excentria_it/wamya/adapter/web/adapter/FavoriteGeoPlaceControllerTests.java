package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase.CreateFavoriteGeoPlaceCommand;
import com.excentria_it.wamya.application.port.in.LoadFavoriteGeoPlacesUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { FavoriteGeoPlaceController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = FavoriteGeoPlaceController.class)
public class FavoriteGeoPlaceControllerTests {

	@Autowired
	private MockMvcSupport api;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CreateFavoriteGeoPlaceUseCase createFavoriteGeoPlaceUseCase;

	@MockBean
	private LoadFavoriteGeoPlacesUseCase loadFavoriteGeoPlaceUseCase;

	@Test
	void givenValidInput_WhenCreateFavoriteGeoPlace_ThenReturnGeoPlaceDto() throws Exception {
		CreateFavoriteGeoPlaceCommand command = defaultCreateFavoriteGeoPlaceCommandBuilder().build();

		GeoPlaceDto result = defaultGeoPlaceDto();
		// given
		given(createFavoriteGeoPlaceUseCase.createFavoriteGeoPlace(any(String.class), any(BigDecimal.class),
				any(BigDecimal.class), any(String.class), any(String.class))).willReturn(result);

		String createFavoriteGeoPlaceCommandJson = objectMapper.writeValueAsString(command);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(post("/geo-places").param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createFavoriteGeoPlaceCommandJson))
				.andExpect(status().isCreated())
				.andExpect(responseBody().containsObjectAsJson(result, GeoPlaceDto.class));

		// then
		then(createFavoriteGeoPlaceUseCase).should(times(1)).createFavoriteGeoPlace(eq(command.getName()),
				eq(command.getLatitude()), eq(command.getLongitude()), eq(TestConstants.DEFAULT_EMAIL), eq("fr_FR"));
	}

	@Test
	void givenValidInputAndBadAuthority_WhenCreateFavoriteGeoPlace_ThenReturnUnauthorized() throws Exception {
		CreateFavoriteGeoPlaceCommand command = defaultCreateFavoriteGeoPlaceCommandBuilder().build();

		GeoPlaceDto result = defaultGeoPlaceDto();
		// given
		given(createFavoriteGeoPlaceUseCase.createFavoriteGeoPlace(any(String.class), any(BigDecimal.class),
				any(BigDecimal.class), any(String.class), any(String.class))).willReturn(result);

		String createFavoriteGeoPlaceCommandJson = objectMapper.writeValueAsString(command);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:read"))
				.perform(post("/geo-places").param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createFavoriteGeoPlaceCommandJson))
				.andExpect(status().isForbidden());

		// then
		then(createFavoriteGeoPlaceUseCase).should(never()).createFavoriteGeoPlace(any(String.class),
				any(BigDecimal.class), any(BigDecimal.class), any(String.class), any(String.class));
	}

	@Test
	void givenValidInput_WhenLoadFavoriteGeoPlaces_ThenReturnUserFavoriteGeoPlaces() throws Exception {

		UserFavoriteGeoPlaces userFavoriteGeoPlaces = defaultUserFavoriteGeoPlaces();
		// given
		given(loadFavoriteGeoPlaceUseCase.loadFavoriteGeoPlaces(any(String.class), any(String.class)))
				.willReturn(userFavoriteGeoPlaces);

		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write")).perform(get("/geo-places").param("lang", "fr_FR"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(userFavoriteGeoPlaces, UserFavoriteGeoPlaces.class));

		// then
		then(loadFavoriteGeoPlaceUseCase).should(times(1)).loadFavoriteGeoPlaces(eq(TestConstants.DEFAULT_EMAIL),
				eq("fr_FR"));
	}

	@Test
	void givenValidInputAndBadAuthority_WhenLoadFavoriteGeoPlaces_ThenReturnForbidden() throws Exception {

		// given

		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:read")).perform(get("/geo-places").param("lang", "fr_FR"))
				.andExpect(status().isForbidden());

		// then
		then(loadFavoriteGeoPlaceUseCase).should(never()).loadFavoriteGeoPlaces(any(String.class), any(String.class));
	}
}
