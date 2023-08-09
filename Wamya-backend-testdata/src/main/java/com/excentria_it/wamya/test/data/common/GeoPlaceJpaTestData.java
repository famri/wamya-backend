package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.math.BigDecimal;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;

public class GeoPlaceJpaTestData {
	public static GeoPlaceJpaEntity defaultGeoPlaceJpaEntity() {
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		ClientJpaEntity client = defaultExistentClientJpaEntity();

		GeoPlaceJpaEntity geoPlace = new GeoPlaceJpaEntity("Municipalité de Sfax", new BigDecimal(34.73228757639919),
				new BigDecimal(10.763917282570517), department, client);

		geoPlace.setId(1L);

		return geoPlace;
	}
}
