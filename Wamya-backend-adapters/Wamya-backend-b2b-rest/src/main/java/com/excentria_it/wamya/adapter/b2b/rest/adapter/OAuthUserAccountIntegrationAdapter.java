package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import java.io.IOException;
import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.AuthServerError;
import com.excentria_it.wamya.common.exception.AuthorizationException;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@Slf4j
public class OAuthUserAccountIntegrationAdapter implements OAuthUserAccountPort {

	private static final String USERNAME_FORM_KEY = "username";
	private static final String PASSWORD_FORM_KEY = "password";
	private static final String GRANT_TYPE_FORM_KEY = "grant_type";
	private static final String GRANT_TYPE_FORM_VALUE = "password";

	private AuthServerProperties authServerProperties;

	private WebClient.Builder webClientBuilder;

	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

	private WebClient webClient;

	private ObjectMapper mapper;

	public OAuthUserAccountIntegrationAdapter(AuthServerProperties authServerProperties,
			WebClient.Builder webClientBuilder, ObjectMapper mapper,
			AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager) {
		this.authServerProperties = authServerProperties;
		this.webClientBuilder = webClientBuilder;
		this.mapper = mapper;
		this.authorizedClientServiceAndManager = authorizedClientServiceAndManager;

	}

	@Override
	public Long createOAuthUserAccount(OAuthUserAccount userAccount) {

		OAuthUserAccount user = getWebClient().post().uri(authServerProperties.getCreateUserUri())
				.bodyValue(userAccount).retrieve().bodyToMono(OAuthUserAccount.class)
				.onErrorMap(WebClientResponseException.class, ex -> handleCreateOAuthUserAccountExceptions(ex)).block();

		return user.getOauthId();

	}

	@Override
	public JwtOAuth2AccessToken fetchJwtTokenForUser(String username, String password) {

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
		formParams.add(USERNAME_FORM_KEY, username);
		formParams.add(PASSWORD_FORM_KEY, password);
		formParams.add(GRANT_TYPE_FORM_KEY, GRANT_TYPE_FORM_VALUE);

		JwtOAuth2AccessToken jwtToken = getWebClient().post().uri(authServerProperties.getTokenUri())
				.headers(headers -> {
					headers.setBasicAuth(authServerProperties.getClientId(), authServerProperties.getClientSecret());
					headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				}).body(BodyInserters.fromFormData(formParams)).retrieve().bodyToMono(JwtOAuth2AccessToken.class)
				.onErrorMap(WebClientResponseException.class, ex -> handleFetchJwtTokenForUserExceptions(ex)).block();

		return jwtToken;

	}

	@Override
	public void resetPassword(Long userOauthId, String password) {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

		getWebClient().post()
				.uri(authServerProperties.getResetPasswordUri().replace("{oAuthId}", userOauthId.toString()))
				.headers(headers -> {
					headers.setBearerAuth(accessToken.getTokenValue());
					headers.setContentType(MediaType.APPLICATION_JSON);
				}).bodyValue(new PasswordBody(password)).retrieve().bodyToMono(Void.class).block();

	}

	@Override
	public void updateMobileNumber(Long userOauthId, String internationalCallingCode, String mobileNumber) {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

		getWebClient().patch()
				.uri(authServerProperties.getUpdateMobileUri().replace("{oAuthId}", userOauthId.toString()))
				.headers(headers -> {
					headers.setBearerAuth(accessToken.getTokenValue());
					headers.setContentType(MediaType.APPLICATION_JSON);
				}).bodyValue(new UpdateMobileBody(internationalCallingCode, mobileNumber)).retrieve()
				.bodyToMono(Void.class).block();

	}

	@Override
	public void updateEmail(Long userOauthId, String email) {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
		OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

		getWebClient().patch()
				.uri(authServerProperties.getUpdateEmailUri().replace("{oAuthId}", userOauthId.toString()))
				.headers(headers -> {
					headers.setBearerAuth(accessToken.getTokenValue());
					headers.setContentType(MediaType.APPLICATION_JSON);
				}).bodyValue(new UpdateEmailBody(email)).retrieve().bodyToMono(Void.class).block();

	}

	private WebClient getWebClient() {
		if (webClient == null) {
			webClient = webClientBuilder.build();
		}
		return webClient;
	}

	private Throwable handleCreateOAuthUserAccountExceptions(Throwable ex) {

		WebClientResponseException wcre = (WebClientResponseException) ex;

		switch (wcre.getStatusCode()) {

		case BAD_REQUEST:
			return new UserAccountAlreadyExistsException(getApiErrorMessage(wcre));

		default:
			log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
			log.warn("Error body: {}", wcre.getResponseBodyAsString());
			return ex;
		}
	}

	private Throwable handleFetchJwtTokenForUserExceptions(Throwable ex) {

		WebClientResponseException wcre = (WebClientResponseException) ex;

		switch (wcre.getStatusCode()) {

		case BAD_REQUEST:
			return new AuthorizationException(getAuthServerErrorMessage(wcre));
		case UNAUTHORIZED:
			return new UserAccountNotFoundException(getAuthServerErrorMessage(wcre));
		default:
			log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
			log.warn("Error body: {}", wcre.getResponseBodyAsString());
			return ex;
		}
	}

	private String getAuthServerErrorMessage(WebClientResponseException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), AuthServerError.class).getErrorDescription();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}

	private String getApiErrorMessage(WebClientResponseException ex) {
		try {
			return mapper.readValue(ex.getResponseBodyAsString(), ApiError.class).getErrors().toString();
		} catch (IOException ioex) {
			return ex.getMessage();
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class PasswordBody {
		private String password;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class UpdateMobileBody {
		private String icc;
		private String mobileNumber;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class UpdateEmailBody {
		private String email;

	}

}
