package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.domain.LoadCountriesDto;

@Component
public class CountryMapper {
	public LoadCountriesDto mapToDomainEntity(CountryJpaEntity countryJpaEntity, String locale) {
		if (countryJpaEntity == null || locale == null || locale.isEmpty()) {
			return null;
		}
		List<LoadCountriesDto.TimeZoneDto> timeZoneDtos = countryJpaEntity.getTimeZones().stream()
				.map(t -> new LoadCountriesDto.TimeZoneDto(t.getId(), t.getName(), t.getGmtOffset()))
				.collect(Collectors.toList());

		return new LoadCountriesDto(countryJpaEntity.getId(), countryJpaEntity.getName(locale),
				countryJpaEntity.getCode(), countryJpaEntity.getFlagPath(),
				new LoadCountriesDto.IccDto(countryJpaEntity.getIcc().getId(), countryJpaEntity.getIcc().getValue()),
				timeZoneDtos);

	}
}
