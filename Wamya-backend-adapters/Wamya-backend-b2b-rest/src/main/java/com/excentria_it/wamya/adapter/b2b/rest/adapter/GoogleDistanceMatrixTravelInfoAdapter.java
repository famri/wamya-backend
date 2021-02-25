package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.client.RestTemplate;

import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix;
import com.excentria_it.wamya.adapter.b2b.rest.props.GoogleApiProperties;
import com.excentria_it.wamya.application.port.out.FetchTravelInfoPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RequiredArgsConstructor
@Slf4j
public class GoogleDistanceMatrixTravelInfoAdapter implements FetchTravelInfoPort {
	private static final String OK_STATUS = "OK";
	private final GoogleApiProperties googleApiProperties;
	private final RestTemplate restTemplate;

	@Override
	public Optional<JourneyTravelInfo> fetchTravelInfo(GeoCoordinates departureGeoCoordinates,
			GeoCoordinates arrivalGeoCoordinates) {

		Map<String, String> urlParams = new HashMap<>();

		urlParams.put("origins", String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
				departureGeoCoordinates.getLongitude()));
		urlParams.put("destinations", String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
				arrivalGeoCoordinates.getLongitude()));
		urlParams.put("key", googleApiProperties.getApiKey());

		try {

			DistanceMatrix distanceMatrix = restTemplate
					.getForObject(googleApiProperties.getDistanceMatrixApi().getUrl(), DistanceMatrix.class, urlParams);
			if (OK_STATUS.equals(distanceMatrix.getStatus())) {
				DistanceMatrix.Row row = distanceMatrix.getRows().get(0);
				DistanceMatrix.Element element = row.getElements().get(0);
				if (OK_STATUS.equals(element.getStatus())) {
					Integer distance = element.getDistance().getValue();

					Integer duration = element.getDuration().getValue();

					Integer hours = (duration / 3600);
					Integer minutes = (duration % 3600) / 60;

					return Optional.of(new JourneyTravelInfo(distance, hours, minutes));

				} else {
					log.error(String.format("Element status %s for origin: %s and destination %s", element.getStatus(),
							departureGeoCoordinates.toString(), arrivalGeoCoordinates.toString()));
					return Optional.empty();
				}
			} else {
				log.error(String.format("Distance matrix status %s for origin: %s and destination %s",
						distanceMatrix.getStatus(), departureGeoCoordinates.toString(),
						arrivalGeoCoordinates.toString()));
				return Optional.empty();
			}

		} catch (Exception e) {

			log.error(String.format("Unable to find distance matrix for origin: %s and destination %s",
					departureGeoCoordinates.toString(), arrivalGeoCoordinates.toString()));
			log.error("Exception: ", e);
			return Optional.empty();
		}

	}

}
