package com.excentria_it.wamya.adapter.web;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.exception.ApiError;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CreateUserAccountController {

	private final CreateUserAccountUseCase createUserAccountUseCase;

	@PostMapping(path = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public UserAccount createUserAccount(@Valid @RequestBody CreateUserAccountCommand command, Locale locale) {

		Long userAccountId = createUserAccountUseCase.registerUserAccountCreationDemand(command, locale);

		return UserAccount.builder().id(userAccountId).isTransporter(command.getIsTransporter())
				.gender(command.getGender()).firstName(command.getFirstName()).lastName(command.getLastName())
				.dateOfBirth(command.getDateOfBirth()).email(command.getEmail()).emailValidationCode("****")
				.isValidatedEmail(false)
				.mobilePhoneNumber(new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()))
				.mobileNumberValidationCode("****").isValidatedMobileNumber(false).userPassword("********")
				.receiveNewsletter(command.getReceiveNewsletter()).receiveNewsletter(command.getReceiveNewsletter())
				.creationTimestamp(null).build();
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
