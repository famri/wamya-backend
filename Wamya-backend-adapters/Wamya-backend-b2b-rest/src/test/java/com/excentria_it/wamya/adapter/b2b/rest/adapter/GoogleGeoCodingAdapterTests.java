package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Collections;
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

import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse;
import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse.AddressComponenetObject;
import com.excentria_it.wamya.adapter.b2b.rest.dto.GeoCodeResponse.AddressComponenetsObject;
import com.excentria_it.wamya.adapter.b2b.rest.props.GoogleApiProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("b2b-rest-local")
public class GoogleGeoCodingAdapterTests {
	@Autowired
	private GoogleGeoCodingAdapter googleGeoCodingAdapter;
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
	void testFetchDepartmentName() throws JsonProcessingException {
		// given
		AddressComponenetObject addressComponenetObject1 = new AddressComponenetObject();
		addressComponenetObject1.setLongName("Medenine");
		addressComponenetObject1.setTypes(List.of("administrative_area_level_1", "political"));

		AddressComponenetObject addressComponenetObject2 = new AddressComponenetObject();
		addressComponenetObject2.setLongName("Tunisia");
		addressComponenetObject2.setTypes(List.of("country", "political"));

		AddressComponenetsObject addressComponenetsObject = new AddressComponenetsObject();
		addressComponenetsObject.setAddressComponents(List.of(addressComponenetObject1, addressComponenetObject2));

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("OK");
		geoCodeResponse.setResults(List.of(addressComponenetsObject));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertEquals("Medenine", departmentName.get());
	}

	@Test
	void testFetchDepartmentNameWithZeroResultsStatus() throws JsonProcessingException {
		// given

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("ZERO_RESULTS");
		geoCodeResponse.setResults(Collections.emptyList());

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertThat(departmentName).isEmpty();
	}

	@Test
	void testFetchDepartmentNameWithEmptyResults() throws JsonProcessingException, URISyntaxException {
		// given

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("OK");
		geoCodeResponse.setResults(Collections.emptyList());

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertThat(departmentName).isEmpty();
	}

	@Test
	void testFetchDepartmentNameWithException() throws JsonProcessingException {
		// given

		AddressComponenetsObject addressComponenetsObject = new AddressComponenetsObject();
		addressComponenetsObject.setAddressComponents(Collections.emptyList());

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("UNKNOWN_ERROR");
		geoCodeResponse.setResults(List.of(addressComponenetsObject));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey()))
				.andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
						.body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertThat(departmentName).isEmpty();
	}

	@Test
	void testFetchDepartmentNameWithEmptyAddressComponentList() throws JsonProcessingException {
		// given

		AddressComponenetsObject addressComponenetsObject = new AddressComponenetsObject();
		addressComponenetsObject.setAddressComponents(Collections.emptyList());

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("OK");
		geoCodeResponse.setResults(List.of(addressComponenetsObject));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertThat(departmentName).isEmpty();
	}

	@Test
	void testFetchDepartmentNameWithNoAdminAreaLevel1() throws JsonProcessingException {
		// given
		AddressComponenetObject addressComponenetObject1 = new AddressComponenetObject();
		addressComponenetObject1.setLongName("Djerba");
		addressComponenetObject1.setTypes(List.of("administrative_area_level_2", "political"));

		AddressComponenetObject addressComponenetObject2 = new AddressComponenetObject();
		addressComponenetObject2.setLongName("Tunisia");
		addressComponenetObject2.setTypes(List.of("country", "political"));

		AddressComponenetsObject addressComponenetsObject = new AddressComponenetsObject();
		addressComponenetsObject.setAddressComponents(List.of(addressComponenetObject1, addressComponenetObject2));

		GeoCodeResponse geoCodeResponse = new GeoCodeResponse();
		geoCodeResponse.setStatus("OK");
		geoCodeResponse.setResults(List.of(addressComponenetsObject));

		UriComponentsBuilder uriBuilder = UriComponentsBuilder
				.fromHttpUrl(googleApiProperties.getGeoCodingApi().getUrl());
		Map<String, String> uriVariables = new HashMap<>();

		BigDecimal geoPlaceLatitude = new BigDecimal(34.7321439241108);
		BigDecimal geoPlaceLongitude = new BigDecimal(10.763976857761634);

		uriVariables.put("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude));
		uriVariables.put("result_type", "administrative_area_level_1");
		uriVariables.put("language", "en_US");
		uriVariables.put("key", googleApiProperties.getApiKey());

		mockServer.expect(ExpectedCount.once(), requestTo(uriBuilder.buildAndExpand(uriVariables).toUri()))
				.andExpect(method(HttpMethod.GET))
				.andExpect(queryParam("latlng", String.format("%.15f,%.15f", geoPlaceLatitude, geoPlaceLongitude)))
				.andExpect(queryParam("result_type", "administrative_area_level_1"))
				.andExpect(queryParam("language", "en_US"))
				.andExpect(queryParam("key", googleApiProperties.getApiKey())).andRespond(withStatus(HttpStatus.OK)
						.contentType(MediaType.APPLICATION_JSON).body(mapper.writeValueAsString(geoCodeResponse)));

		// when
		Optional<String> departmentName = googleGeoCodingAdapter.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, "en_US");

		// then
		mockServer.verify();
		assertThat(departmentName).isEmpty();
	}
}
