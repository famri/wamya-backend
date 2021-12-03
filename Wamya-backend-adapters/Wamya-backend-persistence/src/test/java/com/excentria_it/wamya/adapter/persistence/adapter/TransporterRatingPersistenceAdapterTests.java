package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;

@ExtendWith(MockitoExtension.class)
public class TransporterRatingPersistenceAdapterTests {
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private ClientRepository clientRepository;
	@InjectMocks
	private TransporterRatingPersistenceAdapter transporterRatingPersistenceAdapter;

	@Test
	void testSaveRating() {
		// given
		TransporterJpaEntity transporterJpaEntity = UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity();
		ClientJpaEntity clientJpaEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		given(transporterRepository.findById(any(Long.class))).willReturn(Optional.of(transporterJpaEntity));
		given(clientRepository.findById(any(Long.class))).willReturn(Optional.of(clientJpaEntity));

		ArgumentCaptor<TransporterJpaEntity> captor = ArgumentCaptor.forClass(TransporterJpaEntity.class);
		// when

		transporterRatingPersistenceAdapter.saveRating(clientJpaEntity.getId(), 5, "SOME COMMENT",
				transporterJpaEntity.getId());
		// then

		then(transporterRepository).should(times(1)).save(captor.capture());

		assertEquals(clientJpaEntity.getId(),
				captor.getValue().getRatings().stream().findFirst().get().getUser().getId());
		assertEquals(5, captor.getValue().getRatings().stream().findFirst().get().getValue());
		assertEquals("SOME COMMENT", captor.getValue().getRatings().stream().findFirst().get().getComment());

		assertEquals(transporterJpaEntity.getId(), captor.getValue().getId());
	}
}
