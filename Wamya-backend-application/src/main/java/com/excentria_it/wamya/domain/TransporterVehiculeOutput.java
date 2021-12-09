package com.excentria_it.wamya.domain;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface TransporterVehiculeOutput {

	Long getId();

	String getRegistrationNumber();

	@Value("#{new  com.excentria_it.wamya.domain.TransporterVehiculeOutput.ConstructorDto(target.constructorId, target.constructorName, target.temporaryConstructorName)}")
	ConstructorDto getConstructor();

	@Value("#{new  com.excentria_it.wamya.domain.TransporterVehiculeOutput.ModelDto(target.modelId, target.modelName, target.temporaryModelName)}")
	ModelDto getModel();

	@Value("#{new  com.excentria_it.wamya.domain.TransporterVehiculeOutput.EngineTypeDto(target.engineTypeId, target.engineTypeName, target.engineTypeImageId, target.engineTypeImageHash)}")
	EngineTypeDto getEngineType();

	LocalDate getCirculationDate();

	@Value("#{new  com.excentria_it.wamya.domain.TransporterVehiculeOutput.ImageDto(target.imageId, target.imageHash)}")
	ImageDto getImage();

	@AllArgsConstructor
	@Data
	public static class ConstructorDto {
		private Long id;
		private String name;
		private String temporaryName;
	}

	@AllArgsConstructor
	@Data
	public static class ModelDto {

		private Long id;
		private String name;
		private String temporaryName;

	}

	@AllArgsConstructor
	@Data
	public static class EngineTypeDto {

		private Long id;
		private String name;
		private Long imageId;
		private String imageHash;

	}

	@AllArgsConstructor
	@Data
	public static class ImageDto {
		private Long id;
		private String hash;
	}

}
