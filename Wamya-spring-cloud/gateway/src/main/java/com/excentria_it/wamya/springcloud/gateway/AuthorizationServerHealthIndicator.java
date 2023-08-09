package com.excentria_it.wamya.springcloud.gateway;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServerHealthIndicator implements ReactiveHealthIndicator {

	private static final String SERVICE_BASE_URL = "http://auth-server";

	private final WebClient.Builder webClientBuilder;

	private WebClient webClient;

	@Override
	public Mono<Health> health() {
		String url = SERVICE_BASE_URL + "/actuator/health";
		log.debug("Will call the Health API on URL: {}", url);
		return getWebClient().get().uri(url).retrieve().bodyToMono(String.class)
				.map(s -> new Health.Builder().up().build())
				.onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build())).log();
	}

	private WebClient getWebClient() {
		if (webClient == null) {
			webClient = webClientBuilder.build();
		}
		return webClient;
	}
}
