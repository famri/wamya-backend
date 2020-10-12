package com.excentria_it.wamya.adapter.web;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequest;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequestStatus;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationCodeCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = { "/validation-codes" })
public class SendValidationCodeController {

	private final SendValidationCodeUseCase sendValidationCodeUseCase;

	@PostMapping(path = "/sms/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ValidationCodeRequest sendSMSValidationCode(@Valid @RequestBody SendSMSValidationCodeCommand command,
			Locale locale) {

		boolean success = sendValidationCodeUseCase.sendSMSValidationCode(command, locale);

		ValidationCodeRequest result = new ValidationCodeRequest();

		if (success) {
			result.setStatus(ValidationCodeRequestStatus.REGISTRED);
		} else {
			result.setStatus(ValidationCodeRequestStatus.REJECTED);
		}
		return result;

	}

	@PostMapping(path = "/email/send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ValidationCodeRequest sendEmailValidationCode(@Valid @RequestBody SendEmailValidationCodeCommand command,
			Locale locale) {

		boolean success = sendValidationCodeUseCase.sendEmailValidationCode(command, locale);

		ValidationCodeRequest result = new ValidationCodeRequest();

		if (success) {
			result.setStatus(ValidationCodeRequestStatus.REGISTRED);
		} else {
			result.setStatus(ValidationCodeRequestStatus.REJECTED);
		}
		return result;

	}

	@ExceptionHandler({ UserAccountNotFoundException.class })
	public void handleUserAccountNotFoundException(UserAccountNotFoundException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}

	@ExceptionHandler({ UserMobileNumberValidationException.class })
	public void handleUserMobileNumberValidationException(UserMobileNumberValidationException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}

	@ExceptionHandler({ UserEmailValidationException.class })
	public void handleUserEmailValidationException(UserEmailValidationException exception) {
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
	}

}
