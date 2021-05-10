package com.excentria_it.wamya.common.exception.handlers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.AuthServerError;
import com.excentria_it.wamya.common.exception.AuthorizationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	private ObjectMapper mapper;

	private static final String INVALID_GRANT_ERROR = "invalid_grant";

	@Override
	public boolean hasError(ClientHttpResponse httpResponse) throws IOException {

		return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
				|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
	}

	@Override
	public void handleError(ClientHttpResponse httpResponse) throws IOException {
		// A hack to avoid IOException on httpResponse.getBody()
		httpResponse.getStatusCode();
		
		try {
			ApiError apiError = mapper.readValue(httpResponse.getBody(), ApiError.class);
			if (apiError.getErrors() != null) {
				throw new RuntimeException(apiError.getErrors().toString());
			}
			throw new RuntimeException(httpResponse.getStatusText());

		} catch (JsonMappingException e) {
			try {
				AuthServerError authServerError = mapper.readValue(httpResponse.getBody(), AuthServerError.class);
				if (authServerError.getError() != null) {
					if (INVALID_GRANT_ERROR.equals(authServerError.getError())) {
						throw new AuthorizationException(authServerError.getErrorDescription());
					}
					throw new RuntimeException(authServerError.getErrorDescription());
				}
				throw new RuntimeException(httpResponse.getStatusText());
			} catch (JsonMappingException e1) {
				throw new RuntimeException(httpResponse.getStatusText());
			}
		}


	}
}
