package com.excentria_it.wamya.adapter.b2b.rest;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest(properties = { "eureka.client.enabled=false" })
@ActiveProfiles(value = { "b2b-rest-local" })
public class B2bRestModuleTest {
	@Autowired
	private WebClient.Builder webClientBuilder;
	@Autowired
	private OAuth2AuthorizedClientManager authorizedClientManager;

	@Test
	void testConfiguration() {
		assertThat(webClientBuilder).isNotNull();
		assertThat(authorizedClientManager).isNotNull();
	}
}
