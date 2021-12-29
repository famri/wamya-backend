package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.TransporterRatingRequestRecordMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRatingRequestRecordRepository;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;
import com.excentria_it.wamya.domain.UserPreferenceKey;
import com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestJpaTestData;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestTestData;

@ExtendWith(MockitoExtension.class)
public class TransporterRatingRequestPersistenceAdapterTests {

	@Mock
	private TransporterRatingRequestRecordRepository transporterRatingRequestRecordRepository;
	@Mock
	private TransporterRatingRequestRecordMapper transporterRatingDetailsMapper;
	@Mock
	private JourneyRequestRepository journeyRequestRepository;

	@InjectMocks
	private TransporterRatingRequestPersistenceAdapter transporterRatingRequestPersistenceAdapter;

	@Test
	void givenEmptyTransporterRatingDetailsJpaEntity_WhenLoadTransporterRatingDetails_ThenReturnOptionalEmpty() {
		// given
		given(transporterRatingRequestRecordRepository.findByHashAndStatusAndClient_Id(any(String.class),
				any(TransporterRatingRequestStatus.class), any(Long.class))).willReturn(Optional.empty());
		// when
		Optional<TransporterRatingRequestRecordOutput> TransporterRatingDetailsOutputOptional = transporterRatingRequestPersistenceAdapter
				.loadRecord("SOME_HASH", 1L, "fr_FR");
		// then
		assertTrue(TransporterRatingDetailsOutputOptional.isEmpty());
	}

	@Test
	void givenNotEmptyTransporterRatingDetailsJpaEntity_WhenLoadTransporterRatingDetails_ThenReturnOptionalTransporterRatingDetailsOutput() {
		// given

		TransporterRatingRequestRecordOutput trdo = TransporterRatingRequestTestData
				.defaultTransporterRatingRequestRecordOutput();

		TransporterRatingRequestRecordJpaEntity trdje = TransporterRatingRequestJpaTestData
				.defaultTransporterRatingRequestRecordJpaEntity();

		given(transporterRatingRequestRecordRepository.findByHashAndStatusAndClient_Id(any(String.class),
				any(TransporterRatingRequestStatus.class), any(Long.class))).willReturn(Optional.of(trdje));

		given(transporterRatingDetailsMapper.mapToDomainEntity(trdje, "fr_FR")).willReturn(trdo);

		// when
		Optional<TransporterRatingRequestRecordOutput> transporterRatingDetailsOutputOptional = transporterRatingRequestPersistenceAdapter
				.loadRecord("SOME_HASH", 1L, "fr_FR");
		// then
		assertEquals(trdo, transporterRatingDetailsOutputOptional.get());
	}

	@Test
	void testCreateTransporterRatingRequests() {
		// given
		JourneyRequestJpaEntity journeyRequest = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntityWithAcceptedProposal();
		given(journeyRequestRepository.findByIds(any(Set.class))).willReturn(Set.of(journeyRequest));

		// when
		transporterRatingRequestPersistenceAdapter.createTransporterRatingRequests(Set.of(1L), List.of("H1"));
		// then

		ArgumentCaptor<TransporterRatingRequestRecordJpaEntity> captor = ArgumentCaptor
				.forClass(TransporterRatingRequestRecordJpaEntity.class);

		then(transporterRatingRequestRecordRepository).should(times(1)).save(captor.capture());

		assertEquals(journeyRequest, captor.getValue().getJourenyRequest());
		assertEquals(journeyRequest.getClient(), captor.getValue().getClient());
		assertEquals(journeyRequest.getProposals().stream()
				.filter(p -> p.getStatus().getCode().equals(JourneyProposalStatusCode.ACCEPTED)).findFirst().get()
				.getTransporter(), captor.getValue().getTransporter());

		assertEquals("H1", captor.getValue().getHash());
		assertEquals(0, captor.getValue().getRevivals());
		assertEquals(TransporterRatingRequestStatus.SAVED, captor.getValue().getStatus());

	}

	@Test
	void testLoadUnfulfilledRecords() {
		// given

		TransporterRatingRequestRecordJpaEntity t = TransporterRatingRequestJpaTestData
				.defaultTransporterRatingRequestRecordJpaEntity();
		given(transporterRatingRequestRecordRepository
				.findByStatusAndRevivalsLessThan(any(TransporterRatingRequestStatus.class), any(Integer.class)))
						.willReturn(Set.of(t));

		TransporterRatingRequestRecordOutput o = TransporterRatingRequestTestData
				.defaultTransporterRatingRequestRecordOutput();
		given(transporterRatingDetailsMapper.mapToDomainEntity(t,
				t.getClient().getPreferenceValue(UserPreferenceKey.LOCALE))).willReturn(o);
		// when

		Set<TransporterRatingRequestRecordOutput> result = transporterRatingRequestPersistenceAdapter
				.loadUnfulfilledRecords(3);

		// then

		assertEquals(Set.of(o), result);
	}

	@Test
	void testIncrementRevives() {
		// given

		// when
		transporterRatingRequestPersistenceAdapter.incrementRevivals(Set.of(1L, 2L, 3L));
		// then
		then(transporterRatingRequestRecordRepository).should(times(1)).incrementRevivals(Set.of(1L, 2L, 3L));

	}

	@Test
	void testUpdateStatus() {
		// given

		// when
		transporterRatingRequestPersistenceAdapter.updateRequestStatus(1L, TransporterRatingRequestStatus.FULFILLED);
		// then
		then(transporterRatingRequestRecordRepository).should(times(1)).updateStatus(1L,
				TransporterRatingRequestStatus.FULFILLED);
	}

}
