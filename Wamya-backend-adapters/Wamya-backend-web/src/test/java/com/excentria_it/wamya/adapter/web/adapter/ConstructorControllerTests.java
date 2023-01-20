package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.LoadConstructorsUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadConstructorModelsResult;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadConstructorsResult;
import com.excentria_it.wamya.domain.LoadModelsDto;
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
import static com.excentria_it.wamya.test.data.common.ConstructorTestData.defaultLoadConstructorsDtos;
import static com.excentria_it.wamya.test.data.common.ModelTestData.defaultLoadModelsDtos;
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
@Import(value = {ConstructorController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = ConstructorController.class)
public class ConstructorControllerTests {

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
    private LoadConstructorsUseCase loadConstructorsUseCase;

    @Test
    void testLoadAllConstructors() throws Exception {

        List<LoadConstructorsDto> loadConstructorsDtos = defaultLoadConstructorsDtos();
        LoadConstructorsResult result = new LoadConstructorsResult(loadConstructorsDtos.size(), loadConstructorsDtos);
        // given
        given(loadConstructorsUseCase.loadAllConstructors()).willReturn(loadConstructorsDtos);
        // when
        mvc.perform(get("/constructors").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorsResult.class));

        // then

        then(loadConstructorsUseCase).should(times(1)).loadAllConstructors();
    }

    @Test
    void testLoadAllConstructorsWithEmptyResult() throws Exception {

        LoadConstructorsResult result = new LoadConstructorsResult(0, Collections.<LoadConstructorsDto>emptyList());
        // given
        given(loadConstructorsUseCase.loadAllConstructors()).willReturn(Collections.<LoadConstructorsDto>emptyList());
        // when
        mvc.perform(get("/constructors").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorsResult.class));

        // then

        then(loadConstructorsUseCase).should(times(1)).loadAllConstructors();
    }

    @Test
    void testLoadAllConstructorsWithNullResult() throws Exception {

        LoadConstructorsResult result = new LoadConstructorsResult(0, Collections.<LoadConstructorsDto>emptyList());
        // given
        given(loadConstructorsUseCase.loadAllConstructors()).willReturn(null);
        // when
        mvc.perform(get("/constructors").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorsResult.class));

        // then

        then(loadConstructorsUseCase).should(times(1)).loadAllConstructors();
    }

    @Test
    void testLoadConstructorModels() throws Exception {

        List<LoadModelsDto> loadModelsDtos = defaultLoadModelsDtos();
        LoadConstructorModelsResult result = new LoadConstructorModelsResult(loadModelsDtos.size(), loadModelsDtos);
        // given
        given(loadConstructorsUseCase.loadConstructorModels(any(Long.class))).willReturn(loadModelsDtos);

        // when
        mvc.perform(get("/constructors/{constructorId}/models", 1L).with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorModelsResult.class));

        // then
        then(loadConstructorsUseCase).should(times(1)).loadConstructorModels(eq(1L));
    }

    @Test
    void testLoadConstructorModelsWithEmptyResult() throws Exception {

        LoadConstructorModelsResult result = new LoadConstructorModelsResult(0, Collections.<LoadModelsDto>emptyList());
        // given
        given(loadConstructorsUseCase.loadConstructorModels(any(Long.class)))
                .willReturn(Collections.<LoadModelsDto>emptyList());
        // when
        mvc.perform(get("/constructors/{constructorId}/models", 1L).with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorModelsResult.class));

        // then

        then(loadConstructorsUseCase).should(times(1)).loadConstructorModels(eq(1L));
    }

    @Test
    void testLoadConstructorModelsWithNullResult() throws Exception {

        LoadConstructorModelsResult result = new LoadConstructorModelsResult(0, Collections.<LoadModelsDto>emptyList());
        // given
        given(loadConstructorsUseCase.loadConstructorModels(any(Long.class))).willReturn(null);

        // when
        mvc.perform(get("/constructors/{constructorId}/models", 1L).with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(result, LoadConstructorModelsResult.class));
        // then

        then(loadConstructorsUseCase).should(times(1)).loadConstructorModels(eq(1L));
    }
}
