package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.ConstructorJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.EngineTypeJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.ModelJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity.VehiculeJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.repository.DocumentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.VehiculeNotFoundException;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData;

@ExtendWith(MockitoExtension.class)
public class VehiculePersistenceAdapterTests {

	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@Mock
	private ModelRepository modelRepository;
	@Mock
	private VehiculeRepository vehiculeRepository;
	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private VehiculePersistenceAdapter vehiculePersistenceAdapter;

	@Test
	void givenBadTransporterEmailAndVehiculeConstructorNameAndModelName_WhenAddVehicule_ThenReturnNull() {
		// given
		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.empty());
		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL,
				"Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then
		assertNull(addVehiculeDto);

	}

	@Test
	void givenBadTransporterEmailAndVehiculeConstructorIdAndModelId_WhenAddVehicule_ThenReturnNull() {
		// given
		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.empty());
		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
				LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then
		assertNull(addVehiculeDto);

	}

	@Test
	void givenBadEngineTypeIdAndVehiculeConstructorNameAndModelName_WhenAddVehicule_ThenReturnNull() {
		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL,
				"Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then
		assertNull(addVehiculeDto);

	}

	@Test
	void givenBadEngineTypeIdAndVehiculeConstructorIdAndModelId_WhenAddVehicule_ThenReturnNull() {
		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
				LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then
		assertNull(addVehiculeDto);

	}

	@Test
	void givenBadModelIdAndVehiculeConstructorIdAndModelId_WhenAddVehicule_ThenReturnNull() {
		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

		given(modelRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
				LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then
		assertNull(addVehiculeDto);

	}

	@Test
	void givenTransporterEmailAndVehiculeConstructorNameAndModelName_WhenAddVehicule_ThenSucceed() {

		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

		given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL,
				"Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);

		Optional<VehiculeJpaEntity> vehiculeJpaEntityOptional = transporterJpaEntity.getVehicules().stream()
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
	void givenTransporterEmailAndVehiculeConstructorIdAndModelId_WhenAddVehicule_ThenSucceed() {

		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

		given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

		ModelJpaEntity modelJpaEntity = defaultModelJpaEntityBuilder().build();
		ConstructorJpaEntity constructorJpaEntity = defaultConstructorJpaEntity();
		modelJpaEntity.setConstructor(constructorJpaEntity);

		given(modelRepository.findById(any(Long.class))).willReturn(Optional.of(modelJpaEntity));
		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(TestConstants.DEFAULT_EMAIL, 1L, 1L, 1L,
				LocalDate.of(2020, 01, 01), "0001 TUN 220", "en_US");

		// then

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);

		Optional<VehiculeJpaEntity> vehiculeJpaEntityOptional = transporterJpaEntity.getVehicules().stream()
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
	void givenVehiculeHasImage_whenLoadImageLocation_thenReturnVehiculeImageLocation() {
		// given
		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder
				.image(DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity()).build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));
		// when
		String location = vehiculePersistenceAdapter.loadImageLocation(1L);
		// then
		assertEquals(vehicule.getImage().getLocation(), location);
	}

	@Test
	void givenVehiculeHasNoImage_whenLoadImageLocation_thenReturnNull() {
		// given
		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder.build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));
		// when
		String location = vehiculePersistenceAdapter.loadImageLocation(1L);
		// then
		assertNull(location);
	}

	@Test
	void givenVehiculeNotFoundById_whenLoadImageLocation_thenReturnNull() {
		// given

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		String location = vehiculePersistenceAdapter.loadImageLocation(1L);
		// then
		assertNull(location);
	}

	@Test
	void givenVehiculeHasNoImage_whenHasDefaultImage_thenReturnTrue() {
		// given
		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder.build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));

		// when //then
		assertTrue(vehiculePersistenceAdapter.hasDefaultVehiculeImage(1L));
	}

	@Test
	void givenVehiculeHasImage_whenHasDefaultImage_thenReturnFalse() {
		// given
		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder
				.image(DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity()).build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));

		// when //then
		assertFalse(vehiculePersistenceAdapter.hasDefaultVehiculeImage(1L));
	}

	@Test
	void givenVehiculeNotFoundById_whenHasDefaultImage_thenReturnFalse() {
		// given

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when // then
		assertFalse(vehiculePersistenceAdapter.hasDefaultVehiculeImage(1L));

	}

	@Test
	void givenTransporterNotFoundByEmailUsername_whenUpdateVehiculeImage_thenThrowUserAccountNotFoundException() {
		// given
		given(transporterRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
		// when //then
		assertThrows(UserAccountNotFoundException.class, () -> vehiculePersistenceAdapter
				.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash"));// then

	}

	@Test
	void givenTransporterNotFoundByMobileNumberUsername_whenUpdateVehiculeImage_thenThrowUserAccountNotFoundException() {
		// given
		given(transporterRepository.findByIcc_ValueAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(UserAccountNotFoundException.class, () -> vehiculePersistenceAdapter
				.updateVehiculeImage(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, 1L, "someLocation", "someHash"));// then

	}

	@Test
	void givenVehiculeNotFoundById_whenUpdateVehiculeImage_thenThrowVehiculeNotFoundException() {
		// given

		TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
		given(transporterRepository.findByEmail(any(String.class))).willReturn(Optional.of(transporter));
		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when //then
		assertThrows(VehiculeNotFoundException.class, () -> vehiculePersistenceAdapter
				.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash"));// then

	}

	@Test
	void givenVehiculeWithDefaultImage_whenUpdateVehiculeImage_thenDoNotDeleteOldImageAndSaveNewImageDocumentAndUpdateVehiculeImage() {
		// given
		TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
		given(transporterRepository.findByEmail(any(String.class))).willReturn(Optional.of(transporter));

		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder.build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));

		DocumentJpaEntity document = DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity();

		given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(document);
		// When
		vehiculePersistenceAdapter.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash");
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

		ArgumentCaptor<VehiculeJpaEntity> vehiculeCaptor = ArgumentCaptor.forClass(VehiculeJpaEntity.class);

		then(vehiculeRepository).should(times(1)).save(vehiculeCaptor.capture());

		assertEquals(vehiculeCaptor.getValue().getImage(), document);

	}

	@Test
	void givenVehiculeWithNonDefaultImage_whenUpdateVehiculeImage_thenDeleteOldImageAndSaveNewImageDocumentAndUpdateVehiculeImage() {
		// given
		TransporterJpaEntity transporter = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
		given(transporterRepository.findByEmail(any(String.class))).willReturn(Optional.of(transporter));

		VehiculeJpaEntityBuilder vehiculeBuilder = VehiculeJpaEntityTestData.defaultVehiculeJpaEntityBuilder();
		VehiculeJpaEntity vehicule = vehiculeBuilder
				.image(DocumentJpaTestData.nonDefaultVehiculeImageDocumentJpaEntity()).build();

		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehicule));

		DocumentJpaEntity document = DocumentJpaTestData.defaultVanL1H1VehiculeImageDocumentJpaEntity();

		given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(document);

		DocumentJpaEntity oldImage = vehicule.getImage();
		// When
		vehiculePersistenceAdapter.updateVehiculeImage(TestConstants.DEFAULT_EMAIL, 1L, "someLocation", "someHash");

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

		ArgumentCaptor<VehiculeJpaEntity> vehiculeCaptor = ArgumentCaptor.forClass(VehiculeJpaEntity.class);

		then(vehiculeRepository).should(times(1)).save(vehiculeCaptor.capture());

		assertEquals(vehiculeCaptor.getValue().getImage(), document);

	}

}
