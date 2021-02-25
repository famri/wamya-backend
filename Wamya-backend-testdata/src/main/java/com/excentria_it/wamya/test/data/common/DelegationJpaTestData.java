package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedDelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;

public class DelegationJpaTestData {
	public static DelegationJpaEntity defaultExistentDelegationJpaEntity() {

		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		LocalizedDelegationJpaEntity ldjeFR = new LocalizedDelegationJpaEntity(new LocalizedId(1L, "fr_FR"), "Thyna",
				null);
		LocalizedDelegationJpaEntity ldjeEN = new LocalizedDelegationJpaEntity(new LocalizedId(1L, "en_US"), "Thyna",
				null);

		DelegationJpaEntity del = new DelegationJpaEntity("Thyna", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), department,
				new HashSet<LocalityJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		del.setId(1L);

		ldjeFR.setDelegation(del);
		ldjeEN.setDelegation(del);

		return del;
	}

	public static DelegationJpaEntity defaultNewDelegationJpaEntity() {

		DepartmentJpaEntity department = defaultNewDepartmentJpaEntity();

		LocalizedDelegationJpaEntity ldjeFR = new LocalizedDelegationJpaEntity(new LocalizedId("fr_FR"), "Thyna", null);
		LocalizedDelegationJpaEntity ldjeEN = new LocalizedDelegationJpaEntity(new LocalizedId("en_US"), "Thyna", null);

		DelegationJpaEntity del = new DelegationJpaEntity("Thyna", Map.of("fr_FR", ldjeFR, "en_US", ldjeEN), department,
				new HashSet<LocalityJpaEntity>(), new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		ldjeFR.setDelegation(del);
		ldjeEN.setDelegation(del);

		return del;
	}
}
