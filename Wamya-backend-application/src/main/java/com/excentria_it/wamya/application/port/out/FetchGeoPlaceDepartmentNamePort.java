package com.excentria_it.wamya.application.port.out;

import java.math.BigDecimal;
import java.util.Optional;

public interface FetchGeoPlaceDepartmentNamePort {

	Optional<String> fetchDepartmentName(BigDecimal geoPlaceLatitude, BigDecimal geoPlaceLongitude, String locale);

}
