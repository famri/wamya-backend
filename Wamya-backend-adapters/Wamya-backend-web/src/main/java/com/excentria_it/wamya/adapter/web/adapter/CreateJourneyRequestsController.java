package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreateJourneyRequestsController {

	private final CreateJourneyRequestUseCase createJourneyRequestUseCase;



	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public CreateJourneyRequestDto createJourneyRequest(@Valid @RequestBody CreateJourneyRequestCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		CreateJourneyRequestDto journeyRequest = createJourneyRequestUseCase.createJourneyRequest(command,
				principal.getName());

		return journeyRequest;
	}



}
