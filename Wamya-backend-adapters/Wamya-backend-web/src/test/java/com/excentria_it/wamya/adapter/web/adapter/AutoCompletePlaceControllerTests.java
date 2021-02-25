package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForClientUseCase;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AutoCompletePlaceController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = AutoCompletePlaceController.class)
public class AutoCompletePlaceControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AutoCompletePlaceForClientUseCase autoCompleteLocalityUseCase;

	@Test
	void testAutoCompleteLocality() throws Exception {

		List<AutoCompleteLocalityDto> autoCompleteLocalityDtos = defaultAutoCompleteLocalitiesDtos();
		List<AutoCompletePlaceDto> AutoCompletePlaceDtos = autoCompleteLocalityDtos.stream()
				.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(AutoCompletePlaceDtos.size(),
				AutoCompletePlaceDtos);
		// given
		given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
				any(String.class))).willReturn(AutoCompletePlaceDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityEmptyResult() throws Exception {

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
		// given
		given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
				any(String.class))).willReturn(Collections.<AutoCompletePlaceDto>emptyList());
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityNullResult() throws Exception {

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
		// given
		given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
				any(String.class))).willReturn(null);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityWithInputSizeLessThan3AndEmptyCountryCode() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "be").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isBadRequest()).andExpect(responseBody().containsApiErrors(
						List.of("autoCompletePlace.input: la taille doit être comprise entre 3 et 2147483647")));

		// then

		then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}

	@Test
	void testAutoCompleteLocalityWithCountryCodeEmpty() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "ben").param("country", "").param("limit",
						"5"))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("autoCompletePlace.countryCode: ne doit pas être vide")));

		// then

		then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}

	@Test
	void testAutoCompleteLocalityWithLimitZero() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/places").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
						.param("limit", "0"))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("autoCompletePlace.limit: doit être supérieur ou égal à 1")));

		// then

		then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}
}
