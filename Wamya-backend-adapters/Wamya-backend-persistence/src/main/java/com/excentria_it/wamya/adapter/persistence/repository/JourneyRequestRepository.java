package com.excentria_it.wamya.adapter.persistence.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

public interface JourneyRequestRepository extends JpaRepository<JourneyRequestJpaEntity, Long> {

	@Query(nativeQuery = true, value = "SELECT jr.id AS id, jr.departure_place_id AS departurePlaceId, dp.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude,  dp.department_id AS departurePlaceDepartmentId, ldp.name AS departurePlaceName, jr.arrival_place_id AS arrivalPlaceId, ap.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, ap.department_id AS arrivalPlaceDepartmentId, lap.name AS arrivalPlaceName, let.id AS engineTypeId, let.name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.date_time AS dateTime, jr.workers AS workers, jr.description AS description, ua.oauth_id AS clientOauthId, ua.firstname AS clientFirstname, pi.id AS clientProfileImageId, pi.hash AS clientProfileImageHash, COALESCE(MIN(jp.price),0) AS minPrice FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN localized_place ldp ON dp.id = ldp.id AND dp.type = ldp.type INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_place lap ON ap.id = lap.id AND ap.type = lap.type INNER JOIN engine_type et ON jr.engine_type_id = et.id INNER JOIN localized_engine_type let ON jr.engine_type_id = let.id INNER JOIN user_account ua ON jr.client_id = ua.id INNER JOIN document pi ON ua.profile_image_id = pi.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.department_id = ?1 AND ap.department_id IN (?2) AND let.id IN (?3) AND jr.date_time BETWEEN ?4 AND ?5 AND let.locale = ?6 AND ldp.locale = ?6 AND lap.locale = ?6 GROUP BY jr.id, jr.departure_place_id, dp.type, dp.latitude, dp.longitude, dp.department_id, ldp.name, jr.arrival_place_id, ap.type, ap.latitude, ap.longitude, ap.department_id, lap.name, let.id, let.name, et.code, jr.distance, jr.date_time, jr.workers, jr.description, ua.oauth_id, ua.firstname, pi.id, pi.hash", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN engine_type et ON jr.engine_type_id = et.id WHERE dp.department_id = ?1 AND ap.department_id IN (?2) AND et.id IN (?3) AND jr.date_time BETWEEN ?4 AND ?5")
	Page<JourneyRequestSearchOutput> findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween(
			Long departurePlaceDepartmentId, Set<Long> arrivalPlaceDepartmentId, Set<Long> engineTypes,
			Instant startDate, Instant endDate, String locale, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT jr.id AS id, jr.departure_place_id AS departurePlaceId, dp.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dp.department_id AS departurePlaceDepartmentId, ldp.name AS departurePlaceName, jr.arrival_place_id AS arrivalPlaceId, ap.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, ap.department_id AS arrivalPlaceDepartmentId, lap.name AS arrivalPlaceName, let.id AS engineTypeId, let.name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.date_time AS dateTime, jr.workers AS workers, jr.description AS description, ua.oauth_id AS clientOauthId, ua.firstname AS clientFirstname, pi.id AS clientProfileImageId, pi.hash AS clientProfileImageHash, COALESCE(MIN(jp.price),0) AS minPrice FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN localized_place ldp ON dp.id = ldp.id AND dp.type = ldp.type INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN localized_place lap ON ap.id = lap.id AND ap.type = lap.type INNER JOIN engine_type et ON jr.engine_type_id = et.id INNER JOIN localized_engine_type let ON jr.engine_type_id = let.id INNER JOIN user_account ua ON jr.client_id = ua.id INNER JOIN document pi ON ua.profile_image_id = pi.id LEFT OUTER JOIN journey_proposal jp ON jr.id = jp.journey_request_id WHERE dp.department_id = ?1 AND let.id IN (?2) AND jr.date_time BETWEEN ?3 AND ?4 AND let.locale = ?5 AND ldp.locale = ?5 AND lap.locale = ?5 GROUP BY jr.id, jr.departure_place_id, dp.type, dp.latitude, dp.longitude, dp.department_id, ldp.name, jr.arrival_place_id, ap.type, ap.latitude, ap.longitude, ap.department_id, lap.name, let.id, let.name, et.code, jr.distance, jr.date_time, jr.workers, jr.description, ua.oauth_id, ua.firstname, pi.id, pi.hash", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM journey_request jr INNER JOIN place dp ON jr.departure_place_id = dp.id INNER JOIN place ap ON jr.arrival_place_id = ap.id INNER JOIN engine_type et ON jr.engine_type_id = et.id WHERE dp.department_id = ?1 AND et.id IN (?2) AND jr.date_time BETWEEN ?3 AND ?4")
	Page<JourneyRequestSearchOutput> findByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween(
			Long departurePlaceDepartmentId, Set<Long> engineTypes, Instant startDate, Instant endDate, String locale,
			Pageable pageable);

	@Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap JOIN jr.client c LEFT JOIN jr.proposals p JOIN jr.status st WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3 AND KEY(l) = ?4 AND KEY(ldp) = ?4 AND KEY(lap) = ?4 AND st.code != com.excentria_it.wamya.domain.JourneyRequestStatusCode.CANCELED GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN jr.status st WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3 AND ?4 = ?4 AND st.code != com.excentria_it.wamya.domain.JourneyRequestStatusCode.CANCELED")
	Page<ClientJourneyRequestDtoOutput> findByCreationDateTimeBetweenAndClient_Email(Instant lowerDateEdge,
			Instant higherDateEdge, String email, String locale, Pageable pageable);

	@Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN c.icc ic JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap LEFT JOIN jr.proposals p WHERE jr.id = ?1 AND c.email = ?2 AND KEY(l) = ?3 AND KEY(ldp) = ?3 AND KEY(lap) = ?3 GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN c.icc ic WHERE jr.id = ?1 AND c.email = ?2 AND ?3 = ?3")
	Optional<ClientJourneyRequestDtoOutput> findByIdAndClient_Email(Long journeyRequestId, String clientEmail,
			String locale);

	@Query(value = "SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JourneyRequestJpaEntity j JOIN j.client c WHERE j.id = ?1 AND j.dateTime > CURRENT_TIMESTAMP AND c.email = ?2")
	boolean existsAndNotExpiredByIdAndClient_Email(Long journeyRequestId, String clientUsername);

	@Query(value = "SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JourneyRequestJpaEntity j JOIN j.client c JOIN c.icc i WHERE j.id = ?1 AND j.dateTime > CURRENT_TIMESTAMP AND c.mobileNumber = ?3 AND i.value = ?2")
	boolean existsAndNotExpiredByIdAndClient_Icc_ValueAndClient_MobileNumber(Long journeyRequestId, String icc,
			String mobileNumber);

	@Modifying
	@Query(value = "UPDATE JourneyRequestJpaEntity j SET j.status = ?2 WHERE j.id = ?1")
	void updateStatus(Long journeyRequestId, JourneyRequestStatusJpaEntity cancelledStatusJpaEntity);

	Page<JourneyRequestJpaEntity> findByStatus_Code(JourneyRequestStatusCode statusCode, Pageable pageable);

	@Query(value = "SELECT jr FROM JourneyRequestJpaEntity jr WHERE jr.id IN ?1")
	Set<JourneyRequestJpaEntity> findByIds(Set<Long> fulfilledJourneysIds);

	@Modifying
	@Query(value = "UPDATE JourneyRequestJpaEntity j SET j.status = ?2 WHERE j.id IN ?1")
	void updateInBatch(Set<Long> fulfilledJourneysIds, JourneyRequestStatusJpaEntity statusJpaEntity);
}
