package com.excentria_it.wamya.adapter.web;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CreateUserAccountController {

	private final CreateUserAccountUseCase createUserAccountUseCase;

	@PostMapping(path = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void createUserAccount(@Valid @RequestBody CreateUserAccountCommand command, Locale locale) {

		createUserAccountUseCase.registerUserAccountCreationDemand(command, locale);
	}

	@ExceptionHandler({ UserAccountAlreadyExistsException.class })
	public void handleUserAccountAlreadyExistsException(UserAccountAlreadyExistsException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}

	@ExceptionHandler({ UnsupportedInternationalCallingCode.class })
	public void handleUnsupportedInternationalCallingCode(UnsupportedInternationalCallingCode exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}

}
