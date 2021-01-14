package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
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
import com.excentria_it.wamya.application.port.in.LoadEngineTypesUseCase;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;
import com.excentria_it.wamya.domain.LoadEngineTypesResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { EngineTypeController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = EngineTypeController.class)
public class EngineTypeControllerTests {
	@MockBean
	private LoadEngineTypesUseCase loadEngineTypesUseCase;

	@Autowired
	private MockMvcSupport api;

	@Test
	void testLoadAllEngineTypes() throws Exception {

		// given
		List<LoadEngineTypesDto> loadEngineTypesDtos = defaultLoadEngineTypesDtos();
		LoadEngineTypesResult loadEngineTypesResult = new LoadEngineTypesResult(loadEngineTypesDtos.size(),
				loadEngineTypesDtos);
		given(loadEngineTypesUseCase.loadAllEngineTypes(any(String.class))).willReturn(loadEngineTypesDtos);

		// when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write")).perform(get("/engine-types").param("lang", "fr_FR"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(loadEngineTypesResult, LoadEngineTypesResult.class));
		// then

		then(loadEngineTypesUseCase).should(times(1)).loadAllEngineTypes(eq("fr_FR"));
	}
}
