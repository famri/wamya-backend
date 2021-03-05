package com.excentria_it.wamya.adapter.persistence.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserPreference;

public class UserPreferenceMapperTests {
	private UserPreferenceMapper userPreferenceMapper = new UserPreferenceMapper();

	@Test
	void testMapToDomainEntityWithNullJpaEntity() {
		assertNull(userPreferenceMapper.mapToDomainEntity(null));
	}

	@Test
	void testMapToDomainEntity() {
		UserPreferenceJpaEntity jpaEntity = new UserPreferenceJpaEntity("timezone", "Africa/Tunis");
		jpaEntity.setId(1L);

		UserPreference userPreference = userPreferenceMapper.mapToDomainEntity(jpaEntity);

		assertEquals(jpaEntity.getId(), userPreference.getId());
		assertEquals(jpaEntity.getKey(), userPreference.getKey());
		assertEquals(jpaEntity.getValue(), userPreference.getValue());
	}
}
