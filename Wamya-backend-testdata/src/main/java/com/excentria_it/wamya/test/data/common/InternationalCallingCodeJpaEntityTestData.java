package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;

public class InternationalCallingCodeJpaEntityTestData {

	public static final InternationalCallingCodeJpaEntity defaultNewInternationalCallingCodeJpaEntity() {
		return InternationalCallingCodeJpaEntity.builder().id(null)
				.value(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE)
				.enabled(true).build();

	}

	public static final InternationalCallingCodeJpaEntity defaultExistentInternationalCallingCodeJpaEntity() {
		return InternationalCallingCodeJpaEntity.builder().id(1L)
				.value(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE)
				.enabled(true).build();

	}

}
