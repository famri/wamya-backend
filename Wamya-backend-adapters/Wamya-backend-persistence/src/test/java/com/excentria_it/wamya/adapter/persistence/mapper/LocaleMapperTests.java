package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.LocaleJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;
import com.excentria_it.wamya.domain.LoadLocalesDto;

public class LocaleMapperTests {

	private LocaleMapper localeMapper = new LocaleMapper();

	@Test
	void testMapToDomainEntity() {
		LocaleJpaEntity l = defaultLocaleJpaEntity();
		LoadLocalesDto localeDto = localeMapper.mapToDomainEntity(l);
		assertEquals(l.getId(), localeDto.getId());
		assertEquals(l.getName(), localeDto.getName());
		assertEquals(l.getCountryCode(), localeDto.getCountryCode());
		assertEquals(l.getLanguageCode(), localeDto.getLanguageCode());
	}

	@Test
	void testMapToDomainEntityFromNullEntity() {
		LoadLocalesDto localeDto = localeMapper.mapToDomainEntity(null);
		assertNull(localeDto);
	}
}
