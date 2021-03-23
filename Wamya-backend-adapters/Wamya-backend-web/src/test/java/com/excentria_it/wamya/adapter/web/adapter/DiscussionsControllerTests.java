package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.*;
import static org.assertj.core.api.Assertions.*;
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

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { DiscussionsController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = DiscussionsController.class)
public class DiscussionsControllerTests {
	@Autowired
	private MockMvcSupport api;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private LoadDiscussionsUseCase loadDiscussionsUseCase;

	@Test
	void givenValidInput_WhenLoadDiscussions_ThenReturnLoadDiscussionsResult() throws Exception {

		LoadDiscussionsResult result = defaultLoadDiscussionsResult();
		given(loadDiscussionsUseCase.loadDiscussions(any(LoadDiscussionsCommand.class))).willReturn(result);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write")).perform(get("/discussions/me").param("filter", "active:true"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, LoadDiscussionsResult.class));

		ArgumentCaptor<LoadDiscussionsCommand> captor = ArgumentCaptor.forClass(LoadDiscussionsCommand.class);

		then(loadDiscussionsUseCase).should(times(1)).loadDiscussions(captor.capture());

		assertThat(captor.getValue().getFilteringCriterion()).isEqualTo(new FilterCriterion("active", "true"));
		assertThat(captor.getValue().getSortingCriterion()).isEqualTo(new SortCriterion("date-time", "desc"));
		assertThat(captor.getValue().getUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);
		assertThat(captor.getValue().getPageNumber()).isEqualTo(0);
		assertThat(captor.getValue().getPageSize()).isEqualTo(25);

	}
}
