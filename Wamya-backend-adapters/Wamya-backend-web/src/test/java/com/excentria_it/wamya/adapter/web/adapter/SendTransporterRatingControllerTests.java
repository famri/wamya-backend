package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase;
import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase.SendTransporterRatingCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@Import(value = {SendTransporterRatingController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = SendTransporterRatingController.class)
public class SendTransporterRatingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SendTransporterRatingUseCase sendTransporterRatingUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidSendTransporterRatingCommand_WhenSendTransporterRatingRequest_ThenSaveTransporterRating()
            throws Exception {

        // given
        SendTransporterRatingCommand command = SendTransporterRatingCommand.builder().hash("SOME_HASH").uid(1L)
                .comment("SOME COMMENT").rating(5).build();

        String sendTransporterRatingCommandJson = objectMapper.writeValueAsString(command);
        // when

        mockMvc.perform(post("/ratings").param("lang", "en_US").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(sendTransporterRatingCommandJson)).andExpect(status().isCreated()).andReturn();

        // then

        then(sendTransporterRatingUseCase).should(times(1)).saveTransporterRating(eq(command), eq("en_US"));

    }
}
