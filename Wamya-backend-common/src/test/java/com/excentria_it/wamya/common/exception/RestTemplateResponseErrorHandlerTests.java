package com.excentria_it.wamya.common.exception;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class RestTemplateResponseErrorHandlerTests {

	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void givenClientErrorClientHttpResponse_WhenHasError_ThenReturnTrue() throws IOException {
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);

		when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

		boolean hasError = restTemplateResponseErrorHandler.hasError(clientHttpResponse);

		assertEquals(hasError, true);
	}

	@Test
	public void givenServerErrorClientHttpResponse_WhenHasError_ThenReturnTrue() throws IOException {
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.SERVICE_UNAVAILABLE);

		boolean hasError = restTemplateResponseErrorHandler.hasError(clientHttpResponse);

		assertEquals(hasError, true);
	}

	@Test
	public void givenNoErrorClientHttpResponse_WhenHasError_ThenReturnFalse() throws IOException {
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);

		when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.OK);

		boolean hasError = restTemplateResponseErrorHandler.hasError(clientHttpResponse);

		assertEquals(hasError, false);
	}

	@Test
	public void givenApiErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeException() throws IOException {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, List.of("Error1", "Error2"));
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(apiError).getBytes()));

		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class)).thenReturn(apiError);

		assertThrows(RuntimeException.class, () -> restTemplateResponseErrorHandler.handleError(clientHttpResponse));

	}

	@Test
	public void givenApiErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeExceptionWithApiErrorErrors()
			throws IOException {

		List<String> errors = List.of("Error1", "Error2");
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errors);
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(apiError).getBytes()));
		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class)).thenReturn(apiError);

		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals(errors.toString(), e.getMessage());

		}

	}

	@Test
	public void givenNotApiErrorNorAuthServerErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeException()
			throws IOException {

		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString("SOME STRING").getBytes()));
		when(clientHttpResponse.getStatusText()).thenReturn("SOME SATUS TEXT");

		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class))
				.thenThrow(JsonMappingException.class);

		when(objectMapper.readValue(clientHttpResponse.getBody(), AuthServerError.class))
				.thenThrow(JsonMappingException.class);

		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("SOME SATUS TEXT", e.getMessage());

		}

	}

	@Test
	public void givenAuthServerErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeException() throws IOException {

		AuthServerError authError = new AuthServerError();
		authError.setError("invalid_grant");
		authError.setErrorDescription("SOME ERROR DESCRIPTION");

		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(authError).getBytes()));

		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class))
				.thenThrow(JsonMappingException.class);

		when(objectMapper.readValue(clientHttpResponse.getBody(), AuthServerError.class)).thenReturn(authError);

		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof AuthorizationException);
			assertEquals("SOME ERROR DESCRIPTION", e.getMessage());

		}

	}
	@Test
	public void givenAuthServerErrorButNotInvalidGrantErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeException() throws IOException {

		AuthServerError authError = new AuthServerError();
		authError.setError("SOME ERROR");
		authError.setErrorDescription("SOME ERROR DESCRIPTION");

		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(authError).getBytes()));

		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class))
				.thenThrow(JsonMappingException.class);

		when(objectMapper.readValue(clientHttpResponse.getBody(), AuthServerError.class)).thenReturn(authError);

		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("SOME ERROR DESCRIPTION", e.getMessage());

		}

	}
	
	@Test
	public void givenAuthServerErrorWithNullErrorClientHttpResponse_WhenHandleError_ThenThrowRuntimeException() throws IOException {

		AuthServerError authError = new AuthServerError();

		authError.setErrorDescription("SOME ERROR DESCRIPTION");

		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(authError).getBytes()));
		when(clientHttpResponse.getStatusText()).thenReturn("SOME STATUS TEXT");
		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class))
				.thenThrow(JsonMappingException.class);

		when(objectMapper.readValue(clientHttpResponse.getBody(), AuthServerError.class)).thenReturn(authError);

		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("SOME STATUS TEXT", e.getMessage());

		}

	}
	
	@Test
	public void givenApiErrorWithNullErrorsClientHttpResponse_WhenHandleError_ThenThrowRuntimeException() throws IOException {


		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		
		ClientHttpResponse clientHttpResponse = Mockito.mock(ClientHttpResponse.class);
		when(clientHttpResponse.getBody())
				.thenReturn(new ByteArrayInputStream(mapper.writeValueAsString(apiError).getBytes()));
		when(clientHttpResponse.getStatusText()).thenReturn("SOME STATUS TEXT");
		
		when(objectMapper.readValue(clientHttpResponse.getBody(), ApiError.class)).thenReturn(apiError);
	
		
		try {
			restTemplateResponseErrorHandler.handleError(clientHttpResponse);
			fail("No Exception thrown!");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("SOME STATUS TEXT", e.getMessage());
		}

	}
}
