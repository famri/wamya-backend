package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;

public class ConstructorJpaTestData {

	public static ConstructorJpaEntity defaultConstructorJpaEntity() {

		return ConstructorJpaEntity.builder().id(1L).name("Constructor1").build();
	}
}
