package com.excentria_it.wamya.test.data.common;

import java.time.LocalDate;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity.EngineTypeCode;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity.VehiculeJpaEntityBuilder;

public class VehiculeJpaEntityTestData {

	public static final VehiculeJpaEntity vehiculeJpaEntity1 = VehiculeJpaEntity.builder().id(1L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1).build())
			.model(ModelJpaEntity.builder().id(1L).name("PARTNER")
					.constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build();

	public static final VehiculeJpaEntity vehiculeJpaEntity2 = VehiculeJpaEntity.builder().id(2L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L2H2).build())
			.model(ModelJpaEntity.builder().id(2L).name("KANGOO")
					.constructor(ConstructorJpaEntity.builder().id(2L).name("RENAULT").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("2 TUN 220").build();

	public static final VehiculeJpaEntity vehiculeJpaEntity3 = VehiculeJpaEntity.builder().id(3L)
			.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L3H3).build())
			.model(ModelJpaEntity.builder().id(3L).name("JUMPER")
					.constructor(ConstructorJpaEntity.builder().id(3L).name("CITROEN").build()).build())
			.circulationDate(LocalDate.of(2020, 01, 01)).registration("3 TUN 220").build();

	public static final Set<VehiculeJpaEntity> defaultVehiculeJpaEntitySet() {

		return Set.of(vehiculeJpaEntity1, vehiculeJpaEntity2, vehiculeJpaEntity3);
	}

	public static final VehiculeJpaEntity defaultVehiculeJpaEntity() {

		return vehiculeJpaEntity1;
	}

	public static final VehiculeJpaEntityBuilder defaultVehiculeJpaEntityBuilder() {
		return VehiculeJpaEntity.builder().id(1L)
				.type(EngineTypeJpaEntity.builder().id(1L).code(EngineTypeCode.VAN_L1H1).build())
				.model(ModelJpaEntity.builder().id(1L).name("PARTNER")
						.constructor(ConstructorJpaEntity.builder().id(1L).name("PEUGEOT").build()).build())
				.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220");
	}
}
