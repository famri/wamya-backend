package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.adapter.persistence.repository.*;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.VehicleNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.excentria_it.wamya.test.data.common.ConstructorJpaTestData.defaultConstructorJpaEntity;
import static com.excentria_it.wamya.test.data.common.EngineTypeJpaTestData.defaultEngineTypeJpaEntity;
import static com.excentria_it.wamya.test.data.common.ModelJpaTestData.defaultModelJpaEntityBuilder;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.defaultNewTransporterJpaEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class VehiclePersistenceAdapterTests {

    @Mock
    private TransporterRepository transporterRepository;
    @Mock
    private EngineTypeRepository engineTypeRepository;
    @Mock
    private ModelRepository modelRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private VehiclePersistenceAdapter vehiclePersistenceAdapter;

    @Test
    void givenBadTransporterEmailAndVehicleConstructorNameAndModelName_WhenAddVehicle_ThenReturnNull() {
        // given
        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.empty());
        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL,
                "Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then
        assertNull(addVehiculeDto);

    }

    @Test
    void givenBadTransporterEmailAndVehicleConstructorIdAndModelId_WhenAddVehicle_ThenReturnNull() {
        // given
        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.empty());
        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
                LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then
        assertNull(addVehiculeDto);

    }

    @Test
    void givenBadEngineTypeIdAndVehicleConstructorNameAndModelName_WhenAddVehicle_ThenReturnNull() {
        // given
        TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.of(transporterJpaEntity));
        given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL,
                "Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then
        assertNull(addVehiculeDto);

    }

    @Test
    void givenBadEngineTypeIdAndVehicleConstructorIdAndModelId_WhenAddVehicle_ThenReturnNull() {
        // given
        TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.of(transporterJpaEntity));
        given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
                LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then
        assertNull(addVehiculeDto);

    }

    @Test
    void givenBadModelIdAndVehicleConstructorIdAndModelId_WhenAddVehicle_ThenReturnNull() {
        // given
        TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.of(transporterJpaEntity));

        EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
        given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

        given(modelRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
                LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then
        assertNull(addVehiculeDto);

    }

    @Test
    void givenTransporterEmailAndVehicleConstructorNameAndModelName_WhenAddVehicle_ThenSucceed() {

        // given
        TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.of(transporterJpaEntity));

        EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
        given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

        given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL,
                "Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then

        then(transporterRepository).should(times(1)).save(transporterJpaEntity);

        Optional<VehicleJpaEntity> vehiculeJpaEntityOptional = transporterJpaEntity.getVehicles().stream()
                .filter(v -> v.getTemporaryModel().getConstructorName().equals("Constructor1")
                        && v.getTemporaryModel().getModelName().equals("Model1")
                        && v.getCirculationDate().equals(LocalDate.of(2020, 01, 01))
                        && v.getRegistration().equals("0001 TUN 220"))
                .findFirst();

        assertThat(vehiculeJpaEntityOptional).isNotEmpty();

        assertEquals("Constructor1", addVehiculeDto.getConstructorName());
        assertEquals("Model1", addVehiculeDto.getModelName());
        assertEquals(LocalDate.of(2020, 01, 01), addVehiculeDto.getCirculationDate());
        assertEquals("0001 TUN 220", addVehiculeDto.getRegistration());

    }


    @Test
    void givenTransporterEmailAndVehicleConstructorIdAndModelId_WhenAddVehicle_ThenSucceed() {

        // given
        TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

        given(transporterRepository.findTransporterWithVehiclesBySubject(any(String.class)))
                .willReturn(Optional.of(transporterJpaEntity));

        EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
        given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

        given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

        ModelJpaEntity modelJpaEntity = defaultModelJpaEntityBuilder().build();
        ConstructorJpaEntity constructorJpaEntity = defaultConstructorJpaEntity();
        modelJpaEntity.setConstructor(constructorJpaEntity);

        given(modelRepository.findById(any(Long.class))).willReturn(Optional.of(modelJpaEntity));
        // when
        AddVehiculeDto addVehiculeDto = vehiclePersistenceAdapter.addVehicle(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
                LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

        // then

        then(transporterRepository).should(times(1)).save(transporterJpaEntity);

        Optional<VehicleJpaEntity> vehiculeJpaEntityOptional = transporterJpaEntity.getVehicles().stream()
                .filter(v -> v.getModel().getConstructor().getId().equals(1L) && v.getModel().getId().equals(1L)
                        && v.getCirculationDate().equals(LocalDate.of(2020, 01, 01))
                        && v.getRegistration().equals("0001 TUN 220"))
                .findFirst();

        assertThat(vehiculeJpaEntityOptional).isNotEmpty();

        assertEquals(constructorJpaEntity.getName(), addVehiculeDto.getConstructorName());
        assertEquals(modelJpaEntity.getName(), addVehiculeDto.getModelName());
        assertEquals(LocalDate.of(2020, 01, 01), addVehiculeDto.getCirculationDate());
        assertEquals("0001 TUN 220", addVehiculeDto.getRegistration());

    }


    @Test
    void givenVehicleHasImage_whenLoadImageLocation_thenReturnVehicleImageLocation() {
        // given
        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder
                .image(DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity()).build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));
        // when
        String location = vehiclePersistenceAdapter.loadImageLocation(1L);
        // then
        assertEquals(vehicle.getImage().getLocation(), location);
    }

    @Test
    void givenVehicleHasNoImage_whenLoadImageLocation_thenReturnNull() {
        // given
        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder.build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));
        // when
        String location = vehiclePersistenceAdapter.loadImageLocation(1L);
        // then
        assertNull(location);
    }

    @Test
    void givenVehicleNotFoundById_whenLoadImageLocation_thenReturnNull() {
        // given

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when
        String location = vehiclePersistenceAdapter.loadImageLocation(1L);
        // then
        assertNull(location);
    }

    @Test
    void givenVehicleHasNoImage_whenHasDefaultImage_thenReturnTrue() {
        // given
        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder.build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));

        // when //then
        assertTrue(vehiclePersistenceAdapter.hasDefaultVehiculeImage(1L));
    }

    @Test
    void givenVehicleHasImage_whenHasDefaultImage_thenReturnFalse() {
        // given
        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder
                .image(DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity()).build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));

        // when //then
        assertFalse(vehiclePersistenceAdapter.hasDefaultVehiculeImage(1L));
    }

    @Test
    void givenVehicleNotFoundById_whenHasDefaultImage_thenReturnFalse() {
        // given

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when // then
        assertFalse(vehiclePersistenceAdapter.hasDefaultVehiculeImage(1L));

    }

    @Test
    void givenTransporterNotFoundBySubject_whenUpdateVehicleImage_thenThrowUserAccountNotFoundException() {
        // given
        given(transporterRepository.findTransporterBySubject(any(String.class)))
                .willReturn(Optional.empty());
        // when //then
        assertThrows(UserAccountNotFoundException.class, () -> vehiclePersistenceAdapter
                .updateVehiculeImage(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, 1L, "someLocation", "someHash"));// then

    }

    @Test
    void givenVehicleNotFoundById_whenUpdateVehicleImage_thenThrowVehicleNotFoundException() {
        // given

        TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
        given(transporterRepository.findTransporterBySubject(any(String.class))).willReturn(Optional.of(transporter));
        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when //then
        assertThrows(VehicleNotFoundException.class, () -> vehiclePersistenceAdapter
                .updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash"));// then

    }

    @Test
    void givenVehicleWithDefaultImage_whenUpdateVehicleImage_thenDoNotDeleteOldImageAndSaveNewImageDocumentAndUpdateVehicleImage() {
        // given
        TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
        given(transporterRepository.findTransporterBySubject(any(String.class))).willReturn(Optional.of(transporter));

        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder.build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));

        DocumentJpaEntity document = DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity();

        given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(document);
        // When
        vehiclePersistenceAdapter.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash");
        // then
        then(documentRepository).should(never()).delete(any(DocumentJpaEntity.class));

        ArgumentCaptor<DocumentJpaEntity> documentCaptor = ArgumentCaptor.forClass(DocumentJpaEntity.class);

        then(documentRepository).should(times(1)).save(documentCaptor.capture());

        assertEquals(transporter, documentCaptor.getValue().getOwner());
        assertEquals("someLocation", documentCaptor.getValue().getLocation());
        assertEquals("someHash", documentCaptor.getValue().getHash());
        assertEquals(DocumentType.IMAGE_JPEG, documentCaptor.getValue().getType());
        assertEquals(false, documentCaptor.getValue().getIsDefault());
        assertEquals(3, documentCaptor.getValue().getEntitlements().size());
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.OWNER) && e.getRead() && e.getWrite()).count() == 1L);
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.OTHERS) && e.getRead() && !e.getWrite()).count() == 1L);
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.SUPPORT) && e.getRead() && e.getWrite()).count() == 1L);

        ArgumentCaptor<VehicleJpaEntity> vehiculeCaptor = ArgumentCaptor.forClass(VehicleJpaEntity.class);

        then(vehicleRepository).should(times(1)).save(vehiculeCaptor.capture());

        assertEquals(vehiculeCaptor.getValue().getImage(), document);

    }

    @Test
    void givenVehicleWithNonDefaultImage_whenUpdateVehicleImage_thenDeleteOldImageAndSaveNewImageDocumentAndUpdateVehicleImage() {
        // given
        TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
        given(transporterRepository.findTransporterBySubject(any(String.class))).willReturn(Optional.of(transporter));

        VehicleJpaEntity.VehicleJpaEntityBuilder vehicleBuilder = VehicleJpaEntityTestData.defaultVehicleJpaEntityBuilder();
        VehicleJpaEntity vehicle = vehicleBuilder
                .image(DocumentJpaTestData.nonDefaultVehicleImageDocumentJpaEntity()).build();

        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicle));

        DocumentJpaEntity document = DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity();

        given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(document);

        DocumentJpaEntity oldImage = vehicle.getImage();
        // When
        vehiclePersistenceAdapter.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash");

        // then
        then(documentRepository).should(times(1)).delete(oldImage);

        ArgumentCaptor<DocumentJpaEntity> documentCaptor = ArgumentCaptor.forClass(DocumentJpaEntity.class);

        then(documentRepository).should(times(1)).save(documentCaptor.capture());

        assertEquals(transporter, documentCaptor.getValue().getOwner());
        assertEquals("someLocation", documentCaptor.getValue().getLocation());
        assertEquals("someHash", documentCaptor.getValue().getHash());
        assertEquals(DocumentType.IMAGE_JPEG, documentCaptor.getValue().getType());
        assertEquals(false, documentCaptor.getValue().getIsDefault());
        assertEquals(3, documentCaptor.getValue().getEntitlements().size());
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.OWNER) && e.getRead() && e.getWrite()).count() == 1L);
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.OTHERS) && e.getRead() && !e.getWrite()).count() == 1L);
        assertTrue(documentCaptor.getValue().getEntitlements().stream()
                .filter(e -> e.getType().equals(EntitlementType.SUPPORT) && e.getRead() && e.getWrite()).count() == 1L);

        ArgumentCaptor<VehicleJpaEntity> vehiculeCaptor = ArgumentCaptor.forClass(VehicleJpaEntity.class);

        then(vehicleRepository).should(times(1)).save(vehiculeCaptor.capture());

        assertEquals(vehiculeCaptor.getValue().getImage(), document);

    }

}
