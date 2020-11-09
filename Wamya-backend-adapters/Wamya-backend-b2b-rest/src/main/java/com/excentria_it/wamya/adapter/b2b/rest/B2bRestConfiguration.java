package com.excentria_it.wamya.adapter.b2b.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class B2bRestConfiguration {

	private AuthServerProperties authServerProperties;

	@Bean
	public OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientService authorizedClientService) {

		// Create authorized client provider supporting client_credentials grant flow
		OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
				.clientCredentials().password().build();

		// Create an AuthorizedClientManager using the ClientRegistrationRepository and
		// the AuthorizedClientService
		AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
				clientRegistrationRepository, authorizedClientService);

		// Set the client_credentials AuthorizedClientProvider as
		// AuthorizedClientProvider in AuthorizedClientManager
		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		// Tell the AuthorizedClientManager how to find username and password
		authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

		return authorizedClientManager;

	}

	@Bean(name = "client-credentials-web-client")
	public WebClient.Builder clientCredentialsWebClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2ExchangeFunction = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);

		oauth2ExchangeFunction
				.setDefaultClientRegistrationId(authServerProperties.getClientCredentialsRegistrationId());

		return WebClient.builder().apply(oauth2ExchangeFunction.oauth2Configuration());

	}

	@Bean(name = "password-web-client")
	public WebClient.Builder passwordWebClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2ExchangeFunction = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);

		oauth2ExchangeFunction.setDefaultClientRegistrationId(authServerProperties.getPasswordRegistrationId());

		return WebClient.builder().apply(oauth2ExchangeFunction.oauth2Configuration());

	}

	@Bean(name = "no-oauth-web-client")
	public WebClient.Builder noOAuthWebClientBuilder(OAuth2AuthorizedClientManager authorizedClientManager) {

		return WebClient.builder();

	}
	private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
		return authorizeRequest -> {
			Map<String, Object> contextAttributes = Collections.emptyMap();

			String username, password;
			// get username and password from authorizeRequest(for example: when trying to
			// get access token after user sign up)
			if ((username = authorizeRequest.getAttribute(OAuth2ParameterNames.USERNAME)) == null
					|| (password = authorizeRequest.getAttribute(OAuth2ParameterNames.PASSWORD)) == null) {

				// get username and password from httpRequest(for example: when trying to login
				// user)
				HttpServletRequest servletRequest = authorizeRequest.getAttribute(HttpServletRequest.class.getName());

				username = servletRequest.getParameter(OAuth2ParameterNames.USERNAME);
				password = servletRequest.getParameter(OAuth2ParameterNames.PASSWORD);

			}
			if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
				contextAttributes = new HashMap<>();
				// `PasswordOAuth2AuthorizedClientProvider` requires both attributes
				contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
				contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
			}
			return contextAttributes;
		};
	}
}
