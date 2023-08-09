package com.excentria_it.wamya.domain;

import java.math.BigDecimal;

import lombok.Value;

@Value
public class GeoCoordinates {

	private BigDecimal latitude;
	private BigDecimal longitude;

}
