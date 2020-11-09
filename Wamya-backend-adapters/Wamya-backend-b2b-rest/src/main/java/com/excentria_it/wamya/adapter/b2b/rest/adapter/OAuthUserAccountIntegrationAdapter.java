package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.excentria_it.wamya.adapter.b2b.rest.dto.User;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.OAuthUserAccount;

import lombok.extern.slf4j.Slf4j;

@WebAdapter
@Slf4j
public class OAuthUserAccountIntegrationAdapter implements OAuthUserAccountPort {

	private AuthServerProperties authServerProperties;

	private WebClient.Builder webClientBuilder;

	private OAuth2AuthorizedClientManager authorizedClientManager;

	private WebClient webClient;

	public OAuthUserAccountIntegrationAdapter(AuthServerProperties authServerProperties,
			@Qualifier(value = "no-oauth-web-client") WebClient.Builder webClientBuilder,
			OAuth2AuthorizedClientManager authorizedClientManager) {
		this.authServerProperties = authServerProperties;
		this.webClientBuilder = webClientBuilder;
		this.authorizedClientManager = authorizedClientManager;

	}

	@Override
	public UUID createOAuthUserAccount(OAuthUserAccount userAccount) {

		User user = getWebClient().post().uri(authServerProperties.getCreateUserUri()).bodyValue(userAccount).retrieve()
				.bodyToMono(User.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex))
				.block();

		return user.getOauthId();

	}

	@Override
	public OAuth2AccessToken authorizeOAuthUser(String userName, String password) {

		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
				.withClientRegistrationId(authServerProperties.getPasswordRegistrationId()).principal(userName)
				.attributes(attrs -> {
					attrs.put(OAuth2ParameterNames.USERNAME, userName);
					attrs.put(OAuth2ParameterNames.PASSWORD, password);
				}).build();

		OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);

		return authorizedClient.getAccessToken();
	}

	private WebClient getWebClient() {
		if (this.webClient == null) {
			this.webClient = webClientBuilder.build();
		}

		return this.webClient;
	}

	private Throwable handleException(Throwable ex) {
		log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
		return ex;
//		
//		if (!(ex instanceof WebClientResponseException)) {
//			log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
//			return ex;
//		}
//
//		WebClientResponseException wcre = (WebClientResponseException) ex;
//
//		switch (wcre.getStatusCode()) {
//
//		case NOT_FOUND:
//			return new NotFoundException(getErrorMessage(wcre));
//
//		case UNPROCESSABLE_ENTITY:
//			return new InvalidInputException(getErrorMessage(wcre));
//
//		default:
//			log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
//			log.warn("Error body: {}", wcre.getResponseBodyAsString());
//			return ex;
//		}
	}

//	private String getErrorMessage(WebClientResponseException ex) {
//		try {
//			return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
//		} catch (IOException ioex) {
//			return ex.getMessage();
//		}
//	}

}
