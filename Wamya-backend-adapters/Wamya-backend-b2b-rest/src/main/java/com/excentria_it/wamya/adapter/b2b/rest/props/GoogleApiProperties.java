package com.excentria_it.wamya.adapter.b2b.rest.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app.google-api")
@Validated
@Data
public class GoogleApiProperties {
	@NotEmpty
	@NotNull
	private String apiKey;

	@NotNull
	private DistanceMatrixApi distanceMatrixApi;

	@NotNull
	private GeoCodingApi geoCodingApi;

	@Data
	@Validated
	public static class DistanceMatrixApi {
		@NotEmpty
		@NotNull
		String url;
	}

	@Data
	@Validated
	public static class GeoCodingApi {
		@NotEmpty
		@NotNull
		String url;
	}
}
