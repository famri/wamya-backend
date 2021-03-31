package com.excentria_it.wamya.adapter.messaging.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Generated
public class WebSocketEventListener {

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");

		if (username != null) {
			log.info("Web socket connected: " + username);
		} else {
			log.info("Web socket connected: " + "unknown user");
		}
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) headerAccessor.getSessionAttributes().get("username");
		if (username != null) {
			log.info("Web socket disconnected: " + username);
		} else {
			log.info("Web socket disconnected: " + "unknown user");
		}
	}
}