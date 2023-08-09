package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;

public class LocaleJpaTestData {
	public static List<LocaleJpaEntity> defaultLocaleJpaEntities() {
		return List.of(new LocaleJpaEntity(1L, "English", "US", "en"), new LocaleJpaEntity(2L, "Français", "FR", "fr"));
	}

	public static LocaleJpaEntity defaultLocaleJpaEntity() {
		return new LocaleJpaEntity(1L, "English", "US", "en");
	}
}
