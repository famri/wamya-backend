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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
				any(Pageable.class))).willReturn(defaultTransporterVehiculeOutputPage());

		given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehiculeOutputPage().getContent().get(0))))
				.willReturn(defaultTransporterVehicules().getContent().get(0));

		given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehiculeOutputPage().getContent().get(1))))
				.willReturn(defaultTransporterVehicules().getContent().get(1));

		Sort sort = convertToSort(criteria.getSortingCriterion());
		Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);
		// when
		TransporterVehicules transporterVehicules = transporterPersistenceAdapter.loadTransporterVehicules(criteria,
				"en_US");
		// then
		then(transporterRepository).should(times(1)).findTransporterVehiculesByEmail(criteria.getTransporterUsername(),
				"en_US", pagingSort);

		assertEquals(defaultTransporterVehiculeOutputPage().getNumber(), transporterVehicules.getPageNumber());
		assertEquals(defaultTransporterVehiculeOutputPage().getTotalPages(), transporterVehicules.getTotalPages());
		assertEquals(defaultTransporterVehiculeOutputPage().getTotalElements(),
				transporterVehicules.getTotalElements());

		assertEquals(defaultTransporterVehiculeOutputPage().getSize(), transporterVehicules.getPageSize());

		assertEquals(defaultTransporterVehiculeOutputPage().hasNext(), transporterVehicules.isHasNext());

		assertEquals(defaultTransporterVehicules().getContent().get(0), transporterVehicules.getContent().get(0));
		assertEquals(defaultTransporterVehicules().getContent().get(1), transporterVehicules.getContent().get(1));

	}

	@Test
	void givenEmptyVehiculesPage_whenLoadTransporterVehicules_thenReturnTransporterVehiculesContent() {
		// given
		LoadTransporterVehiculesCriteria criteria = defaultLoadTransporterVehiculesCriteriaBuilder().build();

		given(transporterRepository.findTransporterVehiculesByEmail(any(String.class), any(String.class),
				any(Pageable.class))).willReturn(emptyTransporterVehiculeOutputPage());

		Sort sort = convertToSort(criteria.getSortingCriterion());
		Pageable pagingSort = PageRequest.of(criteria.getPageNumber(), criteria.getPageSize(), sort);
		// when
		TransporterVehicules transporterVehicules = transporterPersistenceAdapter.loadTransporterVehicules(criteria,
				"en_US");
		// then
		then(transporterRepository).should(times(1)).findTransporterVehiculesByEmail(criteria.getTransporterUsername(),
				"en_US", pagingSort);

		assertEquals(criteria.getPageNumber(), transporterVehicules.getPageNumber());
		assertEquals(0, transporterVehicules.getTotalPages());
		assertEquals(0, transporterVehicules.getTotalElements());

		assertEquals(criteria.getPageSize(), transporterVehicules.getPageSize());

		assertEquals(false, transporterVehicules.isHasNext());

		assertTrue(transporterVehicules.getContent().isEmpty());

	}

	private Sort convertToSort(SortCriterion sortingCriterion) {

		return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
				ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
	}
}
