package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadTransporterVehiculesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicules;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class TransporterPersistenceAdapterTests {
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private VehiculeMapper vehiculeMapper;
	@InjectMocks
	private TransporterPersistenceAdapter transporterPersistenceAdapter;

	private DocumentUrlResolver documentUrlResolver = new DocumentUrlResolver();

	@BeforeEach
	void initDocumentUrlResolver() {
		documentUrlResolver.setServerBaseUrl("https://domain-name:port/wamya-backend");
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
	void givenBadUsername_whenIsUserVehicule_thenReturnFalse() {
		// given

		// When

		// then
		assertFalse(transporterPersistenceAdapter.isUserVehicule("BadUserName", 1L));

	}

	@Test
	void testLoadTransporterVehicules() {
		// given
		LoadTransporterVehiculesCriteria criteria = defaultLoadTransporterVehiculesCriteriaBuilder().build();

		given(transporterRepository.findTransporterVehiculesByEmail(any(String.class), any(String.class),
				any(Sort.class))).willReturn(defaultTransporterVehiculeOutputList());

		given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehiculeOutputList().get(0))))
				.willReturn(defaultTransporterVehicules().getContent().get(0));

		given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehiculeOutputList().get(1))))
				.willReturn(defaultTransporterVehicules().getContent().get(1));

		Sort sort = convertToSort(criteria.getSortingCriterion());

		// when
		TransporterVehicules transporterVehicules = transporterPersistenceAdapter.loadTransporterVehicules(criteria,
				"en_US");
		// then
		then(transporterRepository).should(times(1)).findTransporterVehiculesByEmail(criteria.getTransporterUsername(),
				"en_US", sort);

		assertEquals(defaultTransporterVehicules().getContent().get(0), transporterVehicules.getContent().get(0));
		assertEquals(defaultTransporterVehicules().getContent().get(1), transporterVehicules.getContent().get(1));

	}

	private Sort convertToSort(SortCriterion sortingCriterion) {

		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}
}
