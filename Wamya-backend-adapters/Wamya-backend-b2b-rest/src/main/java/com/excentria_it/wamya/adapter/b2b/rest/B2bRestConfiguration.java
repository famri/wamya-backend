package com.excentria_it.wamya.adapter.b2b.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class B2bRestConfiguration {

	private static final String REGISTRATION_ID = "on-prem-auth-server";

//	@Bean(name = "on-prem-auth-server-web-client")
//	@LoadBalanced
//	public WebClient.Builder webClientBuilder(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
//		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction();
//
//		oauth.setDefaultClientRegistrationId(REGISTRATION_ID);
//
//		return WebClient.builder().filter(oauth);
//
//	}

//	@Bean
//	public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
//			ClientRegistrationRepository clientRegistrationRepository) {
//
//		// Create authorized client provider supporting client_credentials grant flow
//		ReactiveOAuth2AuthorizedClientProvider authorizedClientProvider = ReactiveOAuth2AuthorizedClientProviderBuilder
//				.builder().clientCredentials().build();
//
//		// Fetch the client registration from application.yml by registration ID
//		ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID);
//
//		// Create a ClientRegistrationRepository using the client registration
//		// configuration from application.yml
//		InMemoryReactiveClientRegistrationRepository clientRegistrationRepo = new InMemoryReactiveClientRegistrationRepository(
//				clientRegistration);
//
//		// Create an AuthorizedClientService using the ClientRegistrationRepository
//		ReactiveOAuth2AuthorizedClientService authorizedClientService = new InMemoryReactiveOAuth2AuthorizedClientService(
//				clientRegistrationRepo);
//
//		// Create an AuthorizedClientManager using the ClientRegistrationRepository and
//		// the AuthorizedClientService
//		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
//				clientRegistrationRepo, authorizedClientService);
//
//		// Set the client_credentials AuthorizedClientProvider as
//		// AuthorizedClientProvider in AuthorizedClientManager
//		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//		return authorizedClientManager;
//
//	}

	@Bean
	public OAuth2AuthorizedClientManager OAuth2AuthorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService authorizedClientService) {

		// Create authorized client provider supporting client_credentials grant flow
		OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials().build();

		// Create an AuthorizedClientManager using the ClientRegistrationRepository and
		// the AuthorizedClientService
		AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientService);

		// Set the client_credentials AuthorizedClientProvider as
		// AuthorizedClientProvider in AuthorizedClientManager
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;

	}

	@Bean(name = "on-prem-auth-server-web-client")
	public WebClient.Builder webClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);

		oauth2.setDefaultClientRegistrationId(REGISTRATION_ID);

		return WebClient.builder().apply(oauth2.oauth2Configuration());

	}

}
