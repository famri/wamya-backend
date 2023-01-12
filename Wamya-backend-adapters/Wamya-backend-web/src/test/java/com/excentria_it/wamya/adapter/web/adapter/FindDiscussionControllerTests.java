package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByClientIdAndTransporterIdCommand;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionByIdCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.test.data.common.OAuthId;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.mockAuthentication;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.defaultLoadDiscussionsDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@Import(value = {FindDiscussionController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
        ValidationHelper.class})

@WebMvcTest(controllers = FindDiscussionController.class)
public class FindDiscussionControllerTests {
    @Autowired
    private MockMvcSupport api;

    @MockBean
    private FindDiscussionUseCase findDiscussionUseCase;

    @Test
    void givenValidInput_WhenFindDiscussionByClientIdAndTransporterId_ThenReturnLoadDiscussionsDto() throws Exception {

        LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
        given(findDiscussionUseCase
                .findDiscussionByClientIdAndTransporterId(any(FindDiscussionByClientIdAndTransporterIdCommand.class)))
                .willReturn(loadDiscussionsDto);

        api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
                        .authorities("SCOPE_journey:write"))
                .perform(get("/users/me/discussions").param("clientId", OAuthId.CLIENT1_UUID).param("transporterId", OAuthId.TRANSPORTER1_UUID))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

        ArgumentCaptor<FindDiscussionByClientIdAndTransporterIdCommand> captor = ArgumentCaptor
                .forClass(FindDiscussionByClientIdAndTransporterIdCommand.class);

        then(findDiscussionUseCase).should(times(1)).findDiscussionByClientIdAndTransporterId(captor.capture());

        assertThat(captor.getValue().getClientId()).isEqualTo(OAuthId.CLIENT1_UUID);
        assertThat(captor.getValue().getTransporterId()).isEqualTo(OAuthId.TRANSPORTER1_UUID);
        assertThat(captor.getValue().getUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);

    }

    @Test
    void givenValidInput_WhenFindDiscussionById_ThenReturnLoadDiscussionsDto() throws Exception {

        LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
        given(findDiscussionUseCase.findDiscussionById(any(FindDiscussionByIdCommand.class)))
                .willReturn(loadDiscussionsDto);

        api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
                        .authorities("SCOPE_journey:write")).perform(get("/users/me/discussions/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

        ArgumentCaptor<FindDiscussionByIdCommand> captor = ArgumentCaptor.forClass(FindDiscussionByIdCommand.class);

        then(findDiscussionUseCase).should(times(1)).findDiscussionById(captor.capture());

        assertThat(captor.getValue().getDiscussionId()).isEqualTo(1L);

        assertThat(captor.getValue().getUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);

    }

}
