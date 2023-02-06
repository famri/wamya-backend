package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
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
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.defaultLoadDiscussionsResult;
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
@Import(value = {LoadDiscussionsController.class, RestApiExceptionHandler.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadDiscussionsController.class)
public class LoadDiscussionsControllerTests {
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
    private LoadDiscussionsUseCase loadDiscussionsUseCase;

    @Test
    void givenFilterInputAndClientScope_WhenLoadDiscussions_ThenReturnLoadDiscussionsResult() throws Exception {

        LoadDiscussionsResult result = defaultLoadDiscussionsResult();
        given(loadDiscussionsUseCase.loadDiscussions(any(LoadDiscussionsCommand.class))).willReturn(result);


        mvc.perform(get("/users/me/discussions").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .param("filter", "active:true").param("sort", "date-time,desc")
                        .param("page", "0").param("size", "25"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadDiscussionsResult.class));

        ArgumentCaptor<LoadDiscussionsCommand> captor = ArgumentCaptor.forClass(LoadDiscussionsCommand.class);

        then(loadDiscussionsUseCase).should(times(1)).loadDiscussions(captor.capture());

        assertThat(captor.getValue().getFilteringCriterion()).isEqualTo(new FilterCriterion("active", "true"));
        assertThat(captor.getValue().getSortingCriterion()).isEqualTo(new SortCriterion("date-time", "desc"));
        assertThat(captor.getValue().getSubject()).isEqualTo(TestConstants.DEFAULT_EMAIL);
        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(25);

    }

    //

    @Test
    void givenFilterInputAndTransporterScope_WhenLoadDiscussions_ThenReturnLoadDiscussionsResult() throws Exception {

        LoadDiscussionsResult result = defaultLoadDiscussionsResult();
        given(loadDiscussionsUseCase.loadDiscussions(any(LoadDiscussionsCommand.class))).willReturn(result);


        mvc.perform(get("/users/me/discussions").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("filter", "active:true").param("sort", "date-time,desc")
                        .param("page", "0").param("size", "25"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadDiscussionsResult.class));

        ArgumentCaptor<LoadDiscussionsCommand> captor = ArgumentCaptor.forClass(LoadDiscussionsCommand.class);

        then(loadDiscussionsUseCase).should(times(1)).loadDiscussions(captor.capture());

        assertThat(captor.getValue().getFilteringCriterion()).isEqualTo(new FilterCriterion("active", "true"));
        assertThat(captor.getValue().getSortingCriterion()).isEqualTo(new SortCriterion("date-time", "desc"));
        assertThat(captor.getValue().getSubject()).isEqualTo(TestConstants.DEFAULT_EMAIL);
        assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
        assertThat(captor.getValue().getPageSize()).isEqualTo(25);

    }
}
