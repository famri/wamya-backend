package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase;
import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehiculeCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { VehiculesController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = VehiculesController.class)
public class VehiculesControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AddVehiculeUseCase addVehiculeUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInputAndTransporterEmail_WhenAddVehicule_ThenSucceed() throws Exception {

		// given
		AddVehiculeCommand command = defaultAddVehiculeCommandBuilder().build();

		String addVehiculeCommandJson = objectMapper.writeValueAsString(command);

		AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();

		given(addVehiculeUseCase.addVehicule(any(AddVehiculeCommand.class), any(String.class), any(String.class)))
				.willReturn(addVehiculeDto);
		// when // then
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_vehicule:write"))
				.perform(post("/users/me/vehicules").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(addVehiculeCommandJson))
				.andExpect(status().isCreated())
				.andExpect(responseBody().containsObjectAsJson(addVehiculeDto, AddVehiculeDto.class));

	}
}
