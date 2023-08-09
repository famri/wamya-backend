package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity.ModelJpaEntityBuilder;
public class ModelJpaTestData {

	public static ModelJpaEntityBuilder defaultModelJpaEntityBuilder() {
		return ModelJpaEntity.builder().id(1L).name("Model1");
	}
}
