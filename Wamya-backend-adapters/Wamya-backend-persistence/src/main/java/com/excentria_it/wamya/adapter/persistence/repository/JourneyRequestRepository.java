package com.excentria_it.wamya.adapter.persistence.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;

public interface JourneyRequestRepository extends JpaRepository<JourneyRequestJpaEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT jr.id AS id, jr.departure_place_id AS departurePlaceId, dp.region_id AS departurePlaceRegionId, dp.name AS departurePlaceName, jr.arrival_place_id AS arrivalPlaceId, ap.region_id AS arrivalPlaceRegionId, ap.name AS arrivalPlaceName, et.id AS engineTypeId, et.name AS engineTypeName, jr.distance AS distance, jr.date_time AS dateTime, jr.end_date_time AS endDateTime, jr.workers AS workers, jr.description AS description, jr.client_id AS clientId, ua.firstname AS clientFirstname, ua.photo_url AS clientPhotoUrl, COALESCE(MIN(jp.price),0) AS minPrice FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_engine_type et ON jr.engine_type_id = et.id INNER JOIN user_account ua ON jr.client_id = ua.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.region_id = ?1 AND ap.region_id IN (?2) AND et.id IN (?3) AND jr.date_time BETWEEN ?4 AND ?5 AND et.locale = ?6 GROUP BY jr.id, jr.departure_place_id, dp.region_id, dp.name, jr.arrival_place_id, ap.region_id, ap.name, et.id, et.name, jr.distance, jr.date_time, jr.end_date_time, jr.workers, jr.description, jr.client_id, ua.firstname, ua.photo_url", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_engine_type et ON jr.engine_type_id = et.id INNER JOIN user_account ua ON jr.client_id = ua.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.region_id = ?1 AND ap.region_id IN (?2) AND et.id IN (?3) AND jr.date_time BETWEEN ?4 AND ?5 AND et.locale = ?6")
	Page<JourneyRequestSearchDto> findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
			String departurePlaceRegionId, Set<String> arrivalPlaceRegionId, Set<Long> engineTypes, Instant startDate,
			Instant endDate, String locale, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT jr.id AS id, jr.departure_place_id AS departurePlaceId, dp.region_id AS departurePlaceRegionId, dp.name AS departurePlaceName, jr.arrival_place_id AS arrivalPlaceId, ap.region_id AS arrivalPlaceRegionId, ap.name AS arrivalPlaceName, et.id AS engineTypeId, et.name AS engineTypeName, jr.distance AS distance, jr.date_time AS dateTime, jr.end_date_time AS endDateTime, jr.workers AS workers, jr.description AS description, jr.client_id AS clientId, ua.firstname AS clientFirstname, ua.photo_url AS clientPhotoUrl, COALESCE(MIN(jp.price),0) AS minPrice FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_engine_type et ON jr.engine_type_id = et.id INNER JOIN user_account ua ON jr.client_id = ua.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.region_id = ?1 AND et.id IN (?2) AND jr.date_time BETWEEN ?3 AND ?4 AND et.locale = ?5 GROUP BY jr.id, jr.departure_place_id, dp.region_id, dp.name, jr.arrival_place_id, ap.region_id, ap.name, et.id, et.name, jr.distance, jr.date_time, jr.end_date_time, jr.workers, jr.description, jr.client_id, ua.firstname, ua.photo_url", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_engine_type et ON jr.engine_type_id = et.id INNER JOIN user_account ua ON jr.client_id = ua.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.region_id = ?1 AND et.id IN (?2) AND jr.date_time BETWEEN ?3 AND ?4 AND et.locale = ?5")
	Page<JourneyRequestSearchDto> findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(
			String departurePlaceRegionId, Set<Long> engineTypes, Instant startDate, Instant endDate, String locale,
			Pageable pageable);

	@Query(value = "SELECT jr.id AS id, dp.id AS departurePlaceId, dp.regionId AS departurePlaceRegionId, dp.name AS departurePlaceName, ap.id AS arrivalPlaceId, ap.regionId AS arrivalPlaceRegionId, ap.name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN jr.departurePlace dp JOIN jr.arrivalPlace ap JOIN jr.client c JOIN jr.proposals p WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3 AND KEY(l) = ?4 GROUP BY jr.id, dp.id, dp.regionId, dp.name, ap.id, ap.regionId, ap.name, et.id, VALUE(l).name, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3")
	Page<ClientJourneyRequestDto> findByCreationDateTimeBetweenAndClient_Email(Instant lowerDateEdge,
			Instant higherDateEdge, String email, String locale, Pageable pageable);

	@Query(value = "SELECT jr.id AS id, dp.id AS departurePlaceId, dp.regionId AS departurePlaceRegionId, dp.name AS departurePlaceName, ap.id AS arrivalPlaceId, ap.regionId AS arrivalPlaceRegionId, ap.name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN c.icc ic JOIN jr.departurePlace dp JOIN jr.arrivalPlace ap JOIN jr.client c JOIN jr.proposals p WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.mobileNumber = ?3 AND ic.value = ?4 AND KEY(l) = ?5 GROUP BY jr.id, dp.id, dp.regionId, dp.name, ap.id, ap.regionId, ap.name, et.id, VALUE(l).name, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN c.icc ic WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.mobileNumber = ?3 AND ic.value = ?4")
	Page<ClientJourneyRequestDto> findByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue(
			Instant lowerDateEdge, Instant higherDateEdge, String mobileNumber, String icc, String locale,
			Pageable pageable);

	Optional<ClientJourneyRequestDto> findByIdAndClient_Email(Long journeyRequestId, String clientEmail);

	Optional<ClientJourneyRequestDto> findByIdAndClient_MobileNumberAndClient_IccValue(Long journeyRequestId,
			String clientMobileNumber, String clientIccValue);
}
