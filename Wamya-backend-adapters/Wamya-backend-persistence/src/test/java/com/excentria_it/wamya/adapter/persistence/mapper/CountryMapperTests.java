package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.CountryJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.domain.LoadCountriesDto;

public class CountryMapperTests {
	private CountryMapper countryMapper = new CountryMapper();

	@Test
	void testMapToDomainEntity() {

		CountryJpaEntity c = defaultCountryJpaEntity();
		LoadCountriesDto countryDto = countryMapper.mapToDomainEntity(c, "fr_FR");
		assertEquals(c.getId(), countryDto.getId());
		assertEquals(c.getCode(), countryDto.getCode());
		assertEquals(c.getFlagPath(), countryDto.getFlagPath());

		assertEquals(c.getName("fr_FR"), countryDto.getName());
		assertEquals(c.getIcc().getId(), countryDto.getIcc().getId());
		assertEquals(c.getIcc().getValue(), countryDto.getIcc().getValue());

		assertEquals(c.getTimeZones().size(), countryDto.getTimeZones().size());
		for (int i = 0; i < c.getTimeZones().size(); i++) {
			assertTrue(c.getTimeZones().get(i).getId().equals(countryDto.getTimeZones().get(i).getId()));
			assertTrue(c.getTimeZones().get(i).getName().equals(countryDto.getTimeZones().get(i).getName()));
			assertTrue(c.getTimeZones().get(i).getGmtOffset().equals(countryDto.getTimeZones().get(i).getGmtOffset()));

		}

	}

	@Test
	void testMapToDomainEntityFromNullJpaEntity() {

		LoadCountriesDto countryDto = countryMapper.mapToDomainEntity(null, "fr_FR");

		assertNull(countryDto);
	}

	@Test
	void testMapToDomainEntityWithNullLocale() {
		CountryJpaEntity c = defaultCountryJpaEntity();
		LoadCountriesDto countryDto = countryMapper.mapToDomainEntity(c, null);

		assertNull(countryDto);
	}

	@Test
	void testMapToDomainEntityWithEmptyLocale() {
		CountryJpaEntity c = defaultCountryJpaEntity();
		LoadCountriesDto countryDto = countryMapper.mapToDomainEntity(c, "");

		assertNull(countryDto);
	}
}
