package com.excentria_it.wamya.adapter.persistence.mapper;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehicleJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.TransporterProposalOutput;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData;
import com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.defaultJourneyProposalJpaEntity;
import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.defaultJourneyProposalJpaEntityBuilder;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity;
import static com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData.defaultVehicleJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JourneyProposalMapperTests {
    private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();
    private JourneyProposalMapper journeyProposalMapper = new JourneyProposalMapper(documentUrlResolver);
    private static final Double JOURNEY_PRICE = 250.0;

    @BeforeEach
    void initDocumentUrlResolver() {
        documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
    }

    @Test
    void testMapToJpaEntity() {

        TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
        VehicleJpaEntity vehicleJpaEntity = defaultVehicleJpaEntity();

        JourneyProposalJpaEntity journeyProposalJpaEntity = journeyProposalMapper.mapToJpaEntity(JOURNEY_PRICE,
                transporterJpaEntity, vehicleJpaEntity);

        assertEquals(JOURNEY_PRICE, journeyProposalJpaEntity.getPrice());

        assertEquals(transporterJpaEntity, journeyProposalJpaEntity.getTransporter());

        assertEquals(vehicleJpaEntity, journeyProposalJpaEntity.getVehicle());
    }

    @Test
    void testMapToJourneyProposalDto() {

        JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

        JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity,
                "en_US");

        assertEquals(journeyProposalJpaEntity.getId(), journeyProposalDto.getId());
        assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalDto.getPrice());

        assertEquals(journeyProposalJpaEntity.getTransporter().getOauthId(),
                journeyProposalDto.getTransporter().getId());
        assertEquals(journeyProposalJpaEntity.getTransporter().getFirstname(),
                journeyProposalDto.getTransporter().getFirstname());
        assertEquals(journeyProposalJpaEntity.getTransporter().getGlobalRating(),
                journeyProposalDto.getTransporter().getGlobalRating());
        assertEquals(
                documentUrlResolver.resolveUrl(journeyProposalJpaEntity.getTransporter().getProfileImage().getId(),
                        journeyProposalJpaEntity.getTransporter().getProfileImage().getHash()),
                journeyProposalDto.getTransporter().getPhotoUrl());

        assertEquals(journeyProposalJpaEntity.getVehicle().getId(), journeyProposalDto.getVehicle().getId());
        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getConstructor().getName(),
                journeyProposalDto.getVehicle().getConstructor());
        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getName(),
                journeyProposalDto.getVehicle().getModel());
        assertEquals(getVehicleImageUrl(journeyProposalJpaEntity.getVehicle()),
                journeyProposalDto.getVehicle().getPhotoUrl());
    }

    @Test
    void testMapToDomainEntityWithNonDefaultVehicleImage() {

        JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntityBuilder()
                .vehicle(VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder()
                        .image(DocumentJpaTestData.nonDefaultVehicleImageDocumentJpaEntity()).build())
                .build();

        JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity,
                "en_US");

        assertEquals(journeyProposalJpaEntity.getId(), journeyProposalDto.getId());
        assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalDto.getPrice());

        assertEquals(journeyProposalJpaEntity.getTransporter().getOauthId(),
                journeyProposalDto.getTransporter().getId());
        assertEquals(journeyProposalJpaEntity.getTransporter().getFirstname(),
                journeyProposalDto.getTransporter().getFirstname());
        assertEquals(journeyProposalJpaEntity.getTransporter().getGlobalRating(),
                journeyProposalDto.getTransporter().getGlobalRating());
        assertEquals(
                documentUrlResolver.resolveUrl(journeyProposalJpaEntity.getTransporter().getProfileImage().getId(),
                        journeyProposalJpaEntity.getTransporter().getProfileImage().getHash()),
                journeyProposalDto.getTransporter().getPhotoUrl());

        assertEquals(journeyProposalJpaEntity.getVehicle().getId(), journeyProposalDto.getVehicle().getId());
        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getConstructor().getName(),
                journeyProposalDto.getVehicle().getConstructor());
        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getName(),
                journeyProposalDto.getVehicle().getModel());
        assertEquals(getVehicleImageUrl(journeyProposalJpaEntity.getVehicle()),
                journeyProposalDto.getVehicle().getPhotoUrl());
    }

    @Test
    void testMapNullToDomainEntity() {

        JourneyProposalDto journeyProposalDto = journeyProposalMapper.mapToJourneyProposalDto(null, "en_US");

        assertNull(journeyProposalDto);
    }

    private String getVehicleImageUrl(VehicleJpaEntity vehicle) {
        return (vehicle.getImage() != null)
                ? documentUrlResolver.resolveUrl(vehicle.getImage().getId(), vehicle.getImage().getHash())
                : documentUrlResolver.resolveUrl(vehicle.getType().getImage().getId(),
                vehicle.getType().getImage().getHash());
    }

    @Test
    void testMapNullToTransporterProposalOutput() {

        TransporterProposalOutput journeyProposalOutput = journeyProposalMapper.mapToTransporterProposalOutput(null,
                "en_US");

        assertNull(journeyProposalOutput);
    }

    @Test
    void testMapToTransporterProposalOutput() {
        JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntityBuilder()
                .vehicle(VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder()
                        .image(DocumentJpaTestData.nonDefaultVehicleImageDocumentJpaEntity()).build())
                .journeyRequest(JourneyRequestJpaTestData.defaultExistentJourneyRequestJpaEntity()).build();

        TransporterProposalOutput journeyProposalOutput = journeyProposalMapper
                .mapToTransporterProposalOutput(journeyProposalJpaEntity, "en_US");

        assertEquals(journeyProposalJpaEntity.getId(), journeyProposalOutput.getId());
        assertEquals(journeyProposalJpaEntity.getPrice(), journeyProposalOutput.getPrice());
        assertEquals(journeyProposalJpaEntity.getStatus().getCode(), journeyProposalOutput.getStatusCode());
        assertEquals(journeyProposalJpaEntity.getStatus().getValue("en_US"), journeyProposalOutput.getStatus());

        assertEquals(journeyProposalJpaEntity.getVehicle().getId(), journeyProposalOutput.getVehicle().getId());
        assertEquals(journeyProposalJpaEntity.getVehicle().getRegistration(),
                journeyProposalOutput.getVehicle().getRegistrationNumber());
        assertEquals(journeyProposalJpaEntity.getVehicle().getCirculationDate(),
                journeyProposalOutput.getVehicle().getCirculationDate());

        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getConstructor().getName(),
                journeyProposalOutput.getVehicle().getConstructorName());
        assertEquals(journeyProposalJpaEntity.getVehicle().getModel().getName(),
                journeyProposalOutput.getVehicle().getModelName());

        assertEquals(journeyProposalJpaEntity.getVehicle().getType().getId(),
                journeyProposalOutput.getVehicle().getEngineTypeId());

        assertEquals(journeyProposalJpaEntity.getVehicle().getType().getName("en_US"),
                journeyProposalOutput.getVehicle().getEngineTypeName());

        assertEquals(journeyProposalJpaEntity.getVehicle().getImage().getId(),
                journeyProposalOutput.getVehicle().getImageId());
        assertEquals(journeyProposalJpaEntity.getVehicle().getImage().getHash(),
                journeyProposalOutput.getVehicle().getImageHash());

        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getId(), journeyProposalOutput.getJourney().getId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDistance(),
                journeyProposalOutput.getJourney().getDistance());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getHours(),
                journeyProposalOutput.getJourney().getHours());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getMinutes(),
                journeyProposalOutput.getJourney().getMinutes());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDateTime(),
                journeyProposalOutput.getJourney().getDateTime());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getWorkers(),
                journeyProposalOutput.getJourney().getWorkers());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDescription(),
                journeyProposalOutput.getJourney().getDescription());

        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getPlaceId().getId(),
                journeyProposalOutput.getJourney().getDeparturePlace().getId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getPlaceId().getType().name(),
                journeyProposalOutput.getJourney().getDeparturePlace().getType());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getName("en_US"),
                journeyProposalOutput.getJourney().getDeparturePlace().getName());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getDepartment().getId(),
                journeyProposalOutput.getJourney().getDeparturePlace().getDepartmentId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getLatitude(),
                journeyProposalOutput.getJourney().getDeparturePlace().getLatitude());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getDeparturePlace().getLongitude(),
                journeyProposalOutput.getJourney().getDeparturePlace().getLongitude());

        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getPlaceId().getId(),
                journeyProposalOutput.getJourney().getArrivalPlace().getId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getPlaceId().getType().name(),
                journeyProposalOutput.getJourney().getArrivalPlace().getType());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getName("en_US"),
                journeyProposalOutput.getJourney().getArrivalPlace().getName());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getDepartment().getId(),
                journeyProposalOutput.getJourney().getArrivalPlace().getDepartmentId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getLatitude(),
                journeyProposalOutput.getJourney().getArrivalPlace().getLatitude());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getArrivalPlace().getLongitude(),
                journeyProposalOutput.getJourney().getArrivalPlace().getLongitude());

        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getId(),
                journeyProposalOutput.getJourney().getEngineType().getId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getName("en_US"),
                journeyProposalOutput.getJourney().getEngineType().getName());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getEngineType().getCode().name(),
                journeyProposalOutput.getJourney().getEngineType().getCode());

        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getOauthId(),
                journeyProposalOutput.getJourney().getClient().getOauthId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getFirstname(),
                journeyProposalOutput.getJourney().getClient().getFirstname());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getProfileImage().getId(),
                journeyProposalOutput.getJourney().getClient().getImageId());
        assertEquals(journeyProposalJpaEntity.getJourneyRequest().getClient().getProfileImage().getHash(),
                journeyProposalOutput.getJourney().getClient().getImageHash());

    }
}
