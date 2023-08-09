package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequest;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequestStatus;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationLinkCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/validation-codes", produces = MediaType.APPLICATION_JSON_VALUE)

public class SendValidationCodeController {

	private final SendValidationCodeUseCase sendValidationCodeUseCase;

	@PostMapping(path = "/sms/send", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ValidationCodeRequest sendSMSValidationCode(@Valid @RequestBody SendSMSValidationCodeCommand command,
			Locale locale) {

		boolean success = sendValidationCodeUseCase.sendSMSValidationCode(command,
				LocaleUtils.getSupporedLocale(locale));

		ValidationCodeRequest result = new ValidationCodeRequest();

		if (success) {
			result.setStatus(ValidationCodeRequestStatus.REGISTRED);
		} else {
			result.setStatus(ValidationCodeRequestStatus.REJECTED);
		}
		return result;

	}

	@PostMapping(path = "/email/send", consumes = MediaType.APPLICATION_JSON_VALUE)
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

}
