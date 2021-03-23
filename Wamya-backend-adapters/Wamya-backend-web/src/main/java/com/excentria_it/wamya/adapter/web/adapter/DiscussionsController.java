package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase;
import com.excentria_it.wamya.application.port.in.LoadDiscussionsUseCase.LoadDiscussionsCommand;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadDiscussionsResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiscussionsController {

	private final LoadDiscussionsUseCase loadDiscussionsUseCase;

	@GetMapping(path = "/me/discussions")
	@ResponseStatus(HttpStatus.OK)
	public LoadDiscussionsResult loadDiscussions(@RequestParam(name = "page", defaultValue = "0") Integer pageNumber,
			@RequestParam(name = "size", defaultValue = "25") Integer pageSize,
			@RequestParam(name = "sort") Optional<String> sort, @RequestParam(name = "filter") Optional<String> filter,
			final @AuthenticationPrincipal JwtAuthenticationToken principal) {

		SortCriterion sortingCriterion = ParameterUtils.parameterToSortCriterion(sort, "date-time,desc");
		FilterCriterion filteringCriterion = ParameterUtils.parameterToFilterCriterion(filter);

		LoadDiscussionsCommand command = LoadDiscussionsCommand.builder().username(principal.getName())
				.pageNumber(pageNumber).pageSize(pageSize).sortingCriterion(sortingCriterion)
				.filteringCriterion(filteringCriterion).build();

		return loadDiscussionsUseCase.loadDiscussions(command);

	}

}
