package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;
import com.excentria_it.wamya.domain.LoadLocalesDto;

@Component
public class LocaleMapper {
	public LoadLocalesDto mapToDomainEntity(LocaleJpaEntity localeJpaEntity) {
		if (localeJpaEntity == null) {
			return null;
		}

		return new LoadLocalesDto(localeJpaEntity.getId(), localeJpaEntity.getName(), localeJpaEntity.getCountryCode(),
				localeJpaEntity.getLanguageCode());
	}
}
