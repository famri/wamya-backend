package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserPreference;

@Component
public class UserPreferenceMapper {
	public UserPreference mapToDomainEntity(UserPreferenceJpaEntity userPreferenceJpaEntity) {
		if (userPreferenceJpaEntity == null)
			return null;

		return new UserPreference(userPreferenceJpaEntity.getId(), userPreferenceJpaEntity.getKey(),
				userPreferenceJpaEntity.getValue());
	}
}
