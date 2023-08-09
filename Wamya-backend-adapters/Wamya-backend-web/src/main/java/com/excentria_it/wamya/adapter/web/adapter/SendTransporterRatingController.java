package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Locale;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase;
import com.excentria_it.wamya.application.port.in.SendTransporterRatingUseCase.SendTransporterRatingCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
public class SendTransporterRatingController {

	private final SendTransporterRatingUseCase sendTransporterRatingUseCase;

	@PostMapping(path = "/ratings", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void sendTransporterRatingRequest(@Valid @RequestBody SendTransporterRatingCommand command, Locale locale) {

		sendTransporterRatingUseCase.saveTransporterRating(command, locale.toString());

	}
}
