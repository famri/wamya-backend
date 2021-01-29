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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.AutoCompleteLocalityUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsResult;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AutoCompleteLocalityController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = AutoCompleteLocalityController.class)
public class AutoCompleteLocalityControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AutoCompleteLocalityUseCase autoCompleteLocalityUseCase;

	@Test
	void testAutoCompleteLocality() throws Exception {

		List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtos = defaultAutoCompleteLocalitiesDtos();
		AutoCompleteLocalitiesResult result = new AutoCompleteLocalitiesResult(autoCompleteLocalitiesDtos.size(),
				autoCompleteLocalitiesDtos);
		// given
		given(autoCompleteLocalityUseCase.autoCompleteLocality(any(String.class), any(String.class), any(String.class)))
				.willReturn(autoCompleteLocalitiesDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/localities").param("lang", "fr_FR").param("input", "thy").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteLocalitiesResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompleteLocality(eq("thy"), eq("TN"), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityEmptyResult() throws Exception {

		AutoCompleteLocalitiesResult result = new AutoCompleteLocalitiesResult(0,
				Collections.<AutoCompleteLocalitiesDto>emptyList());
		// given
		given(autoCompleteLocalityUseCase.autoCompleteLocality(any(String.class), any(String.class), any(String.class)))
				.willReturn(Collections.<AutoCompleteLocalitiesDto>emptyList());
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/localities").param("lang", "fr_FR").param("input", "thy").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteDepartmentsResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompleteLocality(eq("thy"), eq("TN"), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityNullResult() throws Exception {

		AutoCompleteLocalitiesResult result = new AutoCompleteLocalitiesResult(0,
				Collections.<AutoCompleteLocalitiesDto>emptyList());
		// given
		given(autoCompleteLocalityUseCase.autoCompleteLocality(any(String.class), any(String.class), any(String.class)))
				.willReturn(null);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/localities").param("lang", "fr_FR").param("input", "thy").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteDepartmentsResult.class));

		// then

		then(autoCompleteLocalityUseCase).should(times(1)).autoCompleteLocality(eq("thy"), eq("TN"), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteLocalityWithInputSizeLessThan3AndEmptyCountryCode() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/localities").param("lang", "fr_FR").param("input", "be").param("country", "TN"))
				.andExpect(status().isBadRequest()).andExpect(responseBody().containsApiErrors(
						List.of("autoCompleteLocality.input: la taille doit être comprise entre 3 et 2147483647")));

		// then

		then(autoCompleteLocalityUseCase).should(never()).autoCompleteLocality(any(String.class), any(String.class),
				any(String.class));
	}

	@Test
	void testAutoCompleteLocalityWithCountryCodeEmpty() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/localities").param("lang", "fr_FR").param("input", "ben").param("country", ""))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("autoCompleteLocality.countryCode: ne doit pas être vide")));

		// then

		then(autoCompleteLocalityUseCase).should(never()).autoCompleteLocality(any(String.class), any(String.class),
				any(String.class));
	}
}
