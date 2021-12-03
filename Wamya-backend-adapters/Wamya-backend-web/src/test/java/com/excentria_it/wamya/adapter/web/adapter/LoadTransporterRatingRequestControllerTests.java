package com.excentria_it.wamya.adapter.web.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadTransporterRatingDetailsUseCase;
import com.excentria_it.wamya.application.port.in.LoadTransporterRatingDetailsUseCase.LoadTransporterRatingRequestCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadTransporterRatingRequestController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadTransporterRatingRequestController.class)
public class LoadTransporterRatingRequestControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoadTransporterRatingDetailsUseCase loadTransporterRatingDetailsUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidHashAndUserId_WhenLoadTransporterRatingDetails_ThenReturnTransporterRatingDetailsDto()
			throws Exception {

		// given
		LoadTransporterRatingRequestCommand command = LoadTransporterRatingRequestCommand.builder().hash("SOME_HASH")
				.userId(1L).build();

		TransporterRatingRequestRecordDto trdDto = TransporterRatingRequestTestData.defaultTransporterRatingRequestRecordDto();

		given(loadTransporterRatingDetailsUseCase
				.loadTransporterRatingDetails(any(LoadTransporterRatingRequestCommand.class), any(String.class)))
						.willReturn(trdDto);

		ArgumentCaptor<LoadTransporterRatingRequestCommand> commandCaptor = ArgumentCaptor
				.forClass(LoadTransporterRatingRequestCommand.class);
		// when

		MvcResult mvcResult = mockMvc
				.perform(get("/rating-details").param("h", command.getHash())
						.param("uid", command.getUserId().toString()).param("lang", "en_US"))
				.andExpect(status().isOk()).andReturn();

		TransporterRatingRequestRecordDto response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				TransporterRatingRequestRecordDto.class);
		// then

		then(loadTransporterRatingDetailsUseCase).should(times(1)).loadTransporterRatingDetails(commandCaptor.capture(),
				eq("en_US"));

		assertThat(commandCaptor.getValue().getHash()).isEqualTo("SOME_HASH");
		assertThat(commandCaptor.getValue().getUserId()).isEqualTo(1L);

		assertEquals(trdDto, response);

	}

}
