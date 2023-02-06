package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public interface TransporterVehicleOutput {

    Long getId();

    String getRegistrationNumber();

    @Value("#{new  com.excentria_it.wamya.domain.TransporterVehicleOutput.ConstructorDto(target.constructorId, target.constructorName, target.temporaryConstructorName)}")
    ConstructorDto getConstructor();

    @Value("#{new  com.excentria_it.wamya.domain.TransporterVehicleOutput.ModelDto(target.modelId, target.modelName, target.temporaryModelName)}")
    ModelDto getModel();

    @Value("#{new  com.excentria_it.wamya.domain.TransporterVehicleOutput.EngineTypeDto(target.engineTypeId, target.engineTypeName, target.engineTypeImageId, target.engineTypeImageHash)}")
    EngineTypeDto getEngineType();

    LocalDate getCirculationDate();

    @Value("#{new  com.excentria_it.wamya.domain.TransporterVehicleOutput.ImageDto(target.imageId, target.imageHash)}")
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
