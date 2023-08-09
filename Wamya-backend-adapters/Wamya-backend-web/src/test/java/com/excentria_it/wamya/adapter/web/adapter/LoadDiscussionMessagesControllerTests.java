package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase;
import com.excentria_it.wamya.application.port.in.LoadMessagesUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadMessagesResult;
import com.excentria_it.wamya.test.data.common.TestConstants;
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
import static com.excentria_it.wamya.test.data.common.MessageTestData.defaultLoadMessagesResult;
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
@Import(value = {LoadDiscussionMessagesController.class, RestApiExceptionHandler.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadDiscussionMessagesController.class)
public class LoadDiscussionMessagesControllerTests {

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
    private LoadMessagesUseCase loadMessagesCommandUseCase;

    @Test
    void givenValidInput_WhenLoadDiscussionMessages_ThenReturnDiscussionMessages() throws Exception {

        LoadMessagesResult result = defaultLoadMessagesResult();
        given(loadMessagesCommandUseCase.loadMessages(any(LoadMessagesCommand.class))).willReturn(result);


        mvc.perform(get("/users/me/discussions/{discussionId}/messages", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .param("page", "0").param("size", "25"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadMessagesResult.class));

        ArgumentCaptor<LoadMessagesCommand> captor = ArgumentCaptor.forClass(LoadMessagesCommand.class);

        then(loadMessagesCommandUseCase).should(times(1)).loadMessages(captor.capture());

        assertThat(captor.getValue().getSortingCriterion()).isEqualTo(new SortCriterion("date-time", "desc"));
        assertThat(captor.getValue().getUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);
        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(25);
    }

}
