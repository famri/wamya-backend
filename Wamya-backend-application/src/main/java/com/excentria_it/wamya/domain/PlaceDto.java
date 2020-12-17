package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlaceDto {

	private String placeId;

	private String placeRegionId;

	private String placeName;

}
