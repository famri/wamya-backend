package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.domain.OAuthRole;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class OAuthUserAccountIntegrationAdapterTests {

	public static MockWebServer mockBackEnd;

	private OAuthUserAccountIntegrationAdapter oAuthUserAccountIntegrationAdapter;

	@BeforeAll
	static void setUp() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@BeforeEach
	void initialize() throws JsonProcessingException {
		String baseUrl = String.format("http://{authServer}:%s/users", mockBackEnd.getPort());

		AuthServerProperties authServerProperties = Mockito.mock(AuthServerProperties.class);
		when(authServerProperties.getAuthServer()).thenReturn("localhost");
		oAuthUserAccountIntegrationAdapter = new OAuthUserAccountIntegrationAdapter(authServerProperties,
				WebClient.builder());

		oAuthUserAccountIntegrationAdapter.setAuthorizationServerUrl(baseUrl);

	}

	@Test
	void testCreateOAuthUserAccount() throws JsonProcessingException {
		UUID uuid = UUID.randomUUID();
		ObjectMapper objectMapper = new ObjectMapper();
		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(uuid)).addHeader("Content-Type",
				"application/json"));

		OAuthUserAccount userAccount = new OAuthUserAccount("foued", "amri", "test@test.com", "+21622222222",
				"password", true, true, true, true, List.of(new OAuthRole("TRANSPORTER")));

		UUID result = oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(userAccount);

		assertEquals(uuid, result);

	}

	@Test
	void testCreateOAuthUserAccountException() throws JsonProcessingException {

		mockBackEnd.enqueue(new MockResponse().setResponseCode(500));

		OAuthUserAccount userAccount = new OAuthUserAccount("foued", "amri", "test@test.com", "+21622222222",
				"password", true, true, true, true, List.of(new OAuthRole("TRANSPORTER")));

		assertThrows(WebClientException.class,
				() -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(userAccount));

	}
}
