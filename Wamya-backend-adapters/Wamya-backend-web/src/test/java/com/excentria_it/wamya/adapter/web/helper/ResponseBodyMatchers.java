package com.excentria_it.wamya.adapter.web.helper;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.test.web.servlet.ResultMatcher;

import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.AuthServerError;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.mrbean.MrBeanModule;

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

	public ResultMatcher containsAuthServerError(String expectedError) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			AuthServerError authServerError = objectMapper.readValue(json, AuthServerError.class);
			String actualError = authServerError.getError();

			assertThat(actualError != null && actualError.equals(expectedError)).isTrue()
					.withFailMessage("expecting %s error message but found %s.", expectedError, actualError);
		};
	}

	public static ResponseBodyMatchers responseBody() {
		return new ResponseBodyMatchers();
	}
}
