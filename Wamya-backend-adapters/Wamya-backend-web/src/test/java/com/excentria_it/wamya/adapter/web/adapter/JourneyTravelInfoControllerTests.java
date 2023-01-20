package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.LoadJourneyTravelInfoUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.List;
import java.util.Optional;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.defaultJourneyTravelInfo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {JourneyTravelInfoController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = JourneyTravelInfoController.class)
public class JourneyTravelInfoControllerTests {
    @MockBean
    private LoadJourneyTravelInfoUseCase loadJourneyTravelInfoUseCase;

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

    @Test
    void testLoadJourneyTravelInfo() throws Exception {
        // given
        Optional<JourneyTravelInfo> journeyTravelInfo = Optional.of(defaultJourneyTravelInfo());
        given(loadJourneyTravelInfoUseCase.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
                any(PlaceType.class))).willReturn(journeyTravelInfo);
        // when
        mvc.perform(get("/travel-info").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("departure-id", "1").param("departure-type", "department")
                        .param("arrival-id", "2").param("arrival-type", "department"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(journeyTravelInfo.get(), JourneyTravelInfo.class));
        // then

        then(loadJourneyTravelInfoUseCase).should(times(1)).loadTravelInfo(eq(1L), eq(PlaceType.DEPARTMENT), eq(2L),
                eq(PlaceType.DEPARTMENT));
    }

    @Test
    void givenBadDepartureType_WhenLoadJourneyTravelInfo_ThenBadRequest() throws Exception {
        // given

        // when
        mvc.perform(get("/travel-info").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("departure-id", "1").param("departure-type", "city")
                        .param("arrival-id", "2").param("arrival-type", "department"))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(List.of("Invalid place type: city")));
        // then

        then(loadJourneyTravelInfoUseCase).should(never()).loadTravelInfo(any(Long.class), any(PlaceType.class),
                any(Long.class), any(PlaceType.class));
    }

    @Test
    void givenBadArrivalType_WhenLoadJourneyTravelInfo_ThenBadRequest() throws Exception {
        // given

        // when
        mvc.perform(get("/travel-info").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("departure-id", "1").param("departure-type", "department")
                        .param("arrival-id", "2").param("arrival-type", "city"))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(List.of("Invalid place type: city")));
        // then

        then(loadJourneyTravelInfoUseCase).should(never()).loadTravelInfo(any(Long.class), any(PlaceType.class),
                any(Long.class), any(PlaceType.class));
    }
}
