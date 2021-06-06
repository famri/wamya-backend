package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.common.annotation.ViewMessageSource;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.ErrorMessagesPropertiesNames;

@WebAdapter
@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/accounts")
public class PasswordResetController {

	@Autowired
	private RequestPasswordResetUseCase requestPasswordResetUseCase;

	@Autowired
	private ResetPasswordUseCase resetPasswordUseCase;

	@Autowired
	@ViewMessageSource
	private MessageSource messageSource;

	@PostMapping(path = "/do-request-password-reset", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void requestPasswordReset(@NotEmpty @RequestParam(name = "username") String username, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);
		requestPasswordResetUseCase.requestPasswordReset(username, supportedLocale);

	}

	@GetMapping(path = "/password-reset")
	@ResponseStatus(HttpStatus.OK)
	public String showPasswordResetForm(@NotEmpty @RequestParam(name = "uuid") String uuid,
			@NotEmpty @RequestParam(name = "exp") Long expiry, Locale locale, Model model) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		if (resetPasswordUseCase.checkRequest(uuid, expiry)) {
			model.addAttribute("uuid", uuid);
			model.addAttribute("exp", expiry);
			return "password-reset";
		} else {
			String errorMessage = messageSource.getMessage(ErrorMessagesPropertiesNames.PASSWORED_RESET_LINK_EXPIRED,
					null, supportedLocale);
			model.addAttribute("error", errorMessage);
			return "error";
		}

	}

	@PostMapping(path = "/password-reset")
	@ResponseStatus(HttpStatus.OK)
	public String resetPassword(@NotEmpty @RequestParam(name = "password") String password,
			@NotEmpty @RequestParam(name = "uuid") String uuid, @NotEmpty @RequestParam(name = "exp") Long expiry,
			Locale locale, Model model) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		if (!resetPasswordUseCase.checkRequest(uuid, expiry) || !resetPasswordUseCase.resetPassword(uuid, password)) {

			String errorMessage = messageSource.getMessage(ErrorMessagesPropertiesNames.PASSWORED_RESET_LINK_EXPIRED,
					null, supportedLocale);
			model.addAttribute("error", errorMessage);
			return "error";
		} else {
			return "password-reset-ok";
		}

	}
}
