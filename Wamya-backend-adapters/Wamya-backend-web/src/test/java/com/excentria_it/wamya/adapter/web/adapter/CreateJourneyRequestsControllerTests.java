package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {CreateJourneyRequestsController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = CreateJourneyRequestsController.class)
public class CreateJourneyRequestsControllerTests {


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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateJourneyRequestUseCase createJourneyRequestUseCase;

    @Test
    void givenValidInput_WhenCreateJourneyRequest_ThenReturnCreatedJourneyRequest() throws Exception {
        CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
                .build();
        CreateJourneyRequestDto journeyRequest = JourneyRequestTestData.defaultCreateJourneyRequestDto();

        given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
                any(String.class), any(String.class))).willReturn(journeyRequest);

        String createJourneyRequestJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/journey-requests").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(createJourneyRequestJson))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(journeyRequest, CreateJourneyRequestDto.class));

        ArgumentCaptor<CreateJourneyRequestCommand> captor = ArgumentCaptor.forClass(CreateJourneyRequestCommand.class);

        then(createJourneyRequestUseCase).should(times(1)).createJourneyRequest(captor.capture(),
                eq("user"), eq("en_US"));

        assertThat(captor.getValue().getDateTime()).isEqualTo(command.getDateTime());
        assertThat(captor.getValue().getDeparturePlaceId()).isEqualTo(command.getDeparturePlaceId());
        assertThat(captor.getValue().getDeparturePlaceType()).isEqualTo(command.getDeparturePlaceType());

        assertThat(captor.getValue().getArrivalPlaceId()).isEqualTo(command.getArrivalPlaceId());

        assertThat(captor.getValue().getArrivalPlaceType()).isEqualTo(command.getArrivalPlaceType());

        assertThat(captor.getValue().getEngineTypeId()).isEqualTo(command.getEngineTypeId());

        assertThat(captor.getValue().getWorkers()).isEqualTo(command.getWorkers());
        assertThat(captor.getValue().getDescription()).isEqualTo(command.getDescription());

    }

    @Test
    void givenValidInputAndBadAuthority_WhenCreateJourneyRequest_ThenReturnForbidden() throws Exception {
        // given
        CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
                .build();
        CreateJourneyRequestDto journeyRequest = JourneyRequestTestData.defaultCreateJourneyRequestDto();

        given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
                any(String.class), any(String.class))).willReturn(journeyRequest);

        String createJourneyRequestJson = objectMapper.writeValueAsString(command);

        // when
        mvc.perform(post("/journey-requests").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(createJourneyRequestJson))
                .andExpect(status().isForbidden()).andReturn();

        // then
        then(createJourneyRequestUseCase).should(never()).createJourneyRequest(eq(command),
                eq(TestConstants.DEFAULT_EMAIL), eq("en_US"));

    }

}
