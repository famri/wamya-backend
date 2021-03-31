package com.excentria_it.wamya.adapter.messaging;

import static org.springframework.messaging.simp.SimpMessageType.*;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages.nullDestMatcher().authenticated().simpDestMatchers("/app/**")
				.hasAnyAuthority("SCOPE_journey:write", "SCOPE_offer:write")
				.simpSubscribeDestMatchers("/user/queue/messages")
				.hasAnyAuthority("SCOPE_journey:write", "SCOPE_offer:write").simpTypeMatchers(MESSAGE, SUBSCRIBE)
				.denyAll().anyMessage().denyAll();

	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}
}
