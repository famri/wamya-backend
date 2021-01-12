package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static com.excentria_it.wamya.test.data.common.ModelTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.LoadConstructorsUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadModelsDto;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.core.type.TypeReference;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { ConstructorController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = ConstructorController.class)
public class ConstructorControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadConstructorsUseCase loadConstructorsUseCase;

	@Test
	void testLoadAllConstructors() throws Exception {

		List<LoadConstructorsDto> loadConstructorsDtos = defaultLoadConstructorsDtos();
		// given
		given(loadConstructorsUseCase.loadAllConstructors()).willReturn(loadConstructorsDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_vehicule:write")).perform(get("/constructors")).andExpect(status().isOk())
				.andExpect(responseBody().containsListOfObjectsAsJson(loadConstructorsDtos,
						new TypeReference<List<LoadConstructorsDto>>() {
						}));

		// then

		then(loadConstructorsUseCase).should(times(1)).loadAllConstructors();
	}

	@Test
	void testLoadConstructorModels() throws Exception {

		List<LoadModelsDto> loadModelsDtos = defaultLoadModelsDtos();
		// given
		given(loadConstructorsUseCase.loadConstructorModels(any(Long.class))).willReturn(loadModelsDtos);
		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_vehicule:write")).perform(get("/constructors/{constructorId}/models", 1L))
				.andExpect(status().isOk()).andExpect(responseBody().containsListOfObjectsAsJson(loadModelsDtos,
						new TypeReference<List<LoadModelsDto>>() {
						}));

		// then

		then(loadConstructorsUseCase).should(times(1)).loadConstructorModels(eq(1L));
	}

}
