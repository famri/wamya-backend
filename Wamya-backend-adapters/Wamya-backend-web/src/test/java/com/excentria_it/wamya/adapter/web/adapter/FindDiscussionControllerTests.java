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
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.FindDiscussionUseCase.FindDiscussionCommand;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { FindDiscussionController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })

@WebMvcTest(controllers = FindDiscussionController.class)
public class FindDiscussionControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private FindDiscussionUseCase findDiscussionUseCase;

	@Test
	void givenValidInput_WhenFindDiscussion_ThenReturnLoadDiscussionsDto() throws Exception {

		LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
		given(findDiscussionUseCase.findDiscussion(any(FindDiscussionCommand.class))).willReturn(loadDiscussionsDto);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/discussions").param("clientId", "1").param("transporterId", "2"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

		ArgumentCaptor<FindDiscussionCommand> captor = ArgumentCaptor.forClass(FindDiscussionCommand.class);

		then(findDiscussionUseCase).should(times(1)).findDiscussion(captor.capture());

		assertThat(captor.getValue().getClientId()).isEqualTo(1L);
		assertThat(captor.getValue().getTransporterId()).isEqualTo(2L);
		assertThat(captor.getValue().getUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);

	}

}
