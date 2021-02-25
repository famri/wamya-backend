package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;

import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse;
import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse.AddressComponenetObject;
import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse.AddressComponenetsObject;
import com.excentria_it.wamya.adapter.b2b.rest.props.GoogleApiProperties;
import com.excentria_it.wamya.application.port.out.FetchGeoPlaceDepartmentNamePort;
import com.excentria_it.wamya.common.annotation.WebAdapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RequiredArgsConstructor
@Slf4j
public class GoogleGeoCodingAdapter implements FetchGeoPlaceDepartmentNamePort {

	private static final String OK_STATUS = "OK";
	private static final String ADMIN_AREA_LEVEL_1 = "administrative_area_level_1";
	private final GoogleApiProperties googleApiProperties;
	private final RestTemplate restTemplate;

	@Override
	public Optional<String> fetchDepartmentName(BigDecimal geoPlaceLatitude, BigDecimal geoPlaceLongitude,
			String locale) {

		Map<String, String> urlParams = new HashMap<>();

		urlParams.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		urlParams.put("result_type", "administrative_area_level_1");
		urlParams.put("language", "en_US");
		urlParams.put("key", googleApiProperties.getApiKey());

		try {

			GeoCodeResponse geoCodeResponse = restTemplate.getForObject(googleApiProperties.getGeoCodingApi().getUrl(),
					GeoCodeResponse.class, urlParams);
			if (OK_STATUS.equals(geoCodeResponse.getStatus())) {
				List<AddressComponenetsObject> addressComponenetsObjectList = geoCodeResponse.getResults();
				if (addressComponenetsObjectList.isEmpty()) {
					log.error("Results list is empty.");
					return Optional.empty();
				} else {
					AddressComponenetsObject addressComponenetsObject = addressComponenetsObjectList.get(0);
					List<AddressComponenetObject> addressComponenetObjectList = addressComponenetsObject
							.getAddressComponents();
					if (addressComponenetObjectList.isEmpty()) {
						log.error("AddressComponenetsObject list is empty.");
						return Optional.empty();
					} else {
						Optional<AddressComponenetObject> addressComponenetObject = addressComponenetObjectList.stream()
								.filter(a -> a.getTypes().contains(ADMIN_AREA_LEVEL_1)).findFirst();
						if (addressComponenetObject.isEmpty()) {
							log.error("No AddressComponenetsObject was found with " + ADMIN_AREA_LEVEL_1);
							return Optional.empty();
						} else {
							return Optional.of(addressComponenetObject.get().getLongName());
						}
					}
				}
			} else {
				log.error(String.format("GeoCodeResponse status %s for latitude: %.4f and longitude %.4f",
						geoCodeResponse.getStatus(), geoPlaceLatitude, geoPlaceLongitude));
				return Optional.empty();
			}
		} catch (Exception e) {
			log.error(String.format("Unable to find department name for for latitude: %.4f and longitude %.4f",
					geoPlaceLatitude, geoPlaceLongitude));
			log.error("Exception: ", e);
			return Optional.empty();
		}

	}
}
