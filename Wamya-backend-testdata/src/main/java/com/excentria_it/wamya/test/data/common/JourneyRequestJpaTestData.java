package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity.JourneyRequestStatusJpaEntityBuilder;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedEngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedJourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.PlaceType;

public class JourneyRequestJpaTestData {

	private static ZonedDateTime startDate = ZonedDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.UTC);

	public static EngineTypeJpaEntity defaultEngineTypeJpaEntity() {
		LocalizedEngineTypeJpaEntity en = new LocalizedEngineTypeJpaEntity();
		en.setName("EngineType1");
		en.setDescription("EngineTypeDescription1");
		LocalizedId lten = new LocalizedId("en_US");
		lten.setId(1L);
		en.setLocalizedId(lten);

		LocalizedEngineTypeJpaEntity fr = new LocalizedEngineTypeJpaEntity();
		fr.setName("TypeVehicule1");
		fr.setDescription("DescriptionVehicule1");
		LocalizedId ltfr = new LocalizedId("fr_FR");
		ltfr.setId(1L);
		en.setLocalizedId(ltfr);

		return EngineTypeJpaEntity.builder().id(1L).localizations(Map.of("en_US", en, "fr_FR", fr)).build();
	}

	public static PlaceJpaEntity defaultDeparturePlaceJpaEntity() {
		DepartmentJpaEntity d = defaultExistentDepartureDepartmentJpaEntity();
		PlaceJpaEntity p = new PlaceJpaEntity(d, new HashMap<String, LocalizedPlaceJpaEntity>(), d.getLatitude(),
				d.getLongitude());
		PlaceId pId = new PlaceId(1L, PlaceType.DEPARTMENT);

		p.setPlaceId(pId);

		LocalizedPlaceJpaEntity lpjeFR = new LocalizedPlaceJpaEntity(new LocalizedPlaceId(pId, "fr_FR"), "Sfax", p);
		LocalizedPlaceJpaEntity lpjeEN = new LocalizedPlaceJpaEntity(new LocalizedPlaceId(pId, "en_US"), "Sfax", p);

		lpjeFR.setPlace(p);
		lpjeEN.setPlace(p);

		p.getLocalizations().put("fr_FR", lpjeFR);
		p.getLocalizations().put("en_US", lpjeEN);

		return p;
	}

	public static PlaceJpaEntity defaultArrivalPlaceJpaEntity() {
		DepartmentJpaEntity d = defaultExistentArrivalDepartmentJpaEntity();
		PlaceJpaEntity p = new PlaceJpaEntity(d, new HashMap<String, LocalizedPlaceJpaEntity>(), d.getLatitude(),
				d.getLongitude());

		PlaceId pId = new PlaceId(2L, PlaceType.DEPARTMENT);

		p.setPlaceId(pId);

		LocalizedPlaceJpaEntity lpjeFR = new LocalizedPlaceJpaEntity(new LocalizedPlaceId(pId, "fr_FR"), "Tunis", p);
		LocalizedPlaceJpaEntity lpjeEN = new LocalizedPlaceJpaEntity(new LocalizedPlaceId(pId, "en_US"), "Tunis", p);

		lpjeFR.setPlace(p);
		lpjeEN.setPlace(p);

		p.getLocalizations().put("fr_FR", lpjeFR);
		p.getLocalizations().put("en_US", lpjeEN);
		return p;
	}

	public static JourneyRequestJpaEntity defaultExistentJourneyRequestJpaEntity() {
		Map<String, LocalizedJourneyRequestStatusJpaEntity> localizations = new HashMap<>();
		LocalizedJourneyRequestStatusJpaEntity ljrsFR = new LocalizedJourneyRequestStatusJpaEntity(
				new LocalizedId(1L, "fr_FR"), null, "cr��");
		LocalizedJourneyRequestStatusJpaEntity ljrsEN = new LocalizedJourneyRequestStatusJpaEntity(
				new LocalizedId(1L, "en_US"), null, "opened");
		localizations.put("fr_FR", ljrsFR);
		localizations.put("en_US", ljrsEN);
		JourneyRequestStatusJpaEntity status = new JourneyRequestStatusJpaEntity(1L, JourneyRequestStatusCode.OPENED,
				"Opened", localizations);
		return JourneyRequestJpaEntity.builder().id(1L).departurePlace(defaultDeparturePlaceJpaEntity())
				.arrivalPlace(defaultArrivalPlaceJpaEntity()).engineType(defaultEngineTypeJpaEntity())
				.dateTime(startDate.toInstant()).distance(150500).workers(2).proposals(new HashSet<>())
				.description("Need transporter URGENT!").client(defaultExistentClientJpaEntity()).status(status)
				.build();
	}

	public static JourneyRequestStatusJpaEntityBuilder defaultJourneyRequestStatusJpaEntityBuilder() {

		LocalizedJourneyRequestStatusJpaEntity localizedStatusJpaEntity = LocalizedJourneyRequestStatusJpaEntity
				.builder().localizedId(new LocalizedId(1L, "en_US")).value("opened").build();

		return JourneyRequestStatusJpaEntity.builder().id(1L).code(JourneyRequestStatusCode.OPENED)
				.description("Journey request was saved and is ready to receive for proposals.")
				.localizations(Map.of("en_US", localizedStatusJpaEntity));
	}

	public static JourneyRequestStatusJpaEntity canceledJourneyRequestStatusJpaEntity() {

		LocalizedJourneyRequestStatusJpaEntity localizedStatusJpaEntity = LocalizedJourneyRequestStatusJpaEntity
				.builder().localizedId(new LocalizedId(1L, "en_US")).value("canceled").build();

		return JourneyRequestStatusJpaEntity.builder().id(2L).code(JourneyRequestStatusCode.CANCELED)
				.description("Journey request wascanceled by client.")
				.localizations(Map.of("en_US", localizedStatusJpaEntity)).build();
	}
}
