package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.OAuthRole;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
//@ActiveProfiles("b2b-rest-local")
public class OAuthUserAccountIntegrationAdapterRestTemplateTests {

	//@Autowired
	private RestTemplate restTemplate;

	//@Autowired
	private AuthServerProperties authServerProperties;

	private MockRestServiceServer mockServer;

	private ObjectMapper objectMapper = new ObjectMapper();

	private static final String ACCESS_TOKEN_STRING = "SOME_TOKEN_STRING";

	//@Autowired
	private OAuthUserAccountIntegrationAdapter oAuthUserAccountIntegrationAdapter;

	//@BeforeEach
	public void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	//@Test
	void testCreateOAuthUserAccount() throws JsonProcessingException, URISyntaxException {

		Long userOAuthId = 1L;

		OAuthUserAccount oAuthUserAccountResponse = new OAuthUserAccount(userOAuthId, "foued", "amri", "test@test.com",
				"+21622222222", "password", true, true, true, true, List.of(new OAuthRole("ROLE_TRANSPORTER")));

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
				.andExpect(method(HttpMethod.POST))
				.andRespond(withStatus(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON)
						.body(objectMapper.writeValueAsString(oAuthUserAccountResponse)));

		OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com", "+21622222222",
				"password", true, true, true, true, List.of(new OAuthRole("TRANSPORTER")));

		Long result = oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount);

		assertEquals(userOAuthId, result);

	}

	//@Test
	void testAuthorizeOAuthUser() throws JsonProcessingException, URISyntaxException {
		JwtOAuth2AccessToken oAuth2AccessTokenResponse = new JwtOAuth2AccessToken(ACCESS_TOKEN_STRING, "Bearer",
				"REFRESH_TOKEN", 36000L, "read write", UUID.randomUUID().toString());

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
		formParams.add("username", "test");
		formParams.add("password", "test");
		formParams.add("grant_type", "password");

		mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getTokenUri())))
				.andExpect(method(HttpMethod.POST)).andExpect(content().formData(formParams))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
						.body(objectMapper.writeValueAsString(oAuth2AccessTokenResponse)));

		JwtOAuth2AccessToken oAuth2AccessToken = oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test",
				"test");

		assertEquals(ACCESS_TOKEN_STRING, oAuth2AccessToken.getAccessToken());

	}
}
