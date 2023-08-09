package com.excentria_it.wamya.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForClientUseCase;
import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchDelegationPort;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.CountryDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AutoCompletePlaceForClientService implements AutoCompletePlaceForClientUseCase {

	private final SearchLocalityPort searchLocalityPort;
	private final SearchDelegationPort searchDelegationPort;
	private final SearchDepartmentPort searchDepartmentPort;
	private final LoadCountryPort loadCountryPort;

	@Override
	public List<AutoCompletePlaceDto> autoCompletePlace(String input, String countryCode, Integer limit,
			String locale) {

		if (StringUtils.isEmpty(input)) {
			return Collections.<AutoCompletePlaceDto>emptyList();
		}

		Optional<CountryDto> countryDtoOptional = loadCountryPort.loadCountryByCodeAndLocale(countryCode.trim(),
				locale);
		if (countryDtoOptional.isEmpty()) {
			throw new CountryNotFoundException(String.format("County not found by code: %s", countryCode));
		}
		CountryDto countryDto = countryDtoOptional.get();

		input = input.trim();

		if (input.contains(",")) {
			String[] tokens = input.split(",");

			for (String token : tokens) {
				if (StringUtils.isEmpty(token.trim())) {
					return Collections.<AutoCompletePlaceDto>emptyList();
				}

			}

			if (tokens.length == 2) {

				// ONE COMMA ==> TWO tokens; this can be a search for locality, delegation OR
				// locality, department OR delegation, department

				List<AutoCompleteLocalityDto> localitiesAndDelegations = searchLocalityPort
						.searchLocalityByNameAndDelegationName(tokens[0].trim(), tokens[1].trim(), countryDto.getId(),
								limit, locale);
				List<AutoCompleteLocalityDto> localitiesAndDepartments = searchLocalityPort
						.searchLocalityByNameAndDepartmentName(tokens[0].trim(), tokens[1].trim(), countryDto.getId(),
								limit, locale);
				List<AutoCompleteDelegationDto> delegationsAndDepartments = searchDelegationPort
						.searchDelegationByNameAndDepartmentName(tokens[0].trim(), tokens[1].trim(), countryDto.getId(),
								limit, locale);

				List<AutoCompletePlaceDto> result = new ArrayList<>(delegationsAndDepartments.size()
						+ localitiesAndDelegations.size() + localitiesAndDepartments.size());

				result.addAll(delegationsAndDepartments.stream()
						.map(del -> AutoCompletePlaceMapper.mapDelegationToPlace(del)).collect(Collectors.toList()));

				result.addAll(localitiesAndDelegations.stream().map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l))
						.collect(Collectors.toList()));

				result.addAll(localitiesAndDepartments.stream().map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l))
						.collect(Collectors.toList()));

				return result;

			} else if (tokens.length == 3) {
				// TWO COMMA ==> THREE tokens; this can be a search for locality, delegation,
				// department

				List<AutoCompleteLocalityDto> localitiesAndDelegationsAndDepartments = searchLocalityPort
						.searchLocalityByNameAndDelegationNameAndDepartmentName(tokens[0].trim(), tokens[1].trim(),
								tokens[2].trim(), countryDto.getId(), limit, locale);
				return localitiesAndDelegationsAndDepartments.stream()
						.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());
			} else {
				return Collections.<AutoCompletePlaceDto>emptyList();
			}
		} else {

			List<AutoCompleteDepartmentDto> departments = searchDepartmentPort.searchDepartmentByName(input,
					countryDto.getId(), limit, locale);

			List<AutoCompleteDelegationDto> delegations = searchDelegationPort.searchDelegationByName(input,
					countryDto.getId(), limit, locale);

			List<AutoCompleteLocalityDto> localities = searchLocalityPort.searchLocalityByName(input,
					countryDto.getId(), limit, locale);

			List<AutoCompletePlaceDto> result = new ArrayList<>(
					departments.size() + delegations.size() + localities.size());

			result.addAll(departments.stream().map(dep -> AutoCompletePlaceMapper.mapDepartmentToPlace(dep))
					.collect(Collectors.toList()));
			result.addAll(delegations.stream().map(del -> AutoCompletePlaceMapper.mapDelegationToPlace(del))
					.collect(Collectors.toList()));
			result.addAll(localities.stream().map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l))
					.collect(Collectors.toList()));

			return result;
		}

	}

}
