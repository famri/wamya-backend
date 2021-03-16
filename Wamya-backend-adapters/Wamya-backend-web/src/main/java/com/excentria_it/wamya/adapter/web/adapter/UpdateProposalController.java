package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase;
import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase.UpdateProposalCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.JourneyProposalDto.StatusCode;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/journey-requests", produces = MediaType.APPLICATION_JSON_VALUE)
public class UpdateProposalController {

	private final UpdateProposalUseCase updateProposalUseCase;

	@PatchMapping(path = "/{journeyRequestId}/proposals/{proposalId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void acceptProposal(@PathVariable("journeyRequestId") Long journeyRequestId,
			@PathVariable("proposalId") Long proposalId, @Valid @RequestBody UpdateProposalCommand command,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		StatusCode status = StatusCode.valueOf(command.getStatus().toUpperCase());

		updateProposalUseCase.updateProposal(journeyRequestId, proposalId, status, principal.getName());

	}

}
