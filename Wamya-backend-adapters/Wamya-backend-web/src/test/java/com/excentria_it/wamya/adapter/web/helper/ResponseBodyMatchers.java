package com.excentria_it.wamya.adapter.web.helper;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.test.web.servlet.ResultMatcher;

import com.excentria_it.wamya.common.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseBodyMatchers {
	private ObjectMapper objectMapper = new ObjectMapper();

	public <T> ResultMatcher containsObjectAsJson(Object expectedObject, Class<T> targetClass) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			T actualObject = objectMapper.readValue(json, targetClass);
			assertThat(actualObject).isEqualToComparingFieldByField(expectedObject);
		};
	}

	public ResultMatcher containsErrors(List<String> expectedErrors) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			ApiError apiError = objectMapper.readValue(json, ApiError.class);
			List<String> actualErrors = apiError.getErrors();

			assertThat(actualErrors.containsAll(expectedErrors) && actualErrors.size() == expectedErrors.size())
					.isTrue().withFailMessage("expecting exactly %d error message but found %d.", expectedErrors.size(),
							actualErrors.size());
		};
	}

	public static ResponseBodyMatchers responseBody() {
		return new ResponseBodyMatchers();
	}
}
