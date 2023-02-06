package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase.CreateFavoriteGeoPlaceCommand;
import com.excentria_it.wamya.application.port.in.LoadFavoriteGeoPlacesUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.math.BigDecimal;
import java.util.Arrays;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.GeoPlaceTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {FavoriteGeoPlaceController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = FavoriteGeoPlaceController.class)
public class FavoriteGeoPlaceControllerTests {

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
    private CreateFavoriteGeoPlaceUseCase createFavoriteGeoPlaceUseCase;

    @MockBean
    private LoadFavoriteGeoPlacesUseCase loadFavoriteGeoPlaceUseCase;

    @Test
    void givenValidInput_WhenCreateFavoriteGeoPlace_ThenReturnGeoPlaceDto() throws Exception {
        CreateFavoriteGeoPlaceCommand command = defaultCreateFavoriteGeoPlaceCommandBuilder().build();

        GeoPlaceDto result = defaultGeoPlaceDto();
        // given
        given(createFavoriteGeoPlaceUseCase.createFavoriteGeoPlace(any(String.class), any(BigDecimal.class),
                any(BigDecimal.class), any(String.class), any(String.class))).willReturn(result);

        String createFavoriteGeoPlaceCommandJson = objectMapper.writeValueAsString(command);
        // when
        mvc.perform(post("/geo-places").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(createFavoriteGeoPlaceCommandJson))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(result, GeoPlaceDto.class));

        // then
        then(createFavoriteGeoPlaceUseCase).should(times(1)).createFavoriteGeoPlace(eq(command.getName()),
                eq(command.getLatitude()), eq(command.getLongitude()), eq("user"), eq("fr_FR"));
    }

    @Test
    void givenValidInputAndBadAuthority_WhenCreateFavoriteGeoPlace_ThenReturnUnauthorized() throws Exception {
        CreateFavoriteGeoPlaceCommand command = defaultCreateFavoriteGeoPlaceCommandBuilder().build();

        GeoPlaceDto result = defaultGeoPlaceDto();
        // given
        given(createFavoriteGeoPlaceUseCase.createFavoriteGeoPlace(any(String.class), any(BigDecimal.class),
                any(BigDecimal.class), any(String.class), any(String.class))).willReturn(result);

        String createFavoriteGeoPlaceCommandJson = objectMapper.writeValueAsString(command);

        // when
        mvc.perform(post("/geo-places").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(createFavoriteGeoPlaceCommandJson))
                .andExpect(status().isForbidden());

        // then
        then(createFavoriteGeoPlaceUseCase).should(never()).createFavoriteGeoPlace(any(String.class),
                any(BigDecimal.class), any(BigDecimal.class), any(String.class), any(String.class));
    }

    @Test
    void givenValidInput_WhenLoadFavoriteGeoPlaces_ThenReturnUserFavoriteGeoPlaces() throws Exception {

        UserFavoriteGeoPlaces userFavoriteGeoPlaces = defaultUserFavoriteGeoPlaces();
        // given
        given(loadFavoriteGeoPlaceUseCase.loadFavoriteGeoPlaces(any(String.class), any(String.class)))
                .willReturn(userFavoriteGeoPlaces);

        // when
        mvc.perform(get("/geo-places").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR"))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(userFavoriteGeoPlaces, UserFavoriteGeoPlaces.class));
        // then
        then(loadFavoriteGeoPlaceUseCase).should(times(1)).loadFavoriteGeoPlaces(eq("user"),
                eq("fr_FR"));
    }

    @Test
    void givenValidInputAndBadAuthority_WhenLoadFavoriteGeoPlaces_ThenReturnForbidden() throws Exception {

        // given

        // when
        mvc.perform(get("/geo-places").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).param("lang", "fr_FR"))
                .andExpect(status().isForbidden());

        // then
        then(loadFavoriteGeoPlaceUseCase).should(never()).loadFavoriteGeoPlaces(any(String.class), any(String.class));
    }
}
