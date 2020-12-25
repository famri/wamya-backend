package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;

public class JourneyRequestJpaTestData {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private static LocalDateTime startDate = LocalDateTime
			.parse(LocalDateTime.of(2020, 12, 10, 12, 0, 0, 0).format(DATE_TIME_FORMATTER), DATE_TIME_FORMATTER);
	private static LocalDateTime endDate = LocalDateTime.parse(addDays(startDate, 1).format(DATE_TIME_FORMATTER),
			DATE_TIME_FORMATTER);

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

	public static JourneyRequestJpaEntity defaultNewJourneyRequestJpaEntity() {
		return JourneyRequestJpaEntity.builder().id(1L).departurePlace(defaultDeparturePlaceJpaEntity())
				.arrivalPlace(defaultArrivalPlaceJpaEntity()).engineType(defaultEngineTypeJpaEntity())
				.dateTime(startDate).endDateTime(endDate).distance(150.5).workers(2)
				.description("Need transporter URGENT!").client(defaultExistingNotTransporterUserAccountJpaEntity())
				.proposals(null).build();
	}

	private static LocalDateTime addDays(LocalDateTime dateTime, int days) {

		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneOffset.UTC);

		LocalDateTime dateAfter = zonedDateTime.plusDays(days).toLocalDateTime();

		return dateAfter;
	}

}
