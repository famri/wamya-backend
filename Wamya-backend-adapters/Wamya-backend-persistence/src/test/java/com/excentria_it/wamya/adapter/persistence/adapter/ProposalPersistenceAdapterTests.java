package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.*;
import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.PeriodCriterion.PeriodValue;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyProposalTestData.defaultJourneyProposalDto;
import static com.excentria_it.wamya.test.data.common.JourneyProposalTestData.defaultTransporterProposalOutputList;
import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.defaultExistentJourneyRequestJpaEntity;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity;
import static com.excentria_it.wamya.test.data.common.VehicleJpaEntityTestData.defaultVehicleJpaEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ProposalPersistenceAdapterTests {
    @Mock
    private JourneyProposalRepository journeyProposalRepository;
    @Mock
    private JourneyRequestRepository journeyRequestRepository;
    @Mock
    private TransporterRepository transporterRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private JourneyProposalMapper journeyProposalMapper;
    @Mock
    private JourneyProposalStatusRepository journeyProposalStatusRepository;
    @InjectMocks
    private ProposalPersistenceAdapter proposalPersistenceAdapter;

    private static final Double JOURNEY_PRICE = 250.0;
    private static final Long VEHICLE_ID = 1L;
    private static final Long JOURNEY_REQUEST_ID = 1L;

    @Test
    void givenExistentTransporterBySubject_WhenMakeProposal_ThenSucceed() {
        // given
        TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
        VehicleJpaEntity vehicleJpaEntity = defaultVehicleJpaEntity();
        JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

        JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();

        given(transporterRepository.findTransporterBySubject(any(String.class))).willReturn(Optional.of(transporterJpaEntity));
        given(vehicleRepository.findById(any(Long.class))).willReturn(Optional.of(vehicleJpaEntity));

        given(journeyProposalMapper.mapToJpaEntity(any(Double.class), any(TransporterJpaEntity.class),
                any(VehicleJpaEntity.class))).willReturn(journeyProposalJpaEntity);
        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

        given(journeyProposalRepository.save(journeyProposalJpaEntity)).willReturn(journeyProposalJpaEntity);

        JourneyProposalStatusJpaEntity status = defaultJourneyProposalStatusJpaEntity();
        given(journeyProposalStatusRepository.findByCode(JourneyProposalStatusCode.SUBMITTED)).willReturn(status);

        // when
        proposalPersistenceAdapter.makeProposal("some-existent-transporter-uuid", JOURNEY_PRICE, VEHICLE_ID,
                JOURNEY_REQUEST_ID, "en_US");

        // then

        then(journeyProposalRepository).should(times(1)).save(journeyProposalJpaEntity);
        then(journeyRequestJpaEntity.getProposals().contains(journeyProposalJpaEntity));
        then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);

    }


    @Test
    void givenLoadJourneyProposalsCriteria_WhenLoadJourneyProposals_ThenSucceed() {
        // given

        List<JourneyProposalJpaEntity> list = List
                .of(JourneyProposalJpaEntityTestData.defaultJourneyProposalJpaEntity());

        given(journeyProposalRepository.findByJourneyRequest_Id(any(Long.class), any(Sort.class))).willReturn(list);

        JourneyProposalDto journeyProposalDto = JourneyProposalTestData.defaultJourneyProposalDto();
        given(journeyProposalMapper.mapToJourneyProposalDto(any(JourneyProposalJpaEntity.class), any(String.class)))
                .willReturn(journeyProposalDto);

        LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
                .statusCodes(Collections.emptyList()).build();

        // when
        JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria, "en_US");

        // then

        then(journeyProposalRepository).should(times(1)).findByJourneyRequest_Id(eq(criteria.getJourneyRequestId()),
                any(Sort.class));

        assertThat(result.getContent()).containsExactlyInAnyOrder(journeyProposalDto);
    }

    @Test
    void givenLoadJourneyProposalsCriteriaWithStatusCodes_WhenLoadJourneyProposals_ThenSucceed() {
        // given

        List<JourneyProposalJpaEntity> list = List
                .of(JourneyProposalJpaEntityTestData.defaultJourneyProposalJpaEntity());

        given(journeyProposalRepository.findByJourneyRequest_IdAndStatus_CodeIn(any(Long.class), any(List.class),
                any(Sort.class))).willReturn(list);

        JourneyProposalDto journeyProposalDto = JourneyProposalTestData.defaultJourneyProposalDto();
        given(journeyProposalMapper.mapToJourneyProposalDto(any(JourneyProposalJpaEntity.class), any(String.class)))
                .willReturn(journeyProposalDto);

        LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
                .build();

        // when
        JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria, "en_US");

        // then

        then(journeyProposalRepository).should(times(1)).findByJourneyRequest_IdAndStatus_CodeIn(
                eq(criteria.getJourneyRequestId()), eq(criteria.getStatusCodes().stream()
                        .map(s -> JourneyProposalStatusCode.valueOf(s.name())).collect(Collectors.toList())),
                any(Sort.class));

        assertThat(result.getContent()).containsExactlyInAnyOrder(journeyProposalDto);
    }

    @Test
    void givenNullJourneyProposalJpaEntityPage_WhenLoadJourneyProposals_ThenReturnEmptyJourneyRequestProposals() {
        // given

        given(journeyProposalRepository.findByJourneyRequest_Id(any(Long.class), any(Sort.class))).willReturn(null);

        LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
                .statusCodes(Collections.emptyList()).build();

        // when
        JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria, "en_US");

        // then

        then(journeyProposalRepository).should(times(1)).findByJourneyRequest_Id(eq(criteria.getJourneyRequestId()),
                any(Sort.class));

        assertThat(result.getTotalElements()).isEqualTo(0L);

        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void givenNullJourneyProposalJpaEntityPageAndCriteriaWithStatusCode_WhenLoadJourneyProposals_ThenReturnEmptyJourneyRequestProposals() {
        // given

        given(journeyProposalRepository.findByJourneyRequest_IdAndStatus_CodeIn(any(Long.class), any(List.class),
                any(Sort.class))).willReturn(null);

        LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
                .build();

        // when
        JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria, "en_US");

        // then

        then(journeyProposalRepository).should(times(1)).findByJourneyRequest_IdAndStatus_CodeIn(
                eq(criteria.getJourneyRequestId()), eq(criteria.getStatusCodes().stream()
                        .map(s -> JourneyProposalStatusCode.valueOf(s.name())).collect(Collectors.toList())),
                any(Sort.class));

        assertThat(result.getTotalElements()).isEqualTo(0L);
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void givenEmptyJourneyProposalJpaEntity_WhenLoadJourneyProposalByIdAndJourneyRequestId_ThenReturnEmptyJourneyProposalDto() {
        // given

        given(journeyProposalRepository.findByIdAndJourneyRequest_Id(any(Long.class), any(Long.class)))
                .willReturn(Optional.empty());

        // when
        Optional<JourneyProposalDto> journeyProposalDtoOptional = proposalPersistenceAdapter
                .loadJourneyProposalByIdAndJourneyRequestId(1L, 1L, "en_US");

        // then
        assertTrue(journeyProposalDtoOptional.isEmpty());
    }

    @Test
    void givenJourneyProposalJpaEntity_WhenLoadJourneyProposalByIdAndJourneyRequestId_ThenReturnJourneyProposalDtoOptional() {
        // given
        JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

        given(journeyProposalRepository.findByIdAndJourneyRequest_Id(any(Long.class), any(Long.class)))
                .willReturn(Optional.of(journeyProposalJpaEntity));

        JourneyProposalDto journeyProposalDto = defaultJourneyProposalDto();
        given(journeyProposalMapper.mapToJourneyProposalDto(journeyProposalJpaEntity, "en_US"))
                .willReturn(journeyProposalDto);
        // when
        Optional<JourneyProposalDto> journeyProposalDtoOptional = proposalPersistenceAdapter
                .loadJourneyProposalByIdAndJourneyRequestId(1L, 1L, "en_US");

        // then
        assertEquals(journeyProposalDto, journeyProposalDtoOptional.get());
    }

    @Test
    void givenEmptyJourneyRequestByID_WhenAcceptProposal_ThenReturnFalse() {
        // given
        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when
        boolean result = proposalPersistenceAdapter.acceptProposal(1L, 1L);
        // then
        assertFalse(result);
    }

    @Test
    void givenProposalIdNotInJourneyRequestProposalIds_WhenAcceptProposal_ThenReturnFalse() {

        // given
        JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

        // when
        boolean result = proposalPersistenceAdapter.acceptProposal(1L, 10L);

        // then
        assertFalse(result);

    }

    @Test
    void givenProposalInJourneyRequestProposalIds_WhenAcceptProposal_ThenReturnTrue() {

        // given
        JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
        JourneyProposalJpaEntity proposal1 = defaultJourneyProposalJpaEntityBuilder().id(1L).build();
        JourneyProposalJpaEntity proposal2 = defaultJourneyProposalJpaEntityBuilder().id(2L).build();

        journeyRequestJpaEntity.addProposal(proposal1);
        journeyRequestJpaEntity.addProposal(proposal2);

        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

        given(journeyProposalStatusRepository.findByCode(JourneyProposalStatusCode.ACCEPTED)).willReturn(
                defaultJourneyProposalStatusJpaEntityBuilder().code(JourneyProposalStatusCode.ACCEPTED).build());
        given(journeyProposalStatusRepository.findByCode(JourneyProposalStatusCode.REJECTED)).willReturn(
                defaultJourneyProposalStatusJpaEntityBuilder().code(JourneyProposalStatusCode.REJECTED).build());
        // when
        boolean result = proposalPersistenceAdapter.acceptProposal(1L, 1L);

        // then
        then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
        assertTrue(result);
        assertEquals(proposal1.getStatus().getCode(), JourneyProposalStatusCode.ACCEPTED);
        assertEquals(proposal2.getStatus().getCode(), JourneyProposalStatusCode.REJECTED);
    }

    @Test
    void givenEmptyJourneyRequestByID_WhenRejectProposal_ThenReturnFalse() {
        // given
        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.empty());
        // when
        boolean result = proposalPersistenceAdapter.rejectProposal(1L, 1L);
        // then
        assertFalse(result);
    }

    @Test
    void givenProposalIdNotInJourneyRequestProposalIds_WhenRejectProposal_ThenReturnFalse() {

        // given
        JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();

        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

        // when
        boolean result = proposalPersistenceAdapter.rejectProposal(1L, 2L);

        // then
        assertFalse(result);

    }

    @Test
    void givenProposalInJourneyRequestProposalIds_WhenRejectProposal_ThenReturnTrue() {

        // given
        JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
        JourneyProposalJpaEntity proposal1 = defaultJourneyProposalJpaEntityBuilder().id(1L).build();
        JourneyProposalJpaEntity proposal2 = defaultJourneyProposalJpaEntityBuilder().id(2L).build();

        journeyRequestJpaEntity.addProposal(proposal1);
        journeyRequestJpaEntity.addProposal(proposal2);

        given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

        given(journeyProposalStatusRepository.findByCode(JourneyProposalStatusCode.REJECTED)).willReturn(
                defaultJourneyProposalStatusJpaEntityBuilder().code(JourneyProposalStatusCode.REJECTED).build());
        // when
        boolean result = proposalPersistenceAdapter.rejectProposal(1L, 1L);

        // then
        then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
        assertTrue(result);
        assertEquals(proposal1.getStatus().getCode(), JourneyProposalStatusCode.REJECTED);
        assertEquals(proposal2.getStatus().getCode(), JourneyProposalStatusCode.SUBMITTED);
    }

    @Test
    void testExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode() {
        // given
        given(journeyProposalRepository.existsByIdAndJourneyRequestIdAndStatusCode(any(Long.class), any(Long.class),
                any(JourneyProposalStatusCode.class))).willReturn(true);
        // when
        boolean result = proposalPersistenceAdapter.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(1L,
                1L, StatusCode.SUBMITTED);
        // then
        assertTrue(result);
    }

    @Test
    void testNonExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode() {
        // given
        given(journeyProposalRepository.existsByIdAndJourneyRequestIdAndStatusCode(any(Long.class), any(Long.class),
                any(JourneyProposalStatusCode.class))).willReturn(false);
        // when
        boolean result = proposalPersistenceAdapter.isExistentJourneyProposalByIdAndJourneyRequestIdAndStatusCode(1L,
                1L, StatusCode.SUBMITTED);
        // then
        assertFalse(result);
    }

    @Test
    void testLoadTransporterNotificationInfo() {
        proposalPersistenceAdapter.loadTransportersNotificationInfo(1L);
        then(journeyProposalRepository).should(times(1)).loadTransportersNotificationInfo(1L);
    }

    @Test
    void testLoadTransporterProposals() {
        // given

        ZonedDateTime[] edges = PeriodValue.M1
                .calculateLowerAndHigherEdges(Instant.now().atZone(ZoneId.of("Africa/Tunis")));

        LoadTransporterProposalsCriteria criteria = LoadTransporterProposalsCriteria.builder()
                .transporterUsername(TestConstants.DEFAULT_EMAIL).pageNumber(0).pageSize(25)
                .statusCodes(Arrays.stream(JourneyProposalStatusCode.values()).collect(Collectors.toSet()))
                .sortingCriterion(new SortCriterion("date-time", "desc"))
                .periodCriterion(new PeriodCriterion("m1", edges[0], edges[1])).build();
        Page<JourneyProposalJpaEntity> jpPage = defaultJourneyProposalJpaEntityPage();

        given(journeyProposalRepository.findByTransporter_EmailAndJourneyDateTimeBetweenAndProposal_Status_Code(
                any(Instant.class), any(Instant.class), any(String.class), any(Set.class), any(String.class),
                any(Pageable.class))).willReturn(jpPage);

        given(journeyProposalMapper.mapToTransporterProposalOutput(jpPage.getContent().get(0), "en_US"))
                .willReturn(defaultTransporterProposalOutputList().get(0));
        given(journeyProposalMapper.mapToTransporterProposalOutput(jpPage.getContent().get(1), "en_US"))
                .willReturn(defaultTransporterProposalOutputList().get(1));

        // When
        TransporterProposalsOutput tpo = proposalPersistenceAdapter.loadTransporterProposals(criteria, "en_US");
        // then

        assertEquals(jpPage.getTotalPages(), tpo.getTotalPages());
        assertEquals(jpPage.getTotalElements(), tpo.getTotalElements());
        assertEquals(jpPage.getNumber(), tpo.getPageNumber());
        assertEquals(jpPage.getSize(), tpo.getPageSize());
        assertEquals(tpo.getContent().size(), 2);
        assertEquals(tpo.getContent().get(0), defaultTransporterProposalOutputList().get(0));
        assertEquals(tpo.getContent().get(1), defaultTransporterProposalOutputList().get(1));

    }

}
