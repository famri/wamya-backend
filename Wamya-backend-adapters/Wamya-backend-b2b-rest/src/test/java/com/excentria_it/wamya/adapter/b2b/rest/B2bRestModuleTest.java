package com.excentria_it.wamya.adapter.b2b.rest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(properties = { "eureka.client.enabled=false" })
@ActiveProfiles(value = { "b2b-rest-local" })
public class B2bRestModuleTest {
	@Autowired
	@Qualifier("no-oauth-web-client")
	private WebClient.Builder webClientBuilder;

	@Autowired
	@Qualifier("client-credentials-web-client")
	private WebClient.Builder clientCredentialsWebClientBuilder;

	@Autowired
	@Qualifier("password-web-client")
	private WebClient.Builder passwordWebClientBuilder;

	@Autowired
	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager;

	@Test
	void testConfiguration() {
		assertThat(webClientBuilder).isNotNull();
		assertThat(clientCredentialsWebClientBuilder).isNotNull();
		assertThat(passwordWebClientBuilder).isNotNull();
		assertThat(authorizedClientManager).isNotNull();
	}

	//@Test
	void testContextAttributeMapper() {

		OAuth2AuthorizeRequest authorizeRequest = Mockito.mock(OAuth2AuthorizeRequest.class);
		when(authorizeRequest.getAttribute(OAuth2ParameterNames.USERNAME)).thenReturn(null);
		when(authorizeRequest.getAttribute(OAuth2ParameterNames.PASSWORD)).thenReturn(null);

		authorizedClientManager.authorize(authorizeRequest);

	}
}
