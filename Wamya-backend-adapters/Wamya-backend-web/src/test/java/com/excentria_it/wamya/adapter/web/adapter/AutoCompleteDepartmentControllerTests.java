package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForTransporterUseCase;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
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
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.defaultAutoCompleteDepartmentsDtos;
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
@Import(value = {AutoCompleteDepartmentController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = AutoCompleteDepartmentController.class)
public class AutoCompleteDepartmentControllerTests {
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
    private AutoCompletePlaceForTransporterUseCase autoCompleteDepartmentUseCase;

    @Test
    void testAutoCompleteDepartment() throws Exception {
        List<AutoCompleteDepartmentDto> departments = defaultAutoCompleteDepartmentsDtos();
        List<AutoCompletePlaceDto> autoCompletePlaceDtos = departments.stream()
                .map(d -> AutoCompletePlaceMapper.mapDepartmentToPlace(d)).collect(Collectors.toList());

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(autoCompletePlaceDtos.size(),
                autoCompletePlaceDtos);
        // given
        given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class))).willReturn(autoCompletePlaceDtos);
        // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));
        // then

        then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
                eq("fr_FR"));
    }

    @Test
    void testAutoCompleteDepartmentEmptyResult() throws Exception {

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
        // given
        given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class))).willReturn(Collections.<AutoCompletePlaceDto>emptyList());
        // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));
        // then

        then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
                eq("fr_FR"));
    }

    @Test
    void testAutoCompleteDepartmentNullResult() throws Exception {

        AutoCompletePlaceResult result = new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
        // given
        given(autoCompleteDepartmentUseCase.autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class))).willReturn(null);
        // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, AutoCompletePlaceResult.class));
        // then

        then(autoCompleteDepartmentUseCase).should(times(1)).autoCompleteDepartment(eq("ben"), eq("TN"), eq(5),
                eq("fr_FR"));
    }

    @Test
    void testAutoCompleteDepartmentWithInputSizeLessThan3AndEmptyCountryCode() throws Exception {

        // given // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "be").param("country", "TN")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(
                        List.of("autoCompleteDepartment.input: la taille doit être comprise entre 3 et 2147483647")));
        // then

        then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }

    @Test
    void testAutoCompleteDepartmentWithCountryCodeEmpty() throws Exception {

        // given // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "")
                        .param("limit", "5").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest()).andExpect(responseBody()
                        .containsApiErrors(List.of("autoCompleteDepartment.countryCode: ne doit pas être vide")));
        // then

        then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }

    @Test
    void testAutoCompleteDepartmentWithLimitZeor() throws Exception {

        // given // when
        mvc.perform(get("/departments").param("lang", "fr_FR").param("input", "ben").param("country", "TN")
                        .param("limit", "0").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isBadRequest()).andExpect(responseBody()
                        .containsApiErrors(List.of("autoCompleteDepartment.limit: doit être supérieur ou égal à 1")));

        // then

        then(autoCompleteDepartmentUseCase).should(never()).autoCompleteDepartment(any(String.class), any(String.class),
                any(Integer.class), any(String.class));
    }
}
