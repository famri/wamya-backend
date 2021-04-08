package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
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
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase;
import com.excentria_it.wamya.application.port.in.LoadMessagesCommandUseCase.LoadMessagesCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadMessagesResult;
import com.excentria_it.wamya.test.data.common.TestConstants;
import static com.excentria_it.wamya.test.data.common.MessageTestData.*;
@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadDiscussionMessagesController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadDiscussionMessagesController.class)
public class LoadDiscussionMessagesControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadMessagesCommandUseCase loadMessagesCommandUseCase;

	@Test
	void givenValidInput_WhenLoadDiscussionMessages_ThenReturnDiscussionMessages() throws Exception {

		LoadMessagesResult result = defaultLoadMessagesResult();
		given(loadMessagesCommandUseCase.loadMessages(any(LoadMessagesCommand.class))).willReturn(result);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/discussions/{discussionId}/messages",1L)
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
