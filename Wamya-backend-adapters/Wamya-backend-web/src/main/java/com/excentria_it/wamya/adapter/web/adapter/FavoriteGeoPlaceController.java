package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase.CreateFavoriteGeoPlaceCommand;
import com.excentria_it.wamya.application.port.in.LoadFavoriteGeoPlacesUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/geo-places", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteGeoPlaceController {
	private final CreateFavoriteGeoPlaceUseCase createFavoriteGeoPlaceUseCase;

	private final LoadFavoriteGeoPlacesUseCase loadFavoriteGeoPlaceUseCase;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public GeoPlaceDto createFavoriteGeoPlace(@Valid @RequestBody CreateFavoriteGeoPlaceCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return createFavoriteGeoPlaceUseCase.createFavoriteGeoPlace(command.getName(), command.getLatitude(),
				command.getLongitude(), principal.getName(), supportedLocale.toString());

	}

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public UserFavoriteGeoPlaces loadFavoriteGeoPlaces(final @AuthenticationPrincipal JwtAuthenticationToken principal,
			Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadFavoriteGeoPlaceUseCase.loadFavoriteGeoPlaces(principal.getName(), supportedLocale.toString());

	}
}
