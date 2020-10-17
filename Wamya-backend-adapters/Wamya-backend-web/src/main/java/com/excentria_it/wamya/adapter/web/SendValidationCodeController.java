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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequest;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequestStatus;
import com.excentria_it.wamya.adapter.web.exception.ApiError;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationLinkCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = { "/validation-codes" })
@Slf4j
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
	public ValidationCodeRequest sendEmailValidationLink(@Valid @RequestBody SendEmailValidationLinkCommand command,
			Locale locale) {

		boolean success = sendValidationCodeUseCase.sendEmailValidationLink(command, locale);

		ValidationCodeRequest result = new ValidationCodeRequest();

		if (success) {
			result.setStatus(ValidationCodeRequestStatus.REGISTRED);
		} else {
			result.setStatus(ValidationCodeRequestStatus.REJECTED);
		}
		return result;

	}

	@ExceptionHandler({ UserAccountNotFoundException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleUserAccountNotFoundException(UserAccountNotFoundException exception) {
		log.error("Exception at " + exception.getClass() + ": ", exception);
		final String error = "User account not found.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ UserMobileNumberValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleUserMobileNumberValidationException(
			UserMobileNumberValidationException exception) {
		log.error("Exception at " + exception.getClass() + ": ", exception);
		final String error = "User mobile number already validated.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ UserEmailValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleUserEmailValidationException(UserEmailValidationException exception) {
		log.error("Exception at " + exception.getClass() + ": ", exception);
		final String error = "User email already validated.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

}
