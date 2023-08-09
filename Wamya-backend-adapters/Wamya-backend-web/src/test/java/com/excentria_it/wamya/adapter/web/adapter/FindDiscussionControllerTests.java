package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.test.data.common.OAuthId;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.defaultLoadDiscussionsDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {FindDiscussionController.class, RestApiExceptionHandler.class,
        ValidationHelper.class})

@WebMvcTest(controllers = FindDiscussionController.class)
public class FindDiscussionControllerTests {
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
    private FindDiscussionUseCase findDiscussionUseCase;

    @Test
    void givenValidInput_WhenFindDiscussionByClientIdAndTransporterId_ThenReturnLoadDiscussionsDto() throws Exception {

        LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
        given(findDiscussionUseCase
                .findDiscussionByClientIdAndTransporterId(any(FindDiscussionByClientIdAndTransporterIdCommand.class)))
                .willReturn(loadDiscussionsDto);

        mvc.perform(get("/users/me/discussions").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("clientId", OAuthId.CLIENT1_UUID).param("transporterId", OAuthId.TRANSPORTER1_UUID))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

        ArgumentCaptor<FindDiscussionByClientIdAndTransporterIdCommand> captor = ArgumentCaptor
                .forClass(FindDiscussionByClientIdAndTransporterIdCommand.class);

        then(findDiscussionUseCase).should(times(1)).findDiscussionByClientIdAndTransporterId(captor.capture());

        assertThat(captor.getValue().getClientId()).isEqualTo(OAuthId.CLIENT1_UUID);
        assertThat(captor.getValue().getTransporterId()).isEqualTo(OAuthId.TRANSPORTER1_UUID);
        assertThat(captor.getValue().getSubject()).isEqualTo("user");

    }

    @Test
    void givenValidInput_WhenFindDiscussionById_ThenReturnLoadDiscussionsDto() throws Exception {

        LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
        given(findDiscussionUseCase.findDiscussionById(any(FindDiscussionByIdCommand.class)))
                .willReturn(loadDiscussionsDto);

        mvc.perform(get("/users/me/discussions/{id}", 1).with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("clientId", OAuthId.CLIENT1_UUID).param("transporterId", OAuthId.TRANSPORTER1_UUID))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

        ArgumentCaptor<FindDiscussionByIdCommand> captor = ArgumentCaptor.forClass(FindDiscussionByIdCommand.class);

        then(findDiscussionUseCase).should(times(1)).findDiscussionById(captor.capture());

        assertThat(captor.getValue().getDiscussionId()).isEqualTo(1L);

        assertThat(captor.getValue().getSubject()).isEqualTo("user");

    }

}
