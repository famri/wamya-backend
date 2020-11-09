package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.excentria_it.wamya.adapter.b2b.rest.dto.User;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.domain.OAuthRole;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class OAuthUserAccountIntegrationAdapterTests {

	private static MockWebServer mockBackEnd;

	private static final String ACCESS_TOKEN_STRING = "SOME_TOKEN_STRING";

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

		AuthServerProperties authServerProperties = Mockito.mock(AuthServerProperties.class);
		when(authServerProperties.getCreateUserUri())
				.thenReturn(String.format("http://localhost:%s/users", mockBackEnd.getPort()));
		when(authServerProperties.getPasswordRegistrationId()).thenReturn("someRegistrationId");

		OAuth2AuthorizedClientManager authorizedClientManager = Mockito.mock(OAuth2AuthorizedClientManager.class);

		OAuth2AuthorizedClient oAuth2AuthorizedClient = Mockito.mock(OAuth2AuthorizedClient.class);

		OAuth2AccessToken oAuth2AccessToken = Mockito.mock(OAuth2AccessToken.class);

		when(oAuth2AccessToken.getTokenValue()).thenReturn("SOME_TOKEN_STRING");

		when(oAuth2AuthorizedClient.getAccessToken()).thenReturn(oAuth2AccessToken);

		when(authorizedClientManager.authorize(any(OAuth2AuthorizeRequest.class))).thenReturn(oAuth2AuthorizedClient);

		oAuthUserAccountIntegrationAdapter = new OAuthUserAccountIntegrationAdapter(authServerProperties,
				WebClient.builder(), authorizedClientManager);

	}

	@Test
	void testCreateOAuthUserAccount() throws JsonProcessingException {
		UUID uuid = UUID.randomUUID();
		ObjectMapper objectMapper = new ObjectMapper();

		User user = new User();
		user.setOauthId(uuid);

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(user)).addHeader("Content-Type",
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

	@Test
	void testAuthorizeOAuthUser() {

		OAuth2AccessToken oAuth2AccessToken = oAuthUserAccountIntegrationAdapter.authorizeOAuthUser("test", "test");

		assertEquals(ACCESS_TOKEN_STRING, oAuth2AccessToken.getTokenValue());

	}
}
