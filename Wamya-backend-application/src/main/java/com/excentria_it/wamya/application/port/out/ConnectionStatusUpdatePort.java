package com.excentria_it.wamya.application.port.out;

public interface ConnectionStatusUpdatePort {
	void updateConnectionStatus(String username, Boolean isConnected);
}
