package com.excentria_it.wamya.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransporterVehiculeDto {
	private Long id;

	private String regsitrationNumber;

	private LocalDate circulationDate;

	private String constructorName;

	private String modelName;

	private Long engineTypeId;

	private String engineTypeName;

	private String photoUrl;

}
