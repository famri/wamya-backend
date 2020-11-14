package com.excentria_it.wamya.common.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	private ObjectMapper mapper;

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
		if (apiError != null && apiError.getErrors() != null) {
			throw new RuntimeException(apiError.getErrors().toString());
		}
		throw new RuntimeException(httpResponse.getStatusText());
	}
}
