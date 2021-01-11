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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.domain.AddVehiculeDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class VehiculePersistenceAdapterTests {

	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@Mock
	private ModelRepository modelRepository;
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
	void givenTransporterMobileNumberAndVehiculeConstructorNameAndModelName_WhenAddVehicule_ThenSucceed() {

		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(Optional.of(transporterJpaEntity));

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

		given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "Constructor1", "Model1", 1L, LocalDate.of(2020, 01, 01),
				"0001 TUN 220", "en_US");

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
	void givenTransporterMobileNumberAndVehiculeConstructorIdAndModelId_WhenAddVehicule_ThenSucceed() {

		// given
		TransporterJpaEntity transporterJpaEntity = defaultNewTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(Optional.of(transporterJpaEntity));

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.of(engineTypeJpaEntity));

		given(transporterRepository.save(any(TransporterJpaEntity.class))).willReturn(transporterJpaEntity);

		ModelJpaEntity modelJpaEntity = defaultModelJpaEntityBuilder().build();
		ConstructorJpaEntity constructorJpaEntity = defaultConstructorJpaEntity();
		modelJpaEntity.setConstructor(constructorJpaEntity);

		given(modelRepository.findById(any(Long.class))).willReturn(Optional.of(modelJpaEntity));
		// when
		AddVehiculeDto addVehiculeDto = vehiculePersistenceAdapter.addVehicule(
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, 1L, 1L, 1L, LocalDate.of(2020, 01, 01), "0001 TUN 220",
				"en_US");

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

}
