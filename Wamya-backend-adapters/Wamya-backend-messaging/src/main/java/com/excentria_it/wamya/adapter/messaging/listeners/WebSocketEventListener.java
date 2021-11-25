package com.excentria_it.wamya.adapter.messaging.listeners;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase;
import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase.RegisterConnectionStatusCommand;
import com.excentria_it.wamya.common.annotation.Generated;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@Generated
public class WebSocketEventListener {

	private final RegisterWebsocketConnectionStatusUseCase registerWebsocketConnectionStatusUseCase;

	@EventListener
	@Async
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//		String username = (String) headerAccessor.getUser().getName();

		MessageHeaders headers = event.getMessage().getHeaders();
		Principal principal = SimpMessageHeaderAccessor.getUser(headers);
		if (principal == null) {
			log.info("Web socket connected: " + "unknown user");
			return;
		}

		RegisterConnectionStatusCommand command = RegisterConnectionStatusCommand.builder().connected(true)
				.username(principal.getName()).build();
		registerWebsocketConnectionStatusUseCase.registerConnectionStatus(command);

		log.info("Web socket connected: " + principal.getName());

	}

	@EventListener
	@Async
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//		String username = (String) headerAccessor.getUser().getName();
		MessageHeaders headers = event.getMessage().getHeaders();
		Principal principal = SimpMessageHeaderAccessor.getUser(headers);
		if (principal == null) {
			log.info("Web socket disconnected: " + "unknown user");
			return;
		}
		RegisterConnectionStatusCommand command = RegisterConnectionStatusCommand.builder().connected(false)
				.username(principal.getName()).build();
		registerWebsocketConnectionStatusUseCase.registerConnectionStatus(command);

		log.info("Web socket disconnected: " + principal.getName());
	}
}