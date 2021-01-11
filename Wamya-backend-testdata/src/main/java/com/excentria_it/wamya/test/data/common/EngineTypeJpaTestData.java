package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;

public class EngineTypeJpaTestData {
	public static EngineTypeJpaEntity defaultEngineTypeJpaEntity() {
		EngineTypeJpaEntity et11 = EngineTypeJpaEntity.builder().id(1L).build();
		et11.setCode(EngineTypeCode.VAN_L1H1);
		LocalizedEngineTypeJpaEntity let11en = new LocalizedEngineTypeJpaEntity();
		let11en.setLocalizedId(new LocalizedId("en_US"));
		let11en.setEngineType(et11);
		let11en.setName("EngineType11");
		let11en.setDescription("EngineTypeDescription11");
		et11.getLocalizations().put("en_US", let11en);

		return et11;
	}
}
