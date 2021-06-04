package com.excentria_it.wamya.application.port.in;

public interface ResetPasswordUseCase {

	boolean checkRequest(String uuid, Long expiry);


	boolean resetPassword(String uuid, Long expiry, String password);

}
