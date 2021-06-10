package com.excentria_it.wamya.adapter.web.adapter;

import java.io.IOException;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadProfileInfoUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.ProfileInfoDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/profiles/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadProfileInfoController {

	private final LoadProfileInfoUseCase loadProfileInfoUseCase;

	@GetMapping(path = "/info")
	@ResponseStatus(HttpStatus.OK)
	public ProfileInfoDto loadProfileInfo(final @AuthenticationPrincipal JwtAuthenticationToken principal,
			Locale locale) throws IOException {
		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadProfileInfoUseCase.loadProfileInfo(principal.getName(), supportedLocale.toString());

	}

}
