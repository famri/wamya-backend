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
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand.CreateDiscussionCommandBuilder;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { CreateDiscussionController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = CreateDiscussionController.class)
public class CreateDiscussionControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private CreateDiscussionUseCase createDiscussionUseCase;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenCreateDiscussion_ThenReturnCreatedDiscussion() throws Exception {

		// given
		CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
		CreateDiscussionCommand command = commandBuilder.build();

		String createDiscussionCommandJson = objectMapper.writeValueAsString(command);

		LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
		given(createDiscussionUseCase.createDiscussion(any(CreateDiscussionCommand.class), any(String.class)))
				.willReturn(loadDiscussionsDto);
		// When
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(post("/users/me/discussions").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createDiscussionCommandJson))
				.andExpect(status().isCreated())
				.andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

		ArgumentCaptor<CreateDiscussionCommand> captor = ArgumentCaptor.forClass(CreateDiscussionCommand.class);
		// then
		then(createDiscussionUseCase).should(times(1)).createDiscussion(captor.capture(),
				eq(TestConstants.DEFAULT_EMAIL));

		assertThat(captor.getValue().getClientId()).isEqualTo(command.getClientId());
		assertThat(captor.getValue().getTransporterId()).isEqualTo(command.getTransporterId());

	}
}
