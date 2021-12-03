package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadTransporterRatingDetailsUseCase;
import com.excentria_it.wamya.application.port.in.LoadTransporterRatingDetailsUseCase.LoadTransporterRatingRequestCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor

public class LoadTransporterRatingRequestController {

	private final LoadTransporterRatingDetailsUseCase loadTransporterRatingDetailsUseCase;

	@GetMapping(path = "/rating-details", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public TransporterRatingRequestRecordDto loadTransporterRatingDetails(@RequestParam(name = "h") String hash,
			@RequestParam(name = "uid") Long userId, Locale locale) {

		LoadTransporterRatingRequestCommand command = LoadTransporterRatingRequestCommand.builder().hash(hash).userId(userId).build();

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadTransporterRatingDetailsUseCase.loadTransporterRatingDetails(command, supportedLocale.toString());

	}
}
