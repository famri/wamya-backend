package com.excentria_it.wamya.adapter.messaging;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	private final RabbitProperties rabbitProperties;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//TODO update cors config for prod
		registry.addEndpoint("/wamya-ws").setAllowedOrigins("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {

		registry.enableStompBrokerRelay("/queue").setRelayHost(rabbitProperties.getHost())
				.setRelayPort(61613).setClientLogin(rabbitProperties.getUsername())
				.setClientPasscode(rabbitProperties.getPassword());
		registry.setApplicationDestinationPrefixes("/app");
	}

}
