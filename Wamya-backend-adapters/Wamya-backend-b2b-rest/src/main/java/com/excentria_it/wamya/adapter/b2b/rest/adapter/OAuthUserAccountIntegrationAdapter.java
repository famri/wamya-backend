package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.excentria_it.wamya.adapter.b2b.rest.dto.User;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.CreateOAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.OAuthUserAccount;

import lombok.extern.slf4j.Slf4j;

@WebAdapter
@Slf4j
public class OAuthUserAccountIntegrationAdapter implements CreateOAuthUserAccountPort {

	private AuthServerProperties authServerProperties;

	private WebClient.Builder webClientBuilder;

	public OAuthUserAccountIntegrationAdapter(AuthServerProperties authServerProperties,
			@Qualifier(value = "on-prem-auth-server-web-client") WebClient.Builder webClientBuilder) {
		this.authServerProperties = authServerProperties;
		this.webClientBuilder = webClientBuilder;
	}

	private String authorizationServerUrl = "lb://{authServer}:9999/oauth/users";

	private WebClient webClient;

	@Override
	public UUID createOAuthUserAccount(OAuthUserAccount userAccount) {

		log.debug("Will call the Authorization Server URL: {}", authorizationServerUrl);

		User user = getWebClient().post().uri(authorizationServerUrl, authServerProperties.getAuthServer())
				.bodyValue(userAccount).retrieve().bodyToMono(User.class).log()
				.onErrorMap(WebClientResponseException.class, ex -> handleException(ex)).block();

		return user.getOauthId();

	}

	public void setAuthorizationServerUrl(String authorizationServerUrl) {
		this.authorizationServerUrl = authorizationServerUrl;
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
