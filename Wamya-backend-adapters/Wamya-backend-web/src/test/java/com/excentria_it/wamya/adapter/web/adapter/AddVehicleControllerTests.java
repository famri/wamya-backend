package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase;
import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase.AddVehicleCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.defaultAddVehiculeCommandBuilder;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.defaultAddVehiculeDtoBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebSecurityConfiguration.class)
@Import(value = {AddVehicleController.class, RestApiExceptionHandler.class, WebSecurityConfiguration.class})
@WebMvcTest(controllers = AddVehicleController.class)
public class AddVehicleControllerTests {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AddVehiculeUseCase addVehiculeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void givenValidInputAndTransporterEmail_WhenAddVehicle_ThenSucceed() throws Exception {

        // given
        AddVehicleCommand command = defaultAddVehiculeCommandBuilder().build();

        String addVehicleCommandJson = objectMapper.writeValueAsString(command);

        AddVehiculeDto addVehiculeDto = defaultAddVehiculeDtoBuilder().build();

        given(addVehiculeUseCase.addVehicle(any(AddVehicleCommand.class), any(String.class), any(String.class)))
                .willReturn(addVehiculeDto);

        // when // then
        mvc.perform(post("/users/me/vehicles").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(addVehicleCommandJson))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(addVehiculeDto, AddVehiculeDto.class));

    }

}
