package com.excentria_it.wamya.test.data.common;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;

public class DepartmentJpaTestData {

	public static DepartmentJpaEntity defaultExistentDepartmentJpaEntity() {

		LocalizedDepartmentJpaEntity ldjeFR = new LocalizedDepartmentJpaEntity(new LocalizedId(1L, "fr_FR"), null,
				"Sfax");

		LocalizedDepartmentJpaEntity ldjeEN = new LocalizedDepartmentJpaEntity(new LocalizedId(1L, "en_US"), null,
				"Sfax");

		DepartmentJpaEntity dep = new DepartmentJpaEntity("Sfax", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), null,
				new HashSet<DelegationJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		dep.setId(1L);

		ldjeFR.setDepartment(dep);
		ldjeEN.setDepartment(dep);

		return dep;
	}

	public static DepartmentJpaEntity defaultNewDepartmentJpaEntity() {

		LocalizedDepartmentJpaEntity ldjeFR = new LocalizedDepartmentJpaEntity(new LocalizedId("fr_FR"), null, "Sfax");

		LocalizedDepartmentJpaEntity ldjeEN = new LocalizedDepartmentJpaEntity(new LocalizedId("en_US"), null, "Sfax");

		DepartmentJpaEntity dep = new DepartmentJpaEntity("Sfax", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), null,
				new HashSet<DelegationJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		ldjeFR.setDepartment(dep);
		ldjeEN.setDepartment(dep);

		return dep;
	}

	public static DepartmentJpaEntity defaultExistentDepartureDepartmentJpaEntity() {

		return defaultExistentDepartmentJpaEntity();
	}

	public static DepartmentJpaEntity defaultNewDepartureDepartmentJpaEntity() {

		return defaultNewDepartmentJpaEntity();
	}

	public static DepartmentJpaEntity defaultExistentArrivalDepartmentJpaEntity() {

		LocalizedDepartmentJpaEntity ldjeFR = new LocalizedDepartmentJpaEntity(new LocalizedId(2L, "fr_FR"), null,
				"Tunis");
		LocalizedDepartmentJpaEntity ldjeEN = new LocalizedDepartmentJpaEntity(new LocalizedId(2L, "en_US"), null,
				"Tunis");

		DepartmentJpaEntity dep = new DepartmentJpaEntity("Tunis", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), null,
				new HashSet<DelegationJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		dep.setId(2L);

		ldjeFR.setDepartment(dep);
		ldjeEN.setDepartment(dep);

		return dep;
	}

	public static DepartmentJpaEntity defaultNewArrivalDepartmentJpaEntity() {

		LocalizedDepartmentJpaEntity ldjeFR = new LocalizedDepartmentJpaEntity(new LocalizedId("fr_FR"), null, "Tunis");
		LocalizedDepartmentJpaEntity ldjeEN = new LocalizedDepartmentJpaEntity(new LocalizedId("en_US"), null, "Tunis");

		DepartmentJpaEntity dep = new DepartmentJpaEntity("Tunis", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), null,
				new HashSet<DelegationJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		ldjeFR.setDepartment(dep);
		ldjeEN.setDepartment(dep);

		return dep;
	}
}
