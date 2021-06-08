package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.constraints.NotEmpty;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.ErrorMessagesPropertiesNames;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RequiredArgsConstructor
@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/accounts")
public class PasswordResetWebController {

	private final ResetPasswordUseCase resetPasswordUseCase;

	private final MessageSource messageSource;

	@GetMapping(path = "/password-reset")
	@ResponseStatus(HttpStatus.OK)
	public String showPasswordResetForm(@NotEmpty @RequestParam(name = "uuid") String uuid,
			@NotEmpty @RequestParam(name = "exp") Long expiry, Locale locale, Model model) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		if (resetPasswordUseCase.checkRequest(uuid, expiry)) {
			model.addAttribute("uuid", uuid);
			model.addAttribute("exp", expiry);
			model.addAttribute("lang", supportedLocale.toString());
			return "password-reset";
		} else {
			String errorMessage = messageSource.getMessage(ErrorMessagesPropertiesNames.PASSWORED_RESET_LINK_EXPIRED,
					null, supportedLocale);
			model.addAttribute("error", errorMessage);
			return "error";
		}

	}

}
