package com.excentria_it.wamya.adapter.persistence.mapper;

import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.TransporterVehicleOutput;
import com.excentria_it.wamya.domain.TransporterVehicleDto;
import com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData.defaultTransporterVehicleOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class VehicleMapperTests {
    private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();

    private VehiculeMapper vehiculeMapper = new VehiculeMapper(documentUrlResolver);

    @BeforeEach
    void initDocumentUrlResolver() {
        documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
    }

    @Test
    void testMapToDomainEntity() {

        // given
        TransporterVehicleOutput tvo = defaultTransporterVehicleOutput();

        // when
        TransporterVehicleDto tvDto = vehiculeMapper.mapToDomainEntity(tvo);

        // then
        assertEquals(tvo.getId(), tvDto.getId());
        assertEquals(tvo.getCirculationDate(), tvDto.getCirculationDate());
        assertEquals(tvo.getConstructor().getName(), tvDto.getConstructorName());
        assertEquals(tvo.getModel().getName(), tvDto.getModelName());
        assertEquals(tvo.getEngineType().getName(), tvDto.getEngineTypeName());
        assertEquals(getVehiculeImageUrl(tvo), tvDto.getPhotoUrl());
    }

    @Test
    void givenNoConstructorAndNoModelAndNoImage_whentMapToDomainEntity_thenReturnTransporterVehiculeDto() {

        // given
        TransporterVehicleOutput tvo = VehicleJpaEntityTestData.transporterVehicleOutputWithTemporaryConstructorAndModelAndNoImage();

        // when
        TransporterVehicleDto tvDto = vehiculeMapper.mapToDomainEntity(tvo);

        // then
        assertEquals(tvo.getId(), tvDto.getId());
        assertEquals(tvo.getCirculationDate(), tvDto.getCirculationDate());
        assertEquals(tvo.getConstructor().getTemporaryName(), tvDto.getConstructorName());
        assertEquals(tvo.getModel().getTemporaryName(), tvDto.getModelName());
        assertEquals(tvo.getEngineType().getName(), tvDto.getEngineTypeName());
        assertEquals(getVehiculeImageUrl(tvo), tvDto.getPhotoUrl());
    }

    private String getVehiculeImageUrl(TransporterVehicleOutput transporterVehicleOutput) {
        return (transporterVehicleOutput.getImage().getId() != null
                && transporterVehicleOutput.getImage().getHash() != null)
                ? documentUrlResolver.resolveUrl(transporterVehicleOutput.getImage().getId(),
                transporterVehicleOutput.getImage().getHash())
                : documentUrlResolver.resolveUrl(transporterVehicleOutput.getEngineType().getImageId(),
                transporterVehicleOutput.getEngineType().getImageHash());
    }
}
