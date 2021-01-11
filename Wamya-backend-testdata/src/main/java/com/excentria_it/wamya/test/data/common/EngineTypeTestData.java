package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.domain.EngineTypeDto;

public class EngineTypeTestData {
	public static EngineTypeDto defaultEngineTypeDto() {
		return new EngineTypeDto() {

			@Override
			public Long getId() {
				return 1L;
			}

			@Override
			public String getCode() {

				return EngineTypeCode.UTILITY.name();
			}

			@Override
			public String getName() {

				return "utility vehicule";
			}

			@Override
			public String getDescription() {

				return "utility vehicule description";
			}

		};
	}
}
