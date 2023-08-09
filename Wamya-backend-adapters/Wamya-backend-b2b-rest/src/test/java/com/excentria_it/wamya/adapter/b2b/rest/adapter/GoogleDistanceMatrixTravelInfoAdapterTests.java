package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix;
import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix.Distance;
import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix.Duration;
import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix.Element;
import com.excentria_it.wamya.adapter.b2b.rest.dto.DistanceMatrix.Row;
import com.excentria_it.wamya.adapter.b2b.rest.props.GoogleApiProperties;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("b2b-rest-local")
public class GoogleDistanceMatrixTravelInfoAdapterTests {
	@Autowired
	private GoogleDistanceMatrixTravelInfoAdapter googleDistanceMatrixTravelInfoAdapter;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private GoogleApiProperties googleApiProperties;

	private MockRestServiceServer mockServer;
	private ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void init() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void testFetchTravelInfo() throws JsonProcessingException, URISyntaxException {
		BigDecimal departureLatitude = new BigDecimal(34.7321439241108);
		BigDecimal departureLongitude = new BigDecimal(10.763976857761634);

		BigDecimal arrivalLatitude = new BigDecimal(36.80320385406925);
		BigDecimal arrivalLongitude = new BigDecimal(10.178847626618056);

		GeoCoordinates departureGeoCoordinates = new GeoCoordinates(departureLatitude, departureLongitude);
		GeoCoordinates arrivalGeoCoordinates = new GeoCoordinates(arrivalLatitude, arrivalLongitude);
		DistanceMatrix distanceMatrix = new DistanceMatrix();

		distanceMatrix.setOriginAddresses(List.of("Sfax, Tunisie"));
		distanceMatrix.setDestinationAddresses(List.of("Tunis, Tunisie"));

		distanceMatrix.setStatus("OK");

		Row row = new Row();

		Element element = new Element();
		element.setStatus("OK");

		Distance distance = new Distance();
		distance.setValue(267618);

		Duration duration = new Duration();
		duration.setValue(10534);

		element.setDistance(distance);
		element.setDuration(duration);

		row.setElements(List.of(element));

		distanceMatrix.setRows(List.of(row));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getDistanceMatrixApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("origins", String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
				departureGeoCoordinates.getLongitude()));
		uriVariables.put("destinations", String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
				arrivalGeoCoordinates.getLongitude()));
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("origins",
						String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
								departureGeoCoordinates.getLongitude())))
				.andExpect(queryParam("destinations",
						String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
								arrivalGeoCoordinates.getLongitude())))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(distanceMatrix)));

		Optional<JourneyTravelInfo> journeyTravelInfo = googleDistanceMatrixTravelInfoAdapter
				.fetchTravelInfo(departureGeoCoordinates, arrivalGeoCoordinates);

		mockServer.verify();
		assertEquals(distance.getValue(), journeyTravelInfo.get().getDistance());
		assertEquals(duration.getValue() / 3600, journeyTravelInfo.get().getHours());
		assertEquals((duration.getValue() % 3600) / 60, journeyTravelInfo.get().getMinutes());
	}

	@Test
	void testFetchTravelInfoWithKOGlobalStatus() throws JsonProcessingException, URISyntaxException {

		BigDecimal departureLatitude = new BigDecimal(34.7321439241108);
		BigDecimal departureLongitude = new BigDecimal(10.763976857761634);

		BigDecimal arrivalLatitude = new BigDecimal(36.80320385406925);
		BigDecimal arrivalLongitude = new BigDecimal(10.178847626618056);

		GeoCoordinates departureGeoCoordinates = new GeoCoordinates(departureLatitude, departureLongitude);
		GeoCoordinates arrivalGeoCoordinates = new GeoCoordinates(arrivalLatitude, arrivalLongitude);

		DistanceMatrix distanceMatrix = new DistanceMatrix();

		distanceMatrix.setOriginAddresses(List.of("Sfax, Tunisie"));
		distanceMatrix.setDestinationAddresses(List.of("Tunis, Tunisie"));

		distanceMatrix.setStatus("KO");

		Row row = new Row();

		Element element = new Element();
		element.setStatus("OK");

		Distance distance = new Distance();
		distance.setValue(267618);

		Duration duration = new Duration();
		duration.setValue(10534);

		element.setDistance(distance);
		element.setDuration(duration);

		row.setElements(List.of(element));

		distanceMatrix.setRows(List.of(row));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getDistanceMatrixApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("origins", String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
				departureGeoCoordinates.getLongitude()));
		uriVariables.put("destinations", String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
				arrivalGeoCoordinates.getLongitude()));
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("origins",
						String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
								departureGeoCoordinates.getLongitude())))
				.andExpect(queryParam("destinations",
						String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
								arrivalGeoCoordinates.getLongitude())))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(distanceMatrix)));

		Optional<JourneyTravelInfo> journeyTravelInfo = googleDistanceMatrixTravelInfoAdapter
				.fetchTravelInfo(departureGeoCoordinates, arrivalGeoCoordinates);

		mockServer.verify();
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	void testFetchTravelInfoWithKOElementStatus() throws JsonProcessingException, URISyntaxException {

		BigDecimal departureLatitude = new BigDecimal(34.7321439241108);
		BigDecimal departureLongitude = new BigDecimal(10.763976857761634);

		BigDecimal arrivalLatitude = new BigDecimal(36.80320385406925);
		BigDecimal arrivalLongitude = new BigDecimal(10.178847626618056);

		GeoCoordinates departureGeoCoordinates = new GeoCoordinates(departureLatitude, departureLongitude);
		GeoCoordinates arrivalGeoCoordinates = new GeoCoordinates(arrivalLatitude, arrivalLongitude);
		DistanceMatrix distanceMatrix = new DistanceMatrix();

		distanceMatrix.setOriginAddresses(List.of("Sfax, Tunisie"));
		distanceMatrix.setDestinationAddresses(List.of("Tunis, Tunisie"));

		distanceMatrix.setStatus("OK");

		Row row = new Row();

		Element element = new Element();
		element.setStatus("KO");

		Distance distance = new Distance();
		distance.setValue(267618);

		Duration duration = new Duration();
		duration.setValue(10534);

		element.setDistance(distance);
		element.setDuration(duration);

		row.setElements(List.of(element));

		distanceMatrix.setRows(List.of(row));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getDistanceMatrixApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("origins", String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
				departureGeoCoordinates.getLongitude()));
		uriVariables.put("destinations", String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
				arrivalGeoCoordinates.getLongitude()));
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("origins",
						String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
								departureGeoCoordinates.getLongitude())))
				.andExpect(queryParam("destinations",
						String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
								arrivalGeoCoordinates.getLongitude())))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(distanceMatrix)));

		Optional<JourneyTravelInfo> journeyTravelInfo = googleDistanceMatrixTravelInfoAdapter
				.fetchTravelInfo(departureGeoCoordinates, arrivalGeoCoordinates);

		mockServer.verify();
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	void testFetchTravelInfoWithServerErrorStatus() throws JsonProcessingException, URISyntaxException {

		BigDecimal departureLatitude = new BigDecimal(34.7321439241108);
		BigDecimal departureLongitude = new BigDecimal(10.763976857761634);

		BigDecimal arrivalLatitude = new BigDecimal(36.80320385406925);
		BigDecimal arrivalLongitude = new BigDecimal(10.178847626618056);

		GeoCoordinates departureGeoCoordinates = new GeoCoordinates(departureLatitude, departureLongitude);
		GeoCoordinates arrivalGeoCoordinates = new GeoCoordinates(arrivalLatitude, arrivalLongitude);
		DistanceMatrix distanceMatrix = new DistanceMatrix();

		distanceMatrix.setOriginAddresses(List.of("Sfax, Tunisie"));
		distanceMatrix.setDestinationAddresses(List.of("Tunis, Tunisie"));

		distanceMatrix.setStatus("UNKNOWN_ERROR");

		Row row = new Row();

		distanceMatrix.setRows(List.of(row));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getDistanceMatrixApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("origins", String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
				departureGeoCoordinates.getLongitude()));
		uriVariables.put("destinations", String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
				arrivalGeoCoordinates.getLongitude()));
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("origins",
						String.format("%.15f,%.15f", departureGeoCoordinates.getLatitude(),
								departureGeoCoordinates.getLongitude())))
				.andExpect(queryParam("destinations",
						String.format("%.15f,%.15f", arrivalGeoCoordinates.getLatitude(),
								arrivalGeoCoordinates.getLongitude())))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withServerError()
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(distanceMatrix)));

		Optional<JourneyTravelInfo> journeyTravelInfo = googleDistanceMatrixTravelInfoAdapter
				.fetchTravelInfo(departureGeoCoordinates, arrivalGeoCoordinates);

		mockServer.verify();

		assertThat(journeyTravelInfo).isEmpty();
	}
}
