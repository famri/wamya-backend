package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.JourneyProposalDto.VehiculeDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class TransporterPersistenceAdapterTests {
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private VehiculeMapper vehiculeMapper;
	@InjectMocks
	private TransporterPersistenceAdapter transporterPersistenceAdapter;

	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver("https://domain-name:port/wamya-backend",
			"/documents");

	@Test
	void givenEmptyTransporterJpaEntity_WhenLoadTransporterVehiculesByEmail_ThenReturnEmptySet() {

		// given
		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.empty());
		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter
				.loadTransporterVehicules(TestConstants.DEFAULT_EMAIL);
		// then
		assertThat(vehicules).isEmpty();
	}

	@Test
	void givenEmptyTransporterJpaEntity_WhenLoadTransporterVehiculesByMobileNumber_ThenReturnEmptySet() {

		// given
		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(Optional.empty());
		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter.loadTransporterVehicules("+216", "220000001");
		// then
		assertThat(vehicules).isEmpty();
	}

	@Test
	void testLoadTransporterVehiculesByTransporterEmail() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		Set<VehiculeJpaEntity> vehiculeEntities = defaultVehiculeJpaEntitySet();
		vehiculeEntities.forEach(v -> transporterJpaEntity.addVehicule(v));

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));

		List<VehiculeJpaEntity> vehiculeJpaEntities = new ArrayList<>(3);
		List<VehiculeDto> vehiculeDtos = new ArrayList<>(3);
		Iterator<VehiculeJpaEntity> it = vehiculeEntities.iterator();
		while (it.hasNext()) {
			VehiculeJpaEntity v = it.next();
			vehiculeJpaEntities.add(v);
			VehiculeDto vehiculeDto = new VehiculeDto(v.getId(), v.getModel().getConstructor().getName(),
					v.getModel().getName(), getVehiculeImageUrl(v));
			vehiculeDtos.add(vehiculeDto);
			given(vehiculeMapper.mapToDomainEntity(v)).willReturn(vehiculeDto);
		}

		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter
				.loadTransporterVehicules(TestConstants.DEFAULT_EMAIL);
		// then

		assertTrue(vehiculeDtos.containsAll(vehicules) && vehiculeDtos.size() == vehicules.size());

	}

	@Test
	void testLoadTransporterEmptyVehiculesByTransporterEmail() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByEmail(any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));

		// when
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter
				.loadTransporterVehicules(TestConstants.DEFAULT_EMAIL);
		// then

		assertTrue(vehicules.isEmpty());

	}

	@Test
	void testLoadTransporterVehiculesByTransporterMobileNumber() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		Set<VehiculeJpaEntity> vehiculeEntities = defaultVehiculeJpaEntitySet();

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(Optional.of(transporterJpaEntity));

		List<VehiculeJpaEntity> vehiculeJpaEntities = new ArrayList<>(3);
		List<VehiculeDto> vehiculeDtos = new ArrayList<>(3);
		Iterator<VehiculeJpaEntity> it = vehiculeEntities.iterator();
		while (it.hasNext()) {

			VehiculeJpaEntity v = it.next();
			transporterJpaEntity.addVehicule(v);

			vehiculeJpaEntities.add(v);
			VehiculeDto vehiculeDto = new VehiculeDto(v.getId(), v.getModel().getConstructor().getName(),
					v.getModel().getName(), getVehiculeImageUrl(v));
			vehiculeDtos.add(vehiculeDto);
			given(vehiculeMapper.mapToDomainEntity(v)).willReturn(vehiculeDto);
		}

		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter.loadTransporterVehicules(mobileNumber[0],
				mobileNumber[1]);
		// then

		assertTrue(vehiculeDtos.containsAll(vehicules) && vehiculeDtos.size() == vehicules.size());

	}

	@Test
	void testLoadTransporterEmptyVehiculesByTransporterMobileNumber() {
		// given

		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();

		given(transporterRepository.findTransporterWithVehiculesByMobilePhoneNumber(any(String.class),
				any(String.class))).willReturn(Optional.of(transporterJpaEntity));

		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		Set<VehiculeDto> vehicules = transporterPersistenceAdapter.loadTransporterVehicules(mobileNumber[0],
				mobileNumber[1]);
		// then

		assertTrue(vehicules.isEmpty());

	}

	@Test
	void givenNullUsername_whenIsUserVehicule_thenReturnFalse() {
		// given

		// When

		// then
		assertFalse(transporterPersistenceAdapter.isUserVehicule(null, 1L));

	}

	@Test
	void givenNullVehiculeId_whenIsUserVehicule_thenReturnFalse() {
		// given

		// When

		// then
		assertFalse(transporterPersistenceAdapter.isUserVehicule(TestConstants.DEFAULT_EMAIL, null));

	}

	@Test
	void givenEmailUsername_whenIsUserVehicule_thenReturnTrue() {
		// given
		given(transporterRepository.existsByEmailAndVehiculeId(any(String.class), any(Long.class))).willReturn(true);
		// When

		// then
		assertTrue(transporterPersistenceAdapter.isUserVehicule(TestConstants.DEFAULT_EMAIL, 1L));

	}

	@Test
	void givenMobileNumberUsername_whenIsUserVehicule_thenReturnTrue() {
		// given
		given(transporterRepository.existsByIccAndMobileNumberAndVehiculeId(any(String.class), any(String.class),
				any(Long.class))).willReturn(true);
		// When

		// then
		assertTrue(transporterPersistenceAdapter.isUserVehicule(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, 1L));

	}

	@Test
	void givenBadUsername_whenIsUserVehicule_thenReturnFalse() {
		// given

		// When

		// then
		assertFalse(transporterPersistenceAdapter.isUserVehicule("BadUserName", 1L));

	}

	private String getVehiculeImageUrl(VehiculeJpaEntity vehiculeJpaEntity) {
		return vehiculeJpaEntity.getImage() != null
				? documentUrlResolver.resolveUrl(vehiculeJpaEntity.getImage().getId(),
						vehiculeJpaEntity.getImage().getHash())
				: documentUrlResolver.resolveUrl(vehiculeJpaEntity.getType().getImage().getId(),
						vehiculeJpaEntity.getType().getImage().getHash());
	}
}
