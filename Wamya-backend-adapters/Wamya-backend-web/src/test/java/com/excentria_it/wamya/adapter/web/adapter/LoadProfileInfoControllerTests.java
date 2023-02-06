package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.LoadProfileInfoUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {LoadProfileInfoController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = LoadProfileInfoController.class)
public class LoadProfileInfoControllerTests {
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
    private LoadProfileInfoUseCase loadProfileInfoUseCase;

    @Test
    void givenClientRoles_whenReadProfileInfo_thenReturnProfileInfoDto() throws Exception {
        // given
        given(loadProfileInfoUseCase.loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR")).willReturn(UserProfileTestData.defaultProfileInfoDto());

        // when
        mvc.perform(get("/profiles/me/info").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .queryParam("lang", "fr_FR"))
                .andExpect(status().isOk()).andExpect(responseBody().containsObjectAsJson(UserProfileTestData.defaultProfileInfoDto(), ProfileInfoDto.class));
        // then

        then(loadProfileInfoUseCase).should(times(1)).loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR");

    }

    @Test
    void givenTransporterRoles_whenReadProfileInfo_thenReturnProfileInfoDto() throws Exception {
        // given
        given(loadProfileInfoUseCase.loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR")).willReturn(UserProfileTestData.defaultProfileInfoDto());

        // when
        mvc.perform(get("/profiles/me/info").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .queryParam("lang", "fr_FR"))
                .andExpect(status().isOk()).andExpect(responseBody().containsObjectAsJson(UserProfileTestData.defaultProfileInfoDto(), ProfileInfoDto.class));
        // then

        then(loadProfileInfoUseCase).should(times(1)).loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR");

    }
}
