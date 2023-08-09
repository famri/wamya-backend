package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;

import java.math.BigDecimal;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedLocalityJpaEntity;

public class LocalityJpaTestData {

	public static LocalityJpaEntity defaultExistentLocalityJpaEntity() {

		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();

		LocalizedLocalityJpaEntity lljeFR = new LocalizedLocalityJpaEntity(new LocalizedId(1L, "fr_FR"),
				"Cité El Moez 1", null);
		LocalizedLocalityJpaEntity lljeEN = new LocalizedLocalityJpaEntity(new LocalizedId(1L, "en_US"),
				"El Moez 1 City", null);

		LocalityJpaEntity loc = new LocalityJpaEntity("Cité El Moez 1, El Moez 1 City",
				Map.of("fr_FR", lljeFR, "en_US", lljeEN), delegation, new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		loc.setId(1L);

		lljeFR.setLocality(loc);
		lljeEN.setLocality(loc);

		return loc;
	}

	public static LocalityJpaEntity defaultNewLocalityJpaEntity() {

		DelegationJpaEntity delegation = defaultNewDelegationJpaEntity();

		LocalizedLocalityJpaEntity lljeFR = new LocalizedLocalityJpaEntity(new LocalizedId("fr_FR"), "Cité El Moez 1",
				null);
		LocalizedLocalityJpaEntity lljeEN = new LocalizedLocalityJpaEntity(new LocalizedId("en_US"), "El Moez 1 City",
				null);

		LocalityJpaEntity loc = new LocalityJpaEntity("Cité El Moez 1, El Moez 1 City",
				Map.of("fr_FR", lljeFR, "en_US", lljeEN), delegation, new BigDecimal(36.80157399848794),
				new BigDecimal(10.178896922512495));

		lljeFR.setLocality(loc);
		lljeEN.setLocality(loc);

		return loc;
	}
}
