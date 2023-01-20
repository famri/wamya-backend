package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForClientUseCase;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceResult;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.LocalityTestData.defaultAutoCompleteLocalitiesDtos;
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
@Import(value = {AutoCompletePlaceController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = AutoCompletePlaceController.class)
public class AutoCompletePlaceControllerTests {

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
    private AutoCompletePlaceForClientUseCase autoCompleteLocalityUseCase;

    @Test
    void testAutoCompleteLocality() throws Exception {

        List<AutoCompleteLocalityDto> autoCompleteLocalityDtos = defaultAutoCompleteLocalitiesDtos();
        List<AutoCompletePlaceDto> AutoCompletePlaceDtos = autoCompleteLocalityDtos.stream()
                .map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(AutoCompletePlaceDtos.size(),
                AutoCompletePlaceDtos);
        // given
        given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
                any(String.class))).willReturn(AutoCompletePlaceDtos);
        // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

        // then

        then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
    }

    @Test
    void testAutoCompleteLocalityEmptyResult() throws Exception {

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
        // given
        given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
                any(String.class))).willReturn(Collections.<AutoCompletePlaceDto>emptyList());
        // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));

        // then

        then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
    }

    @Test
    void testAutoCompleteLocalityNullResult() throws Exception {

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
        // given
        given(autoCompleteLocalityUseCase.autoCompletePlace(any(String.class), any(String.class), any(Integer.class),
                any(String.class))).willReturn(null);
        // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "thy").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));
        // then

        then(autoCompleteLocalityUseCase).should(times(1)).autoCompletePlace(eq("thy"), eq("TN"), eq(5), eq("fr_FR"));
    }

    @Test
    void testAutoCompleteLocalityWithInputSizeLessThan3AndEmptyCountryCode() throws Exception {

        // given // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "be").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest()).andExpect(responseBody().containsApiErrors(
                        List.of("autoCompletePlace.input: la taille doit être comprise entre 3 et 2147483647")));
        // then

        then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }

    @Test
    void testAutoCompleteLocalityWithCountryCodeEmpty() throws Exception {

        // given // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "ben").param("country", "")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest()).andExpect(responseBody()
                        .containsApiErrors(List.of("autoCompletePlace.countryCode: ne doit pas être vide")));
        // then

        then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }

    @Test
    void testAutoCompleteLocalityWithLimitZero() throws Exception {

        // given // when
        mvc.perform(get("/places").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
                        .param("limit", "0").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest()).andExpect(responseBody()
                        .containsApiErrors(List.of("autoCompletePlace.limit: doit être supérieur ou égal à 1")));
        // then

        then(autoCompleteLocalityUseCase).should(never()).autoCompletePlace(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }
}
