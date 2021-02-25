package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;

import java.math.BigDecimal;
import java.util.HashMap;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.PlaceType;

public class PlaceJpaTestData {
	public static PlaceJpaEntity defaultPlaceJpaEntity() {
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();

		PlaceId pi = new PlaceId(1L, PlaceType.GEO_PLACE);

		LocalizedPlaceId lpiFR = new LocalizedPlaceId(pi, "fr_FR");
		LocalizedPlaceJpaEntity lpjeFR = new LocalizedPlaceJpaEntity(lpiFR, "Cité Thyna 1", null);

		LocalizedPlaceId lpiEN = new LocalizedPlaceId(pi, "en_US");
		LocalizedPlaceJpaEntity lpjeEN = new LocalizedPlaceJpaEntity(lpiEN, "Thyna 1 city", null);

		PlaceJpaEntity place = new PlaceJpaEntity(department, new HashMap<String, LocalizedPlaceJpaEntity>(),
				new BigDecimal(34.7777), new BigDecimal(10.7777));

		place.setPlaceId(pi);
		place.getLocalizations().put("fr_FR", lpjeFR);
		place.getLocalizations().put("en_US", lpjeEN);
		lpjeFR.setPlace(place);
		lpjeEN.setPlace(place);

		return place;
	}
}
