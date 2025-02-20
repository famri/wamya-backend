package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.application.port.in.LoadGendersUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadGendersDto;
import com.excentria_it.wamya.domain.LoadGendersResult;
import com.excentria_it.wamya.test.data.common.GenderTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@Import(value = {GenderController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = GenderController.class)
public class GenderControllerTests {
    @MockBean
    private LoadGendersUseCase loadGendersUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testLoadAllGenders() throws Exception {

        // given
        List<LoadGendersDto> loadGendersDtos = GenderTestData.defaultLoadGendersDtos();
        LoadGendersResult loadGendersResult = new LoadGendersResult(loadGendersDtos.size(), loadGendersDtos);
        given(loadGendersUseCase.loadAllGenders(any(String.class))).willReturn(loadGendersDtos);

        // when
        mockMvc.perform(get("/genders").param("lang", "fr_FR")).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadGendersResult, LoadGendersResult.class));
        // then

        then(loadGendersUseCase).should(times(1)).loadAllGenders(eq("fr_FR"));
    }

    @Test
    void testLoadAllGendersWithEmptyResult() throws Exception {

        // given

        LoadGendersResult loadGendersResult = new LoadGendersResult(0, Collections.emptyList());
        given(loadGendersUseCase.loadAllGenders(any(String.class))).willReturn(Collections.emptyList());

        // when
        mockMvc.perform(get("/genders").param("lang", "fr_FR")).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadGendersResult, LoadGendersResult.class));
        // then

        then(loadGendersUseCase).should(times(1)).loadAllGenders(eq("fr_FR"));
    }

    @Test
    void testLoadAllGendersWithNullResult() throws Exception {

        // given

        LoadGendersResult loadGendersResult = new LoadGendersResult(0, Collections.emptyList());

        given(loadGendersUseCase.loadAllGenders(any(String.class))).willReturn(null);

        // when
        mockMvc.perform(get("/genders").param("lang", "fr_FR")).andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(loadGendersResult, LoadGendersResult.class));
        // then

        then(loadGendersUseCase).should(times(1)).loadAllGenders(eq("fr_FR"));
    }
}