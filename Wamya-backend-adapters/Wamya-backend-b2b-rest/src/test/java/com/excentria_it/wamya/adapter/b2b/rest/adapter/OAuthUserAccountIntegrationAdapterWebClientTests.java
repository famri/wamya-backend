package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.excentria_it.wamya.adapter.b2b.rest.adapter.OAuthUserAccountIntegrationAdapter.PasswordBody;
import com.excentria_it.wamya.adapter.b2b.rest.adapter.OAuthUserAccountIntegrationAdapter.UpdateEmailBody;
import com.excentria_it.wamya.adapter.b2b.rest.adapter.OAuthUserAccountIntegrationAdapter.UpdateMobileBody;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.AuthServerError;
import com.excentria_it.wamya.common.exception.AuthorizationException;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.OAuthRole;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

@SpringBootTest
@ActiveProfiles("b2b-rest-local")
public class OAuthUserAccountIntegrationAdapterWebClientTests {

	public static MockWebServer mockBackEnd;

	@BeforeAll
	static void setUp() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@Autowired
	private AuthServerProperties authServerProperties;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	private ObjectMapper objectMapper;

	@Mock
	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

	private static final String ACCESS_TOKEN_STRING = "SOME_TOKEN_STRING";

	private OAuthUserAccountIntegrationAdapter oAuthUserAccountIntegrationAdapter;

	@BeforeEach
	void initialize() {
		String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		authServerProperties.setClientId("clientId");
		authServerProperties.setClientSecret("clientSecret");
		authServerProperties.setCreateUserUri(baseUrl + "/oauth/users");
		authServerProperties.setTokenUri(baseUrl + "/oauth/token");
		authServerProperties.setResetPasswordUri(baseUrl + "/oauth/users/{oAuthId}/do-reset-password");
		authServerProperties.setUpdateMobileUri(baseUrl + "/oauth/users/{oAuthId}/mobile");
		authServerProperties.setUpdateEmailUri(baseUrl + "/oauth/users/{oAuthId}/email");

		oAuthUserAccountIntegrationAdapter = new OAuthUserAccountIntegrationAdapter(authServerProperties,
				webClientBuilder, objectMapper, authorizedClientServiceAndManager);
	}

	@Test
	void testCreateOAuthUserAccount() throws InterruptedException, IOException {

		Long userOAuthId = 1L;

		OAuthUserAccount oAuthUserAccountResponse = new OAuthUserAccount(userOAuthId, "foued", "amri", "test@test.com",
				"+216_22222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(oAuthUserAccountResponse))
				.setResponseCode(HttpStatus.CREATED.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com",
				"+216_22222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		Long result = oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount);

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users", recordedRequest.getPath());

		assertEquals(oAuthUserAccount,
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), OAuthUserAccount.class));

		assertEquals(userOAuthId, result);

	}

	@Test
	void givenExistentUserAccount_WhenCreateOAuthUserAccount_ThenThrowUserAccountAlreadyExistsException()
			throws InterruptedException, IOException {

		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.BAD_REQUEST);
		apiError.setErrors(List.of("Error1", "Error2"));

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(apiError))
				.setResponseCode(HttpStatus.BAD_REQUEST.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com",
				"+216_22222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		assertThrows(UserAccountAlreadyExistsException.class,
				() -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users", recordedRequest.getPath());

		assertEquals(oAuthUserAccount,
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), OAuthUserAccount.class));

	}

	@Test
	void givenAuthorizationServerError_WhenCreateOAuthUserAccount_ThenThrowWebClientResponseException()
			throws InterruptedException, IOException {

		ApiError apiError = new ApiError();
		apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setErrors(List.of("Error1", "Error2"));

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(apiError))
				.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com",
				"+216_22222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		assertThrows(WebClientResponseException.class,
				() -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users", recordedRequest.getPath());

		assertEquals(oAuthUserAccount,
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), OAuthUserAccount.class));

	}

	@Test
	void givenNotAuthorizationServerError_WhenCreateOAuthUserAccount_ThenThrowUserAccountAlreadyExistsExceptionWithErrorMessage()
			throws InterruptedException, IOException {

		String error = "SOME ERROR";

		mockBackEnd.enqueue(new MockResponse().setBody(error).setResponseCode(HttpStatus.BAD_REQUEST.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com",
				"+216_22222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		try {
			oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount);
			fail("No exception was thrown!");
		} catch (UserAccountAlreadyExistsException e) {
			assertEquals(
					String.format("400 Bad Request from POST http://localhost:%s/oauth/users", mockBackEnd.getPort()),
					e.getMessage());
		}

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();
		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users", recordedRequest.getPath());

		assertEquals(oAuthUserAccount,
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), OAuthUserAccount.class));

	}

	@Test
	void testAuthorizeOAuthUser() throws URISyntaxException, InterruptedException, JsonProcessingException {
		JwtOAuth2AccessToken oAuth2AccessTokenResponse = new JwtOAuth2AccessToken(ACCESS_TOKEN_STRING, "Bearer",
				"REFRESH_TOKEN", 36000L, "read write", UUID.randomUUID().toString());

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(authServerProperties.getClientId(), authServerProperties.getClientSecret());

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(oAuth2AccessTokenResponse))
				.setResponseCode(HttpStatus.OK.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		JwtOAuth2AccessToken oAuth2AccessToken = oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test",
				"test");

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		String expectedRequestBodyStr = "username=test&password=test&grant_type=password";

		String actualRequestBodyStr = recordedRequest.getBody().readUtf8();

		String actualAuthorizationHeader = recordedRequest.getHeader(HttpHeaders.AUTHORIZATION);

		String actualContentTypeHeader = recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE);

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/token", recordedRequest.getPath());

		assertEquals(expectedRequestBodyStr, actualRequestBodyStr);

		assertEquals(headers.getOrEmpty(HttpHeaders.AUTHORIZATION).get(0), actualAuthorizationHeader);

		assertEquals(MediaType.APPLICATION_FORM_URLENCODED + ";charset=UTF-8", actualContentTypeHeader);

		assertEquals(ACCESS_TOKEN_STRING, oAuth2AccessToken.getAccessToken());

	}

	@Test
	void givenBadCredentials_WhenFetchJwtTokenForUser_ThenThrowAuthorizationException()
			throws InterruptedException, JsonProcessingException {

		AuthServerError authServerError = new AuthServerError();
		authServerError.setError("SOME ERROR");
		authServerError.setErrorDescription("SOME ERROR DESCRIPTION");

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(authServerError))
				.setResponseCode(HttpStatus.BAD_REQUEST.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		assertThrows(AuthorizationException.class,
				() -> oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test", "test"));

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/token", recordedRequest.getPath());

	}

	@Test
	void givenUnauthorized_WhenFetchJwtTokenForUser_ThenThrowUserAccountNotFoundException()
			throws InterruptedException, JsonProcessingException {

		AuthServerError authServerError = new AuthServerError();
		authServerError.setError("SOME ERROR");
		authServerError.setErrorDescription("SOME ERROR DESCRIPTION");

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(authServerError))
				.setResponseCode(HttpStatus.UNAUTHORIZED.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		assertThrows(UserAccountNotFoundException.class,
				() -> oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test", "test"));

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/token", recordedRequest.getPath());

	}

	@Test
	void givenAuthorizationServerError_WhenFetchJwtTokenForUser_ThenThrowWebClientResponseException()
			throws InterruptedException, JsonProcessingException {

		AuthServerError authServerError = new AuthServerError();
		authServerError.setError("SOME ERROR");
		authServerError.setErrorDescription("SOME ERROR DESCRIPTION");

		mockBackEnd.enqueue(new MockResponse().setBody(objectMapper.writeValueAsString(authServerError))
				.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		assertThrows(WebClientResponseException.class,
				() -> oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test", "test"));

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/token", recordedRequest.getPath());

	}

	@Test
	void givenAuthorizationServerError_WhenFetchJwtTokenForUser_ThenThrowAuthorizationExceptionWithErrorMessage()
			throws InterruptedException, JsonProcessingException {

		String error = "SOME ERROR";

		mockBackEnd.enqueue(new MockResponse().setBody(error).setResponseCode(HttpStatus.BAD_REQUEST.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
		try {
			oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test", "test");
			fail("No exception was thrown!");
		} catch (AuthorizationException e) {
			assertEquals(
					String.format("400 Bad Request from POST http://localhost:%s/oauth/token", mockBackEnd.getPort()),
					e.getMessage());
		}

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/token", recordedRequest.getPath());

	}

	@Test
	void testResetPassword() throws JsonMappingException, JsonProcessingException, InterruptedException {
		OAuth2AuthorizedClient oAuth2AuthorizedClient = Mockito.mock(OAuth2AuthorizedClient.class);
		OAuth2AccessToken oAuth2AccessToken = Mockito.mock(OAuth2AccessToken.class);
		given(oAuth2AccessToken.getTokenValue()).willReturn("SOME_OAUTH2_BEARER_TOKEN");
		given(oAuth2AuthorizedClient.getAccessToken()).willReturn(oAuth2AccessToken);

		given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class)))
				.willReturn(oAuth2AuthorizedClient);

		mockBackEnd.enqueue(new MockResponse().setBody("OK").setResponseCode(HttpStatus.OK.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		oAuthUserAccountIntegrationAdapter.resetPassword(1L, "myNewPassword");

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users/1/do-reset-password", recordedRequest.getPath());

		assertEquals("myNewPassword",
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), PasswordBody.class).getPassword());

		assertEquals("Bearer " + "SOME_OAUTH2_BEARER_TOKEN", recordedRequest.getHeader(HttpHeaders.AUTHORIZATION));
	}

	@Test
	void testUpdateMobile() throws JsonMappingException, JsonProcessingException, InterruptedException {
		OAuth2AuthorizedClient oAuth2AuthorizedClient = Mockito.mock(OAuth2AuthorizedClient.class);
		OAuth2AccessToken oAuth2AccessToken = Mockito.mock(OAuth2AccessToken.class);
		given(oAuth2AccessToken.getTokenValue()).willReturn("SOME_OAUTH2_BEARER_TOKEN");
		given(oAuth2AuthorizedClient.getAccessToken()).willReturn(oAuth2AccessToken);

		given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class)))
				.willReturn(oAuth2AuthorizedClient);

		mockBackEnd.enqueue(new MockResponse().setBody("OK").setResponseCode(HttpStatus.OK.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		oAuthUserAccountIntegrationAdapter.updateMobileNumber(1L, "+216", "12121212");

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.PATCH.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users/1/mobile", recordedRequest.getPath());

		UpdateMobileBody updateMobileBody = objectMapper.readValue(recordedRequest.getBody().readUtf8(),
				UpdateMobileBody.class);

		assertEquals("+216", updateMobileBody.getIcc());
		assertEquals("12121212", updateMobileBody.getMobileNumber());

		assertEquals("Bearer " + "SOME_OAUTH2_BEARER_TOKEN", recordedRequest.getHeader(HttpHeaders.AUTHORIZATION));
	}

	@Test
	void testUpdateEmail() throws JsonMappingException, JsonProcessingException, InterruptedException {
		OAuth2AuthorizedClient oAuth2AuthorizedClient = Mockito.mock(OAuth2AuthorizedClient.class);
		OAuth2AccessToken oAuth2AccessToken = Mockito.mock(OAuth2AccessToken.class);
		given(oAuth2AccessToken.getTokenValue()).willReturn("SOME_OAUTH2_BEARER_TOKEN");
		given(oAuth2AuthorizedClient.getAccessToken()).willReturn(oAuth2AccessToken);

		given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class)))
				.willReturn(oAuth2AuthorizedClient);

		mockBackEnd.enqueue(new MockResponse().setBody("OK").setResponseCode(HttpStatus.OK.value())
				.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

		oAuthUserAccountIntegrationAdapter.updateEmail(1L, "new.email@gmail.com");

		RecordedRequest recordedRequest = mockBackEnd.takeRequest();

		assertEquals(HttpMethod.PATCH.name(), recordedRequest.getMethod());

		assertEquals("/oauth/users/1/email", recordedRequest.getPath());

		assertEquals("new.email@gmail.com",
				objectMapper.readValue(recordedRequest.getBody().readUtf8(), UpdateEmailBody.class).getEmail());

		assertEquals("Bearer " + "SOME_OAUTH2_BEARER_TOKEN", recordedRequest.getHeader(HttpHeaders.AUTHORIZATION));
	}
}
