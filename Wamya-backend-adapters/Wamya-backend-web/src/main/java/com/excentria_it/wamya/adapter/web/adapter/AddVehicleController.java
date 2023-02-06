package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AddVehiculeUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.AddVehiculeDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddVehicleController {

	private final AddVehiculeUseCase addVehiculeUseCase;

	@PostMapping(path = "/me/vehicles", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public AddVehiculeDto addTransporterVehicle(@Valid @RequestBody AddVehiculeUseCase.AddVehicleCommand command,
												final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return addVehiculeUseCase.addVehicle(command, principal.getName(), supportedLocale.toString());

	}
}
