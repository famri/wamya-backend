package com.excentria_it.wamya.adapter.web.helper;

import com.excentria_it.wamya.common.exception.ApiError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ResponseBodyMatchers {

    private ObjectMapper objectMapper;

    ResponseBodyMatchers() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new MrBeanModule());

    }

    public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).isEqualToComparingFieldByField(expectedObject);
        };
    }

    public <T> ResultMatcher containsListOfObjectsAsJson(List<T> expectedObjectList,
                                                         TypeReference<List<T>> typeReference) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            List<T> actualObjectsList = objectMapper.readValue(json, typeReference);
            assertThat(actualObjectsList.containsAll(expectedObjectList)
                    && actualObjectsList.size() == expectedObjectList.size());
        };
    }

    public ResultMatcher containsApiErrors(List<String> expectedErrors) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            ApiError apiError = objectMapper.readValue(json, ApiError.class);
            List<String> actualErrors = apiError.getErrors();

            assertThat(actualErrors.containsAll(expectedErrors))
                    .withFailMessage("expecting exactly %s error message but found %s.",
                            expectedErrors.stream().collect(Collectors.joining(",")),
                            actualErrors != null ? actualErrors.stream().collect(Collectors.joining(",")) : "null")
                    .isTrue();

            assertThat(actualErrors.size() == expectedErrors.size())
                    .withFailMessage("expecting exactly %d error message but found %d.", expectedErrors.size(),
                            actualErrors.size())
                    .isTrue();
        };
    }

    public static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
}
