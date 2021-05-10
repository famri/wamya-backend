package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
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
import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForTransporterUseCase;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AutoCompleteDepartmentController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = AutoCompleteDepartmentController.class)
public class AutoCompleteDepartmentControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AutoCompletePlaceForTransporterUseCase autoCompleteDepartmentUseCase;

	@Test
	void testAutoCompleteDepartment() throws Exception {
		List<AutoCompleteDepartmentDto> departments = defaultAutoCompleteDepartmentsDtos();
		List<AutoCompletePlaceDto> autoCompletePlaceDtos = departments.stream()
				.map(d -> AutoCompletePlaceMapper.mapDepartmentToPlace(d)).collect(Collectors.toList());

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(autoCompletePlaceDtos.size(),
				autoCompletePlaceDtos);
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class))).willReturn(autoCompletePlaceDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
				eq("fr_FR"));
	}

	@Test
	void testAutoCompleteDepartmentEmptyResult() throws Exception {

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class))).willReturn(Collections.<AutoCompletePlaceDto>emptyList());
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
				eq("fr_FR"));
	}

	@Test
	void testAutoCompleteDepartmentNullResult() throws Exception {

		AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class))).willReturn(null);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
						.param("limit", "5"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
				eq("fr_FR"));
	}

	@Test
	void testAutoCompleteDepartmentWithInputSizeLessThan3AndEmptyCountryCode() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "be").param("country", "TN"))
				.andExpect(status().isBadRequest()).andExpect(responseBody().containsApiErrors(
						List.of("autoCompleteDepartment.input: la taille doit être comprise entre 3 et 2147483647")));

		// then

		then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}

	@Test
	void testAutoCompleteDepartmentWithCountryCodeEmpty() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "")
						.param("limit", "5"))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("autoCompleteDepartment.countryCode: ne doit pas être vide")));

		// then

		then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}

	@Test
	void testAutoCompleteDepartmentWithLimitZeor() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
						.param("limit", "0"))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("autoCompleteDepartment.limit: doit être supérieur ou égal à 1")));

		// then

		then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
				any(Integer.class), any(String.class));
	}
}
