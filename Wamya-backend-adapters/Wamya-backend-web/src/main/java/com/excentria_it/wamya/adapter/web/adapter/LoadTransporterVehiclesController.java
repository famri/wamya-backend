package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase;
import com.excentria_it.wamya.application.port.in.LoadVehiculesUseCase.LoadVehiculesCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.TransporterVehicles;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoadTransporterVehiclesController {

	private final LoadVehiculesUseCase loadVehiculesUseCase;

	@GetMapping(path = "/me/vehicles")
	@ResponseStatus(HttpStatus.OK)
	public TransporterVehicles loadTransporterVehicules(

			@RequestParam(name = "sort") Optional<String> sort,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "id,asc");

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		LoadVehiculesCommand command = LoadVehiculesCommand.builder().transporterUsername(principal.getName())
				.sortingCriterion(sortingCriterion).build();

		return loadVehiculesUseCase.loadTransporterVehicules(command, supportedLocale.toString());

	}
}
