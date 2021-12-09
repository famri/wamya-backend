package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.domain.EngineTypeCode;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class TransporterRepositoryTests {

	@Autowired
	private TransporterRepository transporterRepository;

	@Autowired
	private EngineTypeRepository engineTypeRepository;

	@Autowired
	private VehiculeRepository vehiculeRepository;

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

		vehiculeRepository.deleteAll();
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
		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypes, models);
		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenTransporters(vehicules, icc);

		// when
		Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
				.findByIcc_ValueAndMobileNumber("+216", "22000001");

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
		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypes, models);
		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenTransporters(vehicules, icc);

		// when
		Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
				.findByEmail("transport1@gmail.com");

		TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
		// then

		assertEquals("transport1@gmail.com", transporterJpaEntity.getEmail());

	}

	@Test
	void testFindTransporterWithVehiculesByEmail() {

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);
		List<List<DocumentJpaEntity>> images = givenImages();
		List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypes, models);
		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenTransporters(vehicules, icc);

		// when
		Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
				.findTransporterWithVehiculesByEmail("transport1@gmail.com");

		TransporterJpaEntity transporterJpaEntity = transporterJpaEntityOptional.get();
		// then

		assertEquals("transport1@gmail.com", transporterJpaEntity.getEmail());
		assertTrue(vehicules.get(0).containsAll(transporterJpaEntity.getVehicules())
				&& vehicules.get(0).size() == transporterJpaEntity.getVehicules().size());
	}

	@Test
	void givenInexistentTransporter_WhenFindTransporterWithVehiculesByEmail_ThenReturnEmpty() {

		List<List<ConstructorJpaEntity>> constructors = givenConstructors();
		List<List<ModelJpaEntity>> models = givenModels(constructors);
		List<List<DocumentJpaEntity>> images = givenImages();
		List<List<EngineTypeJpaEntity>> engineTypes = givenEngineTypes(images);
		List<List<VehiculeJpaEntity>> vehicules = givenVehicules(engineTypes, models);
		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenTransporters(vehicules, icc);

		// when
		Optional<TransporterJpaEntity> transporterJpaEntityOptional = transporterRepository
				.findTransporterWithVehiculesByEmail("transport4@gmail.com");

		// then

		assertTrue(transporterJpaEntityOptional.isEmpty());
	}

	private List<TransporterJpaEntity> givenTransporters(List<List<VehiculeJpaEntity>> vehicules,
			InternationalCallingCodeJpaEntity icc) {

		TransporterJpaEntity t1 = new TransporterJpaEntity(null, null, null, "Transporter1", null,
				ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport1@gmail.com", null, null, icc,
				"22000001", null, null, null, null, null, null, null, null, null);
		vehicules.get(0).forEach(v -> t1.addVehicule(v));

		TransporterJpaEntity t2 = new TransporterJpaEntity(null, null, null, "Transporter2", null,
				ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport2@gmail.com", null, null, icc,
				"22000002", null, null, null, null, null, null, null, null, null);
		vehicules.get(1).forEach(v -> t2.addVehicule(v));

		TransporterJpaEntity t3 = new TransporterJpaEntity(null, null, null, "Transporter3", null,
				ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO, null, "transport3@gmail.com", null, null, icc,
				"22000003", null, null, null, null, null, null, null, null, null);
		vehicules.get(2).forEach(v -> t3.addVehicule(v));

		List<TransporterJpaEntity> transporters = List.of(t1, t2, t3);

		return transporterRepository.saveAll(transporters);

	}

	private List<List<VehiculeJpaEntity>> givenVehicules(List<List<EngineTypeJpaEntity>> engineTypes,
			List<List<ModelJpaEntity>> models) {
		List<VehiculeJpaEntity> vehicules1 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(0)).model(models.get(0).get(0))
						.circulationDate(LocalDate.of(2020, 01, 01)).registration("1 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(1)).model(models.get(0).get(1))
						.circulationDate(LocalDate.of(2020, 01, 02)).registration("2 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(0).get(2)).model(models.get(0).get(2))
						.circulationDate(LocalDate.of(2020, 01, 03)).registration("3 TUN 220").build());

		List<VehiculeJpaEntity> vehicules2 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(0)).model(models.get(1).get(0))
						.circulationDate(LocalDate.of(2020, 01, 11)).registration("11 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(1)).model(models.get(1).get(1))
						.circulationDate(LocalDate.of(2020, 01, 12)).registration("12 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(1).get(2)).model(models.get(1).get(2))
						.circulationDate(LocalDate.of(2020, 01, 13)).registration("13 TUN 220").build());

		List<VehiculeJpaEntity> vehicules3 = List.of(
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(0)).model(models.get(2).get(0))
						.circulationDate(LocalDate.of(2020, 01, 21)).registration("21 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(1)).model(models.get(2).get(1))
						.circulationDate(LocalDate.of(2020, 01, 22)).registration("22 TUN 220").build(),
				VehiculeJpaEntity.builder().type(engineTypes.get(2).get(2)).model(models.get(2).get(2))
						.circulationDate(LocalDate.of(2020, 01, 23)).registration("23 TUN 220").build());

		vehicules1 = vehiculeRepository.saveAll(vehicules1);
		vehicules2 = vehiculeRepository.saveAll(vehicules2);
		vehicules3 = vehiculeRepository.saveAll(vehicules3);

		return List.of(vehicules1, vehicules2, vehicules3);
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
		let11fr.setName("TypeVehicule11");
		let11fr.setDescription("DescriptionTypeVehicule11");
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
		let12fr.setName("TypeVehicule12");
		let12fr.setDescription("DescriptionTypeVehicule12");
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
		let13fr.setName("TypeVehicule13");
		let13fr.setDescription("DescriptionTypeVehicule3");
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
		let21fr.setName("TypeVehicule21");
		let21fr.setDescription("DescriptionTypeVehicule21");
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
		let22fr.setName("TypeVehicule22");
		let22fr.setDescription("DescriptionTypeVehicule22");
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
		let23fr.setName("TypeVehicule23");
		let23fr.setDescription("DescriptionTypeVehicule23");
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
		let31fr.setName("TypeVehicule31");
		let31fr.setDescription("DescriptionTypeVehicule31");
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
		let32fr.setName("TypeVehicule32");
		let32fr.setDescription("DescriptionTypeVehicule32");
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
		let33fr.setName("TypeVehicule33");
		let33fr.setDescription("DescriptionTypeVehicule3");
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

		DocumentJpaEntity i11 = DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity();
		i11.setId(null);
		DocumentJpaEntity i12 = DocumentJpaTestData.defaultVanL2H2VehiculeImageDocumentJpaEntity();
		i12.setId(null);
		DocumentJpaEntity i13 = DocumentJpaTestData.defaultVanL3H3VehiculeImageDocumentJpaEntity();
		i13.setId(null);

		DocumentJpaEntity i21 = DocumentJpaTestData.defaultFlatbedTruckVehiculeImageDocumentJpaEntity();
		i21.setId(null);
		DocumentJpaEntity i22 = DocumentJpaTestData.defaultUtilityVehiculeImageDocumentJpaEntity();
		i22.setId(null);
		DocumentJpaEntity i23 = DocumentJpaTestData.defaultDumpTruckVehiculeImageDocumentJpaEntity();
		i23.setId(null);

		DocumentJpaEntity i31 = DocumentJpaTestData.defaultBoxTruckVehiculeImageDocumentJpaEntity();
		i31.setId(null);
		DocumentJpaEntity i32 = DocumentJpaTestData.defaultTankerVehiculeImageDocumentJpaEntity();
		i32.setId(null);
		DocumentJpaEntity i33 = DocumentJpaTestData.defaultBusVehiculeImageDocumentJpaEntity();
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
