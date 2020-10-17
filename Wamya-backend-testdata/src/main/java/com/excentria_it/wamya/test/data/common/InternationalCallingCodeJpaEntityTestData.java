package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;

public class InternationalCallingCodeJpaEntityTestData {

	public static final InternationalCallingCodeJpaEntity defaultInternationalCallingCodeJpaEntity() {
		return InternationalCallingCodeJpaEntity.builder().id(1L).value("+216").countryName("Tunisia")
				.flagPath("path/to/country/flag").enabled(true).build();
	}

}
