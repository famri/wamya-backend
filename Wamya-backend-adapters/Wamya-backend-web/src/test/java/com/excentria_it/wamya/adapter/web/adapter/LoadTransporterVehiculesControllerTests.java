package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.TransporterVehicules;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.VehiculeTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadTransporterVehiculesController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadTransporterVehiculesController.class)
public class LoadTransporterVehiculesControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadVehiculesUseCase loadVehiculesUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testLoadTransporterVehicules() throws Exception {

		// given

		LoadVehiculesCommand command = VehiculeTestData.defaultLoadVehiculesCommandBuilder().build();

		TransporterVehicules transporterVehicules = VehiculeTestData.defaultTransporterVehicules();

		given(loadVehiculesUseCase.loadTransporterVehicules(any(LoadVehiculesCommand.class), any(String.class)))
				.willReturn(transporterVehicules);

		ArgumentCaptor<LoadVehiculesCommand> commandCaptor = ArgumentCaptor.forClass(LoadVehiculesCommand.class);
		// when

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_vehicule:read"))
				.perform(get("/users/me/vehicules").param("sort", "id,asc")).andExpect(status().isOk()).andReturn();

		TransporterVehicules response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				TransporterVehicules.class);
		// then

		then(loadVehiculesUseCase).should(times(1)).loadTransporterVehicules(commandCaptor.capture(), eq("en_US"));

		assertThat(commandCaptor.getValue().getTransporterUsername()).isEqualTo(command.getTransporterUsername());

		assertThat(commandCaptor.getValue().getSortingCriterion().getField())
				.isEqualTo(command.getSortingCriterion().getField());
		assertThat(commandCaptor.getValue().getSortingCriterion().getDirection())
				.isEqualTo(command.getSortingCriterion().getDirection());

		assertEquals(transporterVehicules, response);

	}

}
