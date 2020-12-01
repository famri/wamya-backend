package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;

public class InternationalCallingCodeJpaEntityTestData {

	public static final InternationalCallingCodeJpaEntity defaultInternationalCallingCodeJpaEntity() {
		return InternationalCallingCodeJpaEntity.builder().id(1L)
				.value(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).countryName(TestConstants.DEFAULT_COUNTRY_NAME)
				.flagPath(TestConstants.DEFAULT_FLAG_PATH).enabled(true).build();

	}

}
