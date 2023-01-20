package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.TransporterVehicules;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.VehiculeTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {LoadTransporterVehiculesController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadTransporterVehiculesController.class)
public class LoadTransporterVehiculesControllerTests {

    @Autowired
    private WebApplicationContext context;
    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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

        MvcResult mvcResult =
                mvc.perform(get("/users/me/vehicules").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("sort", "id,asc")).andExpect(status().isOk()).andReturn();

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
