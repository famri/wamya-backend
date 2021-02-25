package com.excentria_it.wamya.application.port.in;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.excentria_it.wamya.domain.GeoPlaceDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface CreateFavoriteGeoPlaceUseCase {
	GeoPlaceDto createFavoriteGeoPlace(String geoPlaceName, BigDecimal geoPlaceLatitude, BigDecimal geoPlaceLongitude,
			String userName, String locale);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class CreateFavoriteGeoPlaceCommand {
		@NotNull
		private BigDecimal latitude;

		@NotNull
		private BigDecimal longitude;

		@NotNull
		@NotEmpty
		private String name;
	}
}
