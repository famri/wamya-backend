package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity.JourneyRequestStatusCode;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity.JourneyRequestStatusJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedJourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;

public class JourneyRequestJpaTestData {

	private static ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.UTC);

	private static ZonedDateTime endDate = startDate.plusDays(1);

	public static EngineTypeJpaEntity defaultEngineTypeJpaEntity() {
		LocalizedEngineTypeJpaEntity en = new LocalizedEngineTypeJpaEntity();
		en.setName("EngineType1");
		en.setDescription("EngineTypeDescription1");
		LocalizedId lten = new LocalizedId("en");
		lten.setId(1L);
		en.setLocalizedId(lten);

		LocalizedEngineTypeJpaEntity fr = new LocalizedEngineTypeJpaEntity();
		fr.setName("TypeVehicule1");
		fr.setDescription("DescriptionVehicule1");
		LocalizedId ltfr = new LocalizedId("fr");
		ltfr.setId(1L);
		en.setLocalizedId(ltfr);

		return EngineTypeJpaEntity.builder().id(1L).localizations(Map.of("en", en, "fr", fr)).build();
	}

	public static PlaceJpaEntity defaultDeparturePlaceJpaEntity() {
		return PlaceJpaEntity.builder().id("departurePlaceId").regionId("departurePlaceRegionId")
				.name("departurePlaceName").build();
	}

	public static PlaceJpaEntity defaultArrivalPlaceJpaEntity() {
		return PlaceJpaEntity.builder().id("arrivalPlaceId").regionId("arrivalPlaceRegionId").name("arrivalPlaceName")
				.build();
	}

	public static JourneyRequestJpaEntity defaultExistentJourneyRequestJpaEntity() {
		return JourneyRequestJpaEntity.builder().id(1L).departurePlace(defaultDeparturePlaceJpaEntity())
				.arrivalPlace(defaultArrivalPlaceJpaEntity()).engineType(defaultEngineTypeJpaEntity())
				.dateTime(startDate.toInstant()).endDateTime(endDate.toInstant()).distance(150.5).workers(2)
				.proposals(new HashSet<>()).description("Need transporter URGENT!")
				.client(defaultExistentClientJpaEntity()).build();
	}

	public static JourneyRequestStatusJpaEntityBuilder defaultJourneyRequestStatusJpaEntityBuilder() {

		LocalizedJourneyRequestStatusJpaEntity localizedStatusJpaEntity = LocalizedJourneyRequestStatusJpaEntity
				.builder().localizedId(new LocalizedId(1L, "en_US")).value("opened").build();

		return JourneyRequestStatusJpaEntity.builder().id(1L).code(JourneyRequestStatusCode.OPENED)
				.description("Journey request was saved and is ready to receive for proposals.")
				.localizations(Map.of("en_US", localizedStatusJpaEntity));
	}

}
