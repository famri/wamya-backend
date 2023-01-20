package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.LoadEngineTypesUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;
import com.excentria_it.wamya.domain.LoadEngineTypesResult;
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

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.defaultLoadEngineTypesDtos;
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
@Import(value = {EngineTypeController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = EngineTypeController.class)
public class EngineTypeControllerTests {
    @MockBean
    private LoadEngineTypesUseCase loadEngineTypesUseCase;

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
    void testLoadAllEngineTypes() throws Exception {

        // given
        List<LoadEngineTypesDto> loadEngineTypesDtos = defaultLoadEngineTypesDtos();
        LoadEngineTypesResult loadEngineTypesResult = new LoadEngineTypesResult(loadEngineTypesDtos.size(),
                loadEngineTypesDtos);
        given(loadEngineTypesUseCase.loadAllEngineTypes(any(String.class))).willReturn(loadEngineTypesDtos);

        // when
        mvc.perform(get("/engine-types").param("lang", "fr_FR").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadEngineTypesResult, LoadEngineTypesResult.class));
        // then

        then(loadEngineTypesUseCase).should(times(1)).loadAllEngineTypes(eq("fr_FR"));
    }

    @Test
    void testLoadAllEngineTypesWithEmptyResult() throws Exception {

        // given

        LoadEngineTypesResult loadEngineTypesResult = new LoadEngineTypesResult(0,
                Collections.<LoadEngineTypesDto>emptyList());
        given(loadEngineTypesUseCase.loadAllEngineTypes(any(String.class)))
                .willReturn(Collections.<LoadEngineTypesDto>emptyList());

        // when
        mvc.perform(get("/engine-types").param("lang", "fr_FR").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadEngineTypesResult, LoadEngineTypesResult.class));
        // then

        then(loadEngineTypesUseCase).should(times(1)).loadAllEngineTypes(eq("fr_FR"));
    }

    @Test
    void testLoadAllEngineTypesWithNullResult() throws Exception {

        // given

        LoadEngineTypesResult loadEngineTypesResult = new LoadEngineTypesResult(0,
                Collections.<LoadEngineTypesDto>emptyList());

        given(loadEngineTypesUseCase.loadAllEngineTypes(any(String.class))).willReturn(null);

        // when
        mvc.perform(get("/engine-types").param("lang", "fr_FR").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))))
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadEngineTypesResult, LoadEngineTypesResult.class));
        // then

        then(loadEngineTypesUseCase).should(times(1)).loadAllEngineTypes(eq("fr_FR"));
    }
}
