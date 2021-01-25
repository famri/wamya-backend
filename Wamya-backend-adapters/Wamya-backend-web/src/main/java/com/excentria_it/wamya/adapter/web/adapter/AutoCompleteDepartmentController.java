package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AutoCompleteDepartmentUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@RequestMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
public class AutoCompleteDepartmentController {

	private final AutoCompleteDepartmentUseCase autoCompleteDepartmentUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public AutoCompleteDepartmentsResult autoCompleteDepartment(
			@NotEmpty @RequestParam(name = "country") String countryCode,
			@Size(min = 3) @RequestParam(name = "input") String input, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<AutoCompleteDepartmentsDto> autoCompleteDepartmentDtos = autoCompleteDepartmentUseCase
				.autoCompleteDepartment(input, countryCode, supportedLocale.toString());

		if (autoCompleteDepartmentDtos == null || autoCompleteDepartmentDtos.isEmpty()) {
			return new AutoCompleteDepartmentsResult(0, Collections.<AutoCompleteDepartmentsDto>emptyList());
		}
		return new AutoCompleteDepartmentsResult(autoCompleteDepartmentDtos.size(), autoCompleteDepartmentDtos);

	}
}
