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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.AutoCompleteDepartmentUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AutoCompleteDepartmentController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = AutoCompleteDepartmentController.class)
public class AutoCompleteDepartmentControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AutoCompleteDepartmentUseCase autoCompleteDepartmentUseCase;

	@Test
	void testAutoCompleteDepartment() throws Exception {

		List<AutoCompleteDepartmentsDto> autoCompleteDepartmentsDtos = defaultAutoCompleteDepartmentsDtos();
		AutoCompleteDepartmentsResult result = new AutoCompleteDepartmentsResult(autoCompleteDepartmentsDtos.size(),
				autoCompleteDepartmentsDtos);
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(String.class))).willReturn(autoCompleteDepartmentsDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "be").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteDepartmentsResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("be"), eq("TN"), eq("fr_FR"));
	}

	@Test
	void testAutoCompleteDepartmentEmptyResult() throws Exception {

		AutoCompleteDepartmentsResult result = new AutoCompleteDepartmentsResult(0,
				Collections.<AutoCompleteDepartmentsDto>emptyList());
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(String.class))).willReturn(Collections.<AutoCompleteDepartmentsDto>emptyList());
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "be").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteDepartmentsResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("be"), eq("TN"), eq("fr_FR"));
	}
	
	@Test
	void testAutoCompleteDepartmentNullResult() throws Exception {

		AutoCompleteDepartmentsResult result = new AutoCompleteDepartmentsResult(0,
				Collections.<AutoCompleteDepartmentsDto>emptyList());
		// given
		given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
				any(String.class))).willReturn(null);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(get("/departments").param("lang", "fr_FR").param("input", "be").param("country", "TN"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, AutoCompleteDepartmentsResult.class));

		// then

		then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("be"), eq("TN"), eq("fr_FR"));
	}

}
