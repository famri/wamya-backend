package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static com.excentria_it.wamya.test.data.common.VehiculeJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyProposalMapper;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyProposalRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.VehiculeRepository;
import com.excentria_it.wamya.domain.JourneyProposalDto;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.domain.LoadJourneyProposalsCriteria;
import com.excentria_it.wamya.test.data.common.JourneyProposalJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
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

	@Test
	void givenLoadJourneyProposalsCriteria_WhenLoadJourneyProposals_ThenScceed() {
		// given

		Page<JourneyProposalJpaEntity> page = createPageFromJourneyProposalJpaEntities(
				List.of(JourneyProposalJpaEntityTestData.defaultJourneyProposalJpaEntity()));

		given(journeyProposalRepository.findByJourneyRequest_Id(any(Long.class), any(Pageable.class))).willReturn(page);

		JourneyProposalDto journeyProposalDto = JourneyProposalTestData.defaultJourneyProposalDto();
		given(journeyProposalMapper.mapToDomainEntity(any(JourneyProposalJpaEntity.class)))
				.willReturn(journeyProposalDto);

		LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
				.build();

		// when
		JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria);

		// then

		then(journeyProposalRepository).should(times(1)).findByJourneyRequest_Id(eq(criteria.getJourneyRequestId()),
				any(Pageable.class));

		assertThat(result.getPageNumber()).isEqualTo(criteria.getPageNumber());

		assertThat(result.getPageSize()).isEqualTo(criteria.getPageSize());

		assertThat(result.getContent()).containsExactlyInAnyOrder(journeyProposalDto);
	}

	@Test
	void givenNullJourneyProposalJpaEntityPage_WhenLoadJourneyProposals_ThenReturnEmptyJourneyRequestProposals() {
		// given

		

		given(journeyProposalRepository.findByJourneyRequest_Id(any(Long.class), any(Pageable.class))).willReturn(null);

		LoadJourneyProposalsCriteria criteria = JourneyProposalTestData.defaultLoadJourneyProposalsCriteriaBuilder()
				.build();

		// when
		JourneyRequestProposals result = proposalPersistenceAdapter.loadJourneyProposals(criteria);

		// then

		then(journeyProposalRepository).should(times(1)).findByJourneyRequest_Id(eq(criteria.getJourneyRequestId()),
				any(Pageable.class));

		assertThat(result.getPageNumber()).isEqualTo(criteria.getPageNumber());

		assertThat(result.getPageSize()).isEqualTo(criteria.getPageSize());
		assertThat(result.getTotalElements()).isEqualTo(0L);
		assertThat(result.getTotalPages()).isEqualTo(0L);
		assertThat(result.isHasNext()).isEqualTo(false);
		assertThat(result.getContent()).isEmpty();
	}

	private Page<JourneyProposalJpaEntity> createPageFromJourneyProposalJpaEntities(
			List<JourneyProposalJpaEntity> journeyProposalJpaEntities) {

		return new Page<JourneyProposalJpaEntity>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 25;
			}

			@Override
			public int getNumberOfElements() {

				return journeyProposalJpaEntities.size();
			}

			@Override
			public List<JourneyProposalJpaEntity> getContent() {

				return journeyProposalJpaEntities;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return Sort.by(List.of(new Order(Direction.ASC, "price")));
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return false;
			}

			@Override
			public boolean hasNext() {

				return true;
			}

			@Override
			public boolean hasPrevious() {

				return false;
			}

			@Override
			public Pageable nextPageable() {

				return null;
			}

			@Override
			public Pageable previousPageable() {

				return null;
			}

			@Override
			public Iterator<JourneyProposalJpaEntity> iterator() {

				return journeyProposalJpaEntities.iterator();
			}

			@Override
			public int getTotalPages() {

				return 1;
			}

			@Override
			public long getTotalElements() {

				return 2;
			}

			@Override
			public <U> Page<U> map(Function<? super JourneyProposalJpaEntity, ? extends U> converter) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
