package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyProposalRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class ProposalPersistenceAdapterTests {
	@Mock
	private JourneyProposalRepository journeyProposalRepository;
	@Mock
	private JourneyRequestRepository journeyRequestRepository;
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private VehiculeRepository vehiculeRepository;
	@Mock
	private JourneyProposalMapper journeyProposalMapper;
	@InjectMocks
	private ProposalPersistenceAdapter proposalPersistenceAdapter;

	private static final Double JOURNEY_PRICE = 250.0;
	private static final Long VEHICULE_ID = 1L;
	private static final Long JOURNEY_REQUEST_ID = 1L;

	@Test
	void givenEmailUsername_WhenMakeProposal_ThenScceed() {
		// given
		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();
		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		

		given(transporterRepository.findByEmail(any(String.class))).willReturn(Optional.of(transporterJpaEntity));
		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehiculeJpaEntity));

		given(journeyProposalMapper.mapToJpaEntity(any(Double.class), any(TransporterJpaEntity.class),
				any(VehiculeJpaEntity.class))).willReturn(journeyProposalJpaEntity);
		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		given(journeyProposalRepository.save(journeyProposalJpaEntity)).willReturn(journeyProposalJpaEntity);
		// when
		proposalPersistenceAdapter.makeProposal(TestConstants.DEFAULT_EMAIL, JOURNEY_PRICE, VEHICULE_ID,
				JOURNEY_REQUEST_ID);

		// then

		then(journeyProposalRepository).should(times(1)).save(journeyProposalJpaEntity);
		then(journeyRequestJpaEntity.getProposals().contains(journeyProposalJpaEntity));
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);

	}

	@Test
	void givenMobileNumberUsername_WhenMakeProposal_ThenScceed() {
		// given
		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		VehiculeJpaEntity vehiculeJpaEntity = defaultVehiculeJpaEntity();
		JourneyProposalJpaEntity journeyProposalJpaEntity = defaultJourneyProposalJpaEntity();

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
	

		given(transporterRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(transporterJpaEntity));
		given(vehiculeRepository.findById(any(Long.class))).willReturn(Optional.of(vehiculeJpaEntity));

		given(journeyProposalMapper.mapToJpaEntity(any(Double.class), any(TransporterJpaEntity.class),
				any(VehiculeJpaEntity.class))).willReturn(journeyProposalJpaEntity);
		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		given(journeyProposalRepository.save(journeyProposalJpaEntity)).willReturn(journeyProposalJpaEntity);
		// when
		proposalPersistenceAdapter.makeProposal(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, JOURNEY_PRICE,
				VEHICULE_ID, JOURNEY_REQUEST_ID);

		// then

		then(journeyProposalRepository).should(times(1)).save(journeyProposalJpaEntity);
		then(journeyRequestJpaEntity.getProposals().contains(journeyProposalJpaEntity));
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);

	}
}
