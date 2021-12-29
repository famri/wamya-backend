package com.excentria_it.wamya.test.data.common;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity.JourneyProposalJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedJourneyProposalStatusJpaEntity;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;

public class JourneyProposalJpaEntityTestData {

	public static JourneyProposalJpaEntityBuilder defaultJourneyProposalJpaEntityBuilder() {
		return JourneyProposalJpaEntity.builder().id(1L).price(250.0)
				.creationDateTime(LocalDateTime.now(ZoneOffset.UTC)).status(defaultJourneyProposalStatusJpaEntity())
				.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity());
	}

	public static JourneyProposalJpaEntity defaultJourneyProposalJpaEntity() {
		return JourneyProposalJpaEntity.builder().id(10L).price(250.0)
				.creationDateTime(LocalDateTime.now(ZoneOffset.UTC)).status(defaultJourneyProposalStatusJpaEntity())
				.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
				.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity()).build();
	}

	public static JourneyProposalStatusJpaEntity defaultJourneyProposalStatusJpaEntity() {

		LocalizedJourneyProposalStatusJpaEntity localizedJourneyProposalStatusJpaEntity = LocalizedJourneyProposalStatusJpaEntity
				.builder().value("submitted").localizedId(new LocalizedId(1L, "en_US")).build();

		return JourneyProposalStatusJpaEntity.builder().id(1L).code(JourneyProposalStatusCode.SUBMITTED)
				.description("Journey proposal was submitted to client. He should accept or reject the proposal.")
				.localizations(Map.of("en_US", localizedJourneyProposalStatusJpaEntity)).build();

	}

	public static JourneyProposalStatusJpaEntityBuilder defaultJourneyProposalStatusJpaEntityBuilder() {

		LocalizedJourneyProposalStatusJpaEntity localizedJourneyProposalStatusJpaEntity = LocalizedJourneyProposalStatusJpaEntity
				.builder().value("submitted").localizedId(new LocalizedId(1L, "en_US")).build();

		return JourneyProposalStatusJpaEntity.builder().id(1L).code(JourneyProposalStatusCode.SUBMITTED)
				.description("Journey proposal was submitted to client. He should accept or reject the proposal.")
				.localizations(Map.of("en_US", localizedJourneyProposalStatusJpaEntity));

	}

	private static final List<JourneyProposalJpaEntity> journeyProposalJpaEntities = List.of(
			JourneyProposalJpaEntity.builder().id(10L).price(350.0).creationDateTime(LocalDateTime.now(ZoneOffset.UTC))
					.status(defaultJourneyProposalStatusJpaEntity())
					.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
					.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity())
					.journeyRequest(JourneyRequestJpaTestData.defaultExistentJourneyRequestJpaEntity()).build(),
			JourneyProposalJpaEntity.builder().id(20L).price(250.0).creationDateTime(LocalDateTime.now(ZoneOffset.UTC))
					.status(defaultJourneyProposalStatusJpaEntityBuilder().code(JourneyProposalStatusCode.ACCEPTED)
							.id(2L).build())
					.transporter(UserAccountJpaEntityTestData.defaultExistentTransporterJpaEntity())
					.vehicule(VehiculeJpaEntityTestData.defaultVehiculeJpaEntity())
					.journeyRequest(JourneyRequestJpaTestData.defaultExistentJourneyRequestJpaEntity()).build());

	public static Page<JourneyProposalJpaEntity> defaultJourneyProposalJpaEntityPage() {

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

				return 2;
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

				return Sort.by(Direction.DESC, "dateTime");
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return true;
			}

			@Override
			public boolean hasNext() {

				return false;
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

				return null;
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
				return null;
			}
		};
	}
}
