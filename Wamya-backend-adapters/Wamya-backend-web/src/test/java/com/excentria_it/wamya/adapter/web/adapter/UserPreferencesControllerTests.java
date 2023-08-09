package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase;
import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase.SaveUserPreferenceCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
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
import java.util.List;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {UserPreferencesController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = UserPreferencesController.class)
public class UserPreferencesControllerTests {
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
    private SaveUserPreferenceUseCase saveUserPreferenceUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInputAndClientRole_WhenCreateUserPreference_ThenSucceed() throws Exception {

        SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("timezone", "Africa/Tunis");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/user-preferences").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isCreated());
        then(saveUserPreferenceUseCase).should(times(1)).saveUserPreference(command.getKey(), command.getValue(),
                TestConstants.DEFAULT_EMAIL);

    }

    @Test
    void givenValidInputAndTransporterRole_WhenCreateUserPreference_ThenSucceed() throws Exception {

        SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("timezone", "Africa/Tunis");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/user-preferences").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isCreated());
        then(saveUserPreferenceUseCase).should(times(1)).saveUserPreference(command.getKey(), command.getValue(),
                TestConstants.DEFAULT_EMAIL);

    }


    @Test
    void givenValidInputAndBadRole_WhenCreateUserPreference_ThenReturnForbidden() throws Exception {

        SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("timezone", "Africa/Tunis");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/user-preferences").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isForbidden());
        then(saveUserPreferenceUseCase).should(never()).saveUserPreference(any(String.class), any(String.class),
                any(String.class));

    }

    @Test
    void givenInvalidInputAndTransporterRole_WhenCreateUserPreference_ThenBadRequest() throws Exception {

        SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("bad key", "Africa/Tunis");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/user-preferences").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isBadRequest()).andExpect(responseBody().containsApiErrors(
                        List.of("key: Wrong value: 'bad key'. Valid values are: [timezone, locale].")));

        then(saveUserPreferenceUseCase).should(never()).saveUserPreference(any(String.class), any(String.class),
                any(String.class));

    }

}
