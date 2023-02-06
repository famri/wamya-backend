package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.mapper.VehiculeMapper;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadTransporterVehiclesCriteria;
import com.excentria_it.wamya.domain.TransporterVehicles;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import static com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData.defaultLoadTransporterVehiclesCriteriaBuilder;
import static com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData.defaultTransporterVehicleOutputList;
import static com.excentria_it.wamya.test.data.common.VehiculeTestData.defaultTransporterVehicles;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

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
    void givenNullSubject_whenIsUserVehicle_thenReturnFalse() {
        // given

        // When

        // then
        assertFalse(transporterPersistenceAdapter.isUserVehicle(null, 1L));

    }

    @Test
    void givenNullVehicleId_whenIsUserVehicle_thenReturnFalse() {
        // given

        // When

        // then
        assertFalse(transporterPersistenceAdapter.isUserVehicle(TestConstants.DEFAULT_EMAIL, null));

    }

    @Test
    void givenExistentUserVehicle_whenIsUserVehicle_thenReturnTrue() {
        // given
        given(transporterRepository.existsBySubjectAndVehicleId(any(String.class), any(Long.class))).willReturn(true);
        // When

        // then
        assertTrue(transporterPersistenceAdapter.isUserVehicle("user-oauth-id", 1L));

    }


    @Test
    void testLoadTransporterVehicles() {
        // given
        LoadTransporterVehiclesCriteria criteria = defaultLoadTransporterVehiclesCriteriaBuilder().build();

        given(transporterRepository.findTransporterVehiclesByEmail(any(String.class), any(String.class),
                any(Sort.class))).willReturn(defaultTransporterVehicleOutputList());

        given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehicleOutputList().get(0))))
                .willReturn(defaultTransporterVehicles().getContent().get(0));

        given(vehiculeMapper.mapToDomainEntity(eq(defaultTransporterVehicleOutputList().get(1))))
                .willReturn(defaultTransporterVehicles().getContent().get(1));

        Sort sort = convertToSort(criteria.getSortingCriterion());

        // when
        TransporterVehicles transporterVehicules = transporterPersistenceAdapter.loadTransporterVehicules(criteria,
                "en_US");
        // then
        then(transporterRepository).should(times(1)).findTransporterVehiclesByEmail(criteria.getTransporterUsername(),
                "en_US", sort);

        assertEquals(defaultTransporterVehicles().getContent().get(0), transporterVehicules.getContent().get(0));
        assertEquals(defaultTransporterVehicles().getContent().get(1), transporterVehicules.getContent().get(1));

    }

    private Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));
    }
}
