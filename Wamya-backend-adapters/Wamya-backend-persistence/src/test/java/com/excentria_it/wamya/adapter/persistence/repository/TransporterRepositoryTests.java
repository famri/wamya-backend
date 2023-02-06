package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.domain.EngineTypeCode;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles(profiles = {"persistence-local"})
@AutoConfigureTestDatabase(replace = NONE)
public class TransporterRepositoryTests {

    @Autowired
    private TransporterRepository transporterRepository;

    @Autowired
    private EngineTypeRepository engineTypeRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ConstructorRepository constructorRepository;

    @Autowired
    private InternationalCallingCodeRepository internationalCallingCodeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void cleanDatabase() {
        transporterRepository.deleteAll();

        engineTypeRepository.deleteAll();

        vehicleRepository.deleteAll();
        modelRepository.deleteAll();
        constructorRepository.deleteAll();
        internationalCallingCodeRepository.deleteAll();
        documentRepository.deleteAll();
    }

    @Test
    void testFindByIcc_ValueAndMobileNumber() {
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterByIcc_ValueAndMobileNumber("+216", "22000001");

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals("22000001", transporterJpaEntity.getMobileNumber());
        assertEquals("+216", transporterJpaEntity.getIcc().getValue());
    }

    @Test
    void testFindByEmail() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterByEmail("transport1@gmail.com");

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals("transport1@gmail.com", transporterJpaEntity.getEmail());

    }

    @Test
    void givenNullSubject_WhenFindByNullSubjectEmail_ThenReturnEmpty() {

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject(null);

        // then
        assertTrue(transporterJpaEntityOptional.isEmpty());

    }

    @Test
    void testFindBySubjectEmail() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject(transporters.get(0).getEmail());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getEmail(), transporterJpaEntity.getEmail());

    }

    @Test
    void testFindBySubjectIccAndMobileNumber() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject(transporters.get(0).getIcc().getValue() + "_" + transporters.get(0).getMobileNumber());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();

        // then
        assertEquals(transporters.get(0).getIcc().getValue(), transporterJpaEntity.getIcc().getValue());
        assertEquals(transporters.get(0).getMobileNumber(), transporterJpaEntity.getMobileNumber());

    }

    @Test
    void testFindBySubjectOauthId() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject(transporters.get(0).getOauthId());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();

        // then
        assertEquals(transporters.get(0).getOauthId(), transporterJpaEntity.getOauthId());
    }

    @Test
    void givenNonExistentTransporter_WhenFindBySubjectEmail_ThenReturnEmpty() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject("non-existent-email@gmail.com");

        // then
        assertTrue(transporterJpaEntityOptional.isEmpty());

    }

    @Test
    void givenNonExistentTransporter_WhenFindBySubjectIccAndMobileNumber_ThenReturnEmpty() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject("+44_11111111111");

        // then
        assertTrue(transporterJpaEntityOptional.isEmpty());

    }

    @Test
    void givenNonExistentTransporter_WhenFindBySubjectOauthId_ThenReturnEmpty() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject("non-existent-oauth-id");

        // then
        assertTrue(transporterJpaEntityOptional.isEmpty());

    }

    @Test
    void testFindTransporterWithVehiclesByEmail() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesByEmail("transport1@gmail.com");

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals("transport1@gmail.com", transporterJpaEntity.getEmail());
        assertTrue(vehicles.get(0).containsAll(transporterJpaEntity.getVehicles())
                && vehicles.get(0).size() == transporterJpaEntity.getVehicles().size());
    }

    @Test
    void givenNonExistentTransporter_WhenFindTransporterWithVehiclesByEmail_ThenReturnEmpty() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesByEmail("transport4@gmail.com");

        // then

        assertTrue(transporterJpaEntityOptional.isEmpty());
    }

    @Test
    void testFindTransporterWithVehiclesBySubjectOauthId() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesBySubject(transporters.get(0).getOauthId());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getOauthId(), transporterJpaEntity.getOauthId());
        assertTrue(vehicles.get(0).containsAll(transporterJpaEntity.getVehicles())
                && vehicles.get(0).size() == transporterJpaEntity.getVehicles().size());
    }

    @Test
    void givenNonExistentTransporter_WhenFindTransporterWithVehiclesBySubjectOauthId_ThenReturnEmpty() {

        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesByEmail("non-existent-oauth-id");

        // then

        assertTrue(transporterJpaEntityOptional.isEmpty());
    }

    @Test
    void testFindTransporterWithVehiclesBySubjectEmail() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesBySubject(transporters.get(0).getEmail());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getEmail(), transporterJpaEntity.getEmail());
        assertTrue(vehicles.get(0).containsAll(transporterJpaEntity.getVehicles())
                && vehicles.get(0).size() == transporterJpaEntity.getVehicles().size());
    }

    @Test
    void givenNonExistentTransporter_WhenFindTransporterWithVehiclesBySubjectEmail_ThenReturnEmpty() {

        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesByEmail("non-existent-email@gmail.com");

        // then

        assertTrue(transporterJpaEntityOptional.isEmpty());
    }


    @Test
    void testFindTransporterWithVehiclesBySubjectIccAndMobileNumber() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesBySubject(transporters.get(0).getIcc().getValue() + "_" + transporters.get(0).getMobileNumber());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getIcc().getValue(), transporterJpaEntity.getIcc().getValue());
        assertEquals(transporters.get(0).getMobileNumber(), transporterJpaEntity.getMobileNumber());
        assertTrue(vehicles.get(0).containsAll(transporterJpaEntity.getVehicles())
                && vehicles.get(0).size() == transporterJpaEntity.getVehicles().size());
    }

    @Test
    void givenFrenchMobileNumberWithLeadingZero_WhenFindTransporterWithVehiclesBySubjectIccAndMobileNumber_ThenReturnTransporterWithVehicles() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        // given
        List<TransporterJpaEntity> transporters = givenTransportersWithoutLeadingZeroMobileNumber(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesBySubject(transporters.get(0).getIcc().getValue() + "_0" + transporters.get(0).getMobileNumber());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getIcc().getValue(), transporterJpaEntity.getIcc().getValue());
        assertEquals(transporters.get(0).getMobileNumber(), transporterJpaEntity.getMobileNumber());
        assertTrue(vehicles.get(0).containsAll(transporterJpaEntity.getVehicles())
                && vehicles.get(0).size() == transporterJpaEntity.getVehicles().size());
    }

    @Test
    void givenFrenchMobileNumberWithLeadingZero_WhenFindTransporterBySubjectIccAndMobileNumber_ThenReturnTransporter() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        // given
        List<TransporterJpaEntity> transporters = givenTransportersWithoutLeadingZeroMobileNumber(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterBySubject(transporters.get(0).getIcc().getValue() + "_0" + transporters.get(0).getMobileNumber());

        TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
        // then

        assertEquals(transporters.get(0).getIcc().getValue(), transporterJpaEntity.getIcc().getValue());
        assertEquals(transporters.get(0).getMobileNumber(), transporterJpaEntity.getMobileNumber());
      
    }

    @Test
    void givenNonExistentTransporter_WhenFindTransporterWithVehiclesBySubjectIccAndMobileNumber_ThenReturnEmpty() {

        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        givenTransporters(vehicles, icc);

        // when
        Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
                .findTransporterWithVehiclesBySubject("+44_1414141414");

        // then

        assertTrue(transporterJpaEntityOptional.isEmpty());
    }

    @Test
    void testExistsByNullSubjectAndVehicleId() {
        // given
        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId(null, 1L);
        // then
        assertFalse(exists);
    }

    @Test
    void testExistsBySubjectAndNullVehicleId() {
        // given
        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId("some-transporter-oauth-id", null);
        // then
        assertFalse(exists);
    }

    @Test
    void testExistsByEmailSubjectAndVehicleId() {
        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);
        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId(transporters.get(0).getEmail(), transporters.get(0).getVehicles().stream().findFirst().get().getId());

        // then
        assertTrue(exists);
    }

    @Test
    void testExistsByMobileNumberSubjectAndVehicleId() {
        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);
        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId(transporters.get(0).getIcc().getValue() + "_" + transporters.get(0).getMobileNumber(), transporters.get(0).getVehicles().stream().findFirst().get().getId());


        // then
        assertTrue(exists);
    }

    @Test
    void testExistsByOauthIdSubjectAndVehicleId() {
        // given
        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");

        List<TransporterJpaEntity> transporters = givenTransporters(vehicles, icc);
        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId(transporters.get(0).getOauthId(), transporters.get(0).getVehicles().stream().findFirst().get().getId());


        // then
        assertTrue(exists);
    }

    @Test
    void givenFrenchMobileNumberWithLeadingZero_WhenExistsByIccAndMobileNumberSubject_ThenReturnTransporterWithVehicles() {

        List<List<ConstructorJpaEntity>> constructors = givenConstructors();
        List<List<ModelJpaEntity>> models = givenModels(constructors);
        List<List<DocumentJpaEntity>> images = givenImages();
        List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
        List<List<VehicleJpaEntity>> vehicles = givenVehicles(engineTypes, models);
        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        // given
        List<TransporterJpaEntity> transporters = givenTransportersWithoutLeadingZeroMobileNumber(vehicles, icc);

        // when
        boolean exists = transporterRepository
                .existsBySubjectAndVehicleId(transporters.get(0).getIcc().getValue() + "_0" + transporters.get(0).getMobileNumber(), transporters.get(0).getVehicles().stream().findFirst().get().getId());

        // then
        assertTrue(exists);
    }

    private List<TransporterJpaEntity> givenTransporters(List<List<VehicleJpaEntity>> vehicles,
                                                         InternationalCallingCodeJpaEntity icc) {

        TransporterJpaEntity t1 = new TransporterJpaEntity(null, "oauthId1", null, "Transporter1", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport1@gmail.com", null, null, icc,
                "22000001", null, null, null, null, null, null, null, null, null);
        vehicles.get(0).forEach(v -> t1.addVehicle(v));

        TransporterJpaEntity t2 = new TransporterJpaEntity(null, "oauthId2", null, "Transporter2", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport2@gmail.com", null, null, icc,
                "22000002", null, null, null, null, null, null, null, null, null);
        vehicles.get(1).forEach(v -> t2.addVehicle(v));

        TransporterJpaEntity t3 = new TransporterJpaEntity(null, "oauthId3", null, "Transporter3", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport3@gmail.com", null, null, icc,
                "22000003", null, null, null, null, null, null, null, null, null);
        vehicles.get(2).forEach(v -> t3.addVehicle(v));

        List<TransporterJpaEntity> transporters = List.of(t1, t2, t3);

        return transporterRepository.saveAll(transporters);

    }

    private List<TransporterJpaEntity> givenTransportersWithoutLeadingZeroMobileNumber(List<List<VehicleJpaEntity>> vehicles,
                                                                                       InternationalCallingCodeJpaEntity icc) {

        TransporterJpaEntity t1 = new TransporterJpaEntity(null, "oauthId1", null, "Transporter1", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport1@gmail.com", null, null, icc,
                "711111111", null, null, null, null, null, null, null, null, null);
        vehicles.get(0).forEach(v -> t1.addVehicle(v));

        TransporterJpaEntity t2 = new TransporterJpaEntity(null, "oauthId2", null, "Transporter2", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport2@gmail.com", null, null, icc,
                "722222222", null, null, null, null, null, null, null, null, null);
        vehicles.get(1).forEach(v -> t2.addVehicle(v));

        TransporterJpaEntity t3 = new TransporterJpaEntity(null, "oauthId3", null, "Transporter3", null,
                ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport3@gmail.com", null, null, icc,
                "733333333", null, null, null, null, null, null, null, null, null);
        vehicles.get(2).forEach(v -> t3.addVehicle(v));

        List<TransporterJpaEntity> transporters = List.of(t1, t2, t3);

        return transporterRepository.saveAll(transporters);

    }


    private List<List<VehicleJpaEntity>> givenVehicles(List<List<EngineTypeJpaEntity>> engineTypes,
                                                       List<List<ModelJpaEntity>> models) {
        List<VehicleJpaEntity> vehicles1 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(0)).model(models.get(0).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(1)).model(models.get(0).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 02)).registration("2 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(0).get(2)).model(models.get(0).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 03)).registration("3 TUN 220").build());

        List<VehicleJpaEntity> vehicles2 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(0)).model(models.get(1).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 11)).registration("11 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(1)).model(models.get(1).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 12)).registration("12 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(1).get(2)).model(models.get(1).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 13)).registration("13 TUN 220").build());

        List<VehicleJpaEntity> vehicles3 = List.of(
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(0)).model(models.get(2).get(0))
                        .circulationDate(LocalDate.of(2020, 01, 21)).registration("21 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(1)).model(models.get(2).get(1))
                        .circulationDate(LocalDate.of(2020, 01, 22)).registration("22 TUN 220").build(),
                VehicleJpaEntity.builder().type(engineTypes.get(2).get(2)).model(models.get(2).get(2))
                        .circulationDate(LocalDate.of(2020, 01, 23)).registration("23 TUN 220").build());

        vehicles1 = vehicleRepository.saveAll(vehicles1);
        vehicles2 = vehicleRepository.saveAll(vehicles2);
        vehicles3 = vehicleRepository.saveAll(vehicles3);

        return List.of(vehicles1, vehicles2, vehicles3);
    }

    private List<List<EngineTypeJpaEntity>> givenEngineTypes(List<List<DocumentJpaEntity>> images) {
        // Engine type 11
        EngineTypeJpaEntity et11 = new EngineTypeJpaEntity();
        et11.setCode(EngineTypeCode.VAN_L1H1);

        LocalizedEngineTypeJpaEntity let11en = new LocalizedEngineTypeJpaEntity();
        let11en.setLocalizedId(new LocalizedId("en_US"));
        let11en.setEngineType(et11);
        let11en.setName("EngineType11");
        let11en.setDescription("EngineTypeDescription11");
        et11.getLocalizations().put("en_US", let11en);

        LocalizedEngineTypeJpaEntity let11fr = new LocalizedEngineTypeJpaEntity();
        let11fr.setLocalizedId(new LocalizedId("fr_FR"));
        let11fr.setEngineType(et11);
        let11fr.setName("TypeVehicle11");
        let11fr.setDescription("DescriptionTypeVehicle11");
        et11.getLocalizations().put("fr_FR", let11fr);

        et11.setImage(images.get(0).get(0));

        // Engine type 12
        EngineTypeJpaEntity et12 = new EngineTypeJpaEntity();
        et12.setCode(EngineTypeCode.VAN_L2H2);

        LocalizedEngineTypeJpaEntity let12en = new LocalizedEngineTypeJpaEntity();
        let12en.setLocalizedId(new LocalizedId("en_US"));
        let12en.setEngineType(et12);
        let12en.setName("EngineType12");
        let12en.setDescription("EngineTypeDescription12");
        et12.getLocalizations().put("en_US", let12en);

        LocalizedEngineTypeJpaEntity let12fr = new LocalizedEngineTypeJpaEntity();
        let12fr.setLocalizedId(new LocalizedId("fr_FR"));
        let12fr.setEngineType(et12);
        let12fr.setName("TypeVehicle12");
        let12fr.setDescription("DescriptionTypeVehicle12");
        et12.getLocalizations().put("fr_FR", let12fr);

        et12.setImage(images.get(0).get(1));

        // Engine type 13
        EngineTypeJpaEntity et13 = new EngineTypeJpaEntity();
        et13.setCode(EngineTypeCode.VAN_L3H3);

        LocalizedEngineTypeJpaEntity let13en = new LocalizedEngineTypeJpaEntity();
        let13en.setLocalizedId(new LocalizedId("en_US"));
        let13en.setEngineType(et13);
        let13en.setName("EngineType13");
        let13en.setDescription("EngineTypeDescription13");
        et13.getLocalizations().put("en_US", let13en);

        LocalizedEngineTypeJpaEntity let13fr = new LocalizedEngineTypeJpaEntity();
        let13fr.setLocalizedId(new LocalizedId("fr_FR"));
        let13fr.setEngineType(et13);
        let13fr.setName("TypeVehicle13");
        let13fr.setDescription("DescriptionTypeVehicle3");
        et13.getLocalizations().put("fr_FR", let13fr);

        et13.setImage(images.get(0).get(2));

        // Engine type 21
        EngineTypeJpaEntity et21 = new EngineTypeJpaEntity();
        et21.setCode(EngineTypeCode.FLATBED_TRUCK);

        LocalizedEngineTypeJpaEntity let21en = new LocalizedEngineTypeJpaEntity();
        let21en.setLocalizedId(new LocalizedId("en_US"));
        let21en.setEngineType(et21);
        let21en.setName("EngineType21");
        let21en.setDescription("EngineTypeDescription21");
        et21.getLocalizations().put("en_US", let21en);

        LocalizedEngineTypeJpaEntity let21fr = new LocalizedEngineTypeJpaEntity();
        let21fr.setLocalizedId(new LocalizedId("fr_FR"));
        let21fr.setEngineType(et21);
        let21fr.setName("TypeVehicle21");
        let21fr.setDescription("DescriptionTypeVehicle21");
        et21.getLocalizations().put("fr_FR", let21fr);

        et21.setImage(images.get(1).get(0));

        // Engine type 22
        EngineTypeJpaEntity et22 = new EngineTypeJpaEntity();
        et22.setCode(EngineTypeCode.UTILITY);

        LocalizedEngineTypeJpaEntity let22en = new LocalizedEngineTypeJpaEntity();
        let22en.setLocalizedId(new LocalizedId("en_US"));
        let22en.setEngineType(et22);
        let22en.setName("EngineType22");
        let22en.setDescription("EngineTypeDescription22");
        et22.getLocalizations().put("en_US", let22en);

        LocalizedEngineTypeJpaEntity let22fr = new LocalizedEngineTypeJpaEntity();
        let22fr.setLocalizedId(new LocalizedId("fr_FR"));
        let22fr.setEngineType(et22);
        let22fr.setName("TypeVehicle22");
        let22fr.setDescription("DescriptionTypeVehicle22");
        et22.getLocalizations().put("fr_FR", let22fr);

        et22.setImage(images.get(1).get(1));

        // Engine type 23
        EngineTypeJpaEntity et23 = new EngineTypeJpaEntity();
        et23.setCode(EngineTypeCode.DUMP_TRUCK);

        LocalizedEngineTypeJpaEntity let23en = new LocalizedEngineTypeJpaEntity();
        let23en.setLocalizedId(new LocalizedId("en_US"));
        let23en.setEngineType(et23);
        let23en.setName("EngineType23");
        let23en.setDescription("EngineTypeDescription23");
        et23.getLocalizations().put("en_US", let23en);

        LocalizedEngineTypeJpaEntity let23fr = new LocalizedEngineTypeJpaEntity();
        let23fr.setLocalizedId(new LocalizedId("fr_FR"));
        let23fr.setEngineType(et23);
        let23fr.setName("TypeVehicle23");
        let23fr.setDescription("DescriptionTypeVehicle23");
        et23.getLocalizations().put("fr_FR", let23fr);

        et23.setImage(images.get(1).get(2));

        // Engine type 31
        EngineTypeJpaEntity et31 = new EngineTypeJpaEntity();
        et31.setCode(EngineTypeCode.BOX_TRUCK);

        LocalizedEngineTypeJpaEntity let31en = new LocalizedEngineTypeJpaEntity();
        let31en.setLocalizedId(new LocalizedId("en_US"));
        let31en.setEngineType(et31);
        let31en.setName("EngineType31");
        let31en.setDescription("EngineTypeDescription31");
        et31.getLocalizations().put("en_US", let31en);

        LocalizedEngineTypeJpaEntity let31fr = new LocalizedEngineTypeJpaEntity();
        let31fr.setLocalizedId(new LocalizedId("fr_FR"));
        let31fr.setEngineType(et31);
        let31fr.setName("TypeVehicle31");
        let31fr.setDescription("DescriptionTypeVehicle31");
        et31.getLocalizations().put("fr_FR", let31fr);

        et31.setImage(images.get(2).get(0));

        // Engine type 32
        EngineTypeJpaEntity et32 = new EngineTypeJpaEntity();
        et32.setCode(EngineTypeCode.TANKER);

        LocalizedEngineTypeJpaEntity let32en = new LocalizedEngineTypeJpaEntity();
        let32en.setLocalizedId(new LocalizedId("en_US"));
        let32en.setEngineType(et32);
        let32en.setName("EngineType32");
        let32en.setDescription("EngineTypeDescription32");
        et32.getLocalizations().put("en_US", let32en);

        LocalizedEngineTypeJpaEntity let32fr = new LocalizedEngineTypeJpaEntity();
        let32fr.setLocalizedId(new LocalizedId("fr_FR"));
        let32fr.setEngineType(et32);
        let32fr.setName("TypeVehicle32");
        let32fr.setDescription("DescriptionTypeVehicle32");
        et32.getLocalizations().put("fr_FR", let32fr);

        et32.setImage(images.get(2).get(1));

        // Engine type 33
        EngineTypeJpaEntity et33 = new EngineTypeJpaEntity();
        et33.setCode(EngineTypeCode.BUS);

        LocalizedEngineTypeJpaEntity let33en = new LocalizedEngineTypeJpaEntity();
        let33en.setLocalizedId(new LocalizedId("en_US"));
        let33en.setEngineType(et33);
        let33en.setName("EngineType33");
        let33en.setDescription("EngineTypeDescription33");
        et33.getLocalizations().put("en_US", let33en);

        LocalizedEngineTypeJpaEntity let33fr = new LocalizedEngineTypeJpaEntity();
        let33fr.setLocalizedId(new LocalizedId("fr_FR"));
        let33fr.setEngineType(et33);
        let33fr.setName("TypeVehicle33");
        let33fr.setDescription("DescriptionTypeVehicle3");
        et33.getLocalizations().put("fr_FR", let33fr);

        et33.setImage(images.get(2).get(2));

        List<EngineTypeJpaEntity> engineTypes1 = List.of(et11, et12, et13);
        List<EngineTypeJpaEntity> engineTypes2 = List.of(et21, et22, et23);
        List<EngineTypeJpaEntity> engineTypes3 = List.of(et31, et32, et33);

        engineTypes1 = engineTypeRepository.saveAll(engineTypes1);
        engineTypes2 = engineTypeRepository.saveAll(engineTypes2);
        engineTypes3 = engineTypeRepository.saveAll(engineTypes3);

        return List.of(engineTypes1, engineTypes2, engineTypes3);
    }

    private List<List<ConstructorJpaEntity>> givenConstructors() {
        List<ConstructorJpaEntity> constructors1 = List.of(ConstructorJpaEntity.builder().name("Constructor11").build(),
                ConstructorJpaEntity.builder().name("Constructor12").build(),
                ConstructorJpaEntity.builder().name("Constructor13").build());
        List<ConstructorJpaEntity> constructors2 = List.of(ConstructorJpaEntity.builder().name("Constructor21").build(),
                ConstructorJpaEntity.builder().name("Constructor22").build(),
                ConstructorJpaEntity.builder().name("Constructor23").build());
        List<ConstructorJpaEntity> constructors3 = List.of(ConstructorJpaEntity.builder().name("Constructor31").build(),
                ConstructorJpaEntity.builder().name("Constructor32").build(),
                ConstructorJpaEntity.builder().name("Constructor33").build());

        constructors1 = constructorRepository.saveAll(constructors1);
        constructors2 = constructorRepository.saveAll(constructors2);
        constructors3 = constructorRepository.saveAll(constructors3);

        return List.of(constructors1, constructors2, constructors3);
    }

    private List<List<DocumentJpaEntity>> givenImages() {

        DocumentJpaEntity i11 = DocumentJpaTestData.defaultVanL1H1VehicleImageDocumentJpaEntity();
        i11.setId(null);
        DocumentJpaEntity i12 = DocumentJpaTestData.defaultVanL2H2VehicleImageDocumentJpaEntity();
        i12.setId(null);
        DocumentJpaEntity i13 = DocumentJpaTestData.defaultVanL3H3VehicleImageDocumentJpaEntity();
        i13.setId(null);

        DocumentJpaEntity i21 = DocumentJpaTestData.defaultFlatbedTruckVehicleImageDocumentJpaEntity();
        i21.setId(null);
        DocumentJpaEntity i22 = DocumentJpaTestData.defaultUtilityVehicleImageDocumentJpaEntity();
        i22.setId(null);
        DocumentJpaEntity i23 = DocumentJpaTestData.defaultDumpTruckVehicleImageDocumentJpaEntity();
        i23.setId(null);

        DocumentJpaEntity i31 = DocumentJpaTestData.defaultBoxTruckVehicleImageDocumentJpaEntity();
        i31.setId(null);
        DocumentJpaEntity i32 = DocumentJpaTestData.defaultTankerVehicleImageDocumentJpaEntity();
        i32.setId(null);
        DocumentJpaEntity i33 = DocumentJpaTestData.defaultBusVehicleImageDocumentJpaEntity();
        i33.setId(null);

        List<DocumentJpaEntity> images1 = documentRepository.saveAll(List.of(i11, i12, i13));
        List<DocumentJpaEntity> images2 = documentRepository.saveAll(List.of(i21, i22, i23));
        List<DocumentJpaEntity> images3 = documentRepository.saveAll(List.of(i31, i32, i33));

        return List.of(images1, images2, images3);

    }

    private List<List<ModelJpaEntity>> givenModels(List<List<ConstructorJpaEntity>> constructors) {

        List<ModelJpaEntity> models1 = List.of(
                ModelJpaEntity.builder().name("Model11").constructor(constructors.get(0).get(0)).build(),
                ModelJpaEntity.builder().name("Model12").constructor(constructors.get(0).get(1)).build(),
                ModelJpaEntity.builder().name("Model13").constructor(constructors.get(0).get(2)).build());
        List<ModelJpaEntity> models2 = List.of(
                ModelJpaEntity.builder().name("Model21").constructor(constructors.get(1).get(0)).build(),
                ModelJpaEntity.builder().name("Model22").constructor(constructors.get(1).get(1)).build(),
                ModelJpaEntity.builder().name("Model23").constructor(constructors.get(1).get(2)).build());
        List<ModelJpaEntity> models3 = List.of(
                ModelJpaEntity.builder().name("Model31").constructor(constructors.get(2).get(0)).build(),
                ModelJpaEntity.builder().name("Model32").constructor(constructors.get(2).get(1)).build(),
                ModelJpaEntity.builder().name("Model33").constructor(constructors.get(2).get(2)).build());

        models1 = modelRepository.saveAll(models1);
        models2 = modelRepository.saveAll(models2);
        models3 = modelRepository.saveAll(models3);

        return List.of(models1, models2, models3);

    }

    private InternationalCallingCodeJpaEntity givenIcc(String code) {
        InternationalCallingCodeJpaEntity icc = InternationalCallingCodeJpaEntity.builder().value(code).enabled(true)
                .build();
        return internationalCallingCodeRepository.save(icc);
    }

}
