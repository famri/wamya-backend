package com.excentria_it.wamya.test.data.common;

import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;

public class UserPreferenceJpaTestData {
	public static Set<UserPreferenceJpaEntity> defaultUserPreferenceJpaEntities() {
		UserPreferenceJpaEntity entity = new UserPreferenceJpaEntity("timezone", "Africa/Tunis");
		entity.setId(1L);
		return Set.of(entity);
	}

	public static UserPreferenceJpaEntity defaultUserTimeZonePreferenceJpaEntity() {
		UserPreferenceJpaEntity entity = new UserPreferenceJpaEntity("timezone", "Africa/Tunis");
		entity.setId(1L);
		return entity;
	}
}