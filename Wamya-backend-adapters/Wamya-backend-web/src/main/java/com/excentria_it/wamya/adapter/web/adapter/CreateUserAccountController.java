package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.exception.ApiError;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
@RequestMapping(path = "/wamya-backend")
public class CreateUserAccountController {

	private final CreateUserAccountUseCase createUserAccountUseCase;

	@PostMapping(path = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public OAuth2AccessToken createUserAccount(@Valid @RequestBody CreateUserAccountCommand command, Locale locale) {

		OAuth2AccessToken accessToken = createUserAccountUseCase.registerUserAccountCreationDemand(command, locale);

		// Authenticate user and return access token

		return accessToken;
	}

	@ExceptionHandler({ UserAccountAlreadyExistsException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleUserAccountAlreadyExistsException(UserAccountAlreadyExistsException exception) {

		log.error("Exception at " + exception.getClass() + ": ", exception);
		final String error = "User account already exists.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ UnsupportedInternationalCallingCode.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleUnsupportedInternationalCallingCode(
			UnsupportedInternationalCallingCode exception) {

		log.error("Exception at " + exception.getClass() + ": ", exception);
		final String error = "International calling code is not supported.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

	}

}
