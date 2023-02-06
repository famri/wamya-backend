package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static com.excentria_it.wamya.domain.InternationalCallingCodeConstants.FRANCE_ICC;

public interface JourneyRequestRepository extends JpaRepository<JourneyRequestJpaEntity, Long> {

    @Query(value = "SELECT jr.id AS id, " + "dp.placeId.id AS departurePlaceId, "
            + "dp.placeId.type AS departurePlaceType, " + "dp.latitude AS departurePlaceLatitude, "
            + "dp.longitude AS departurePlaceLongitude, " + "dpd.id AS departurePlaceDepartmentId, "
            + "VALUE(ldp).name AS departurePlaceName, " + "ap.placeId.id AS arrivalPlaceId, "
            + "ap.placeId.type AS arrivalPlaceType, " + "ap.latitude AS arrivalPlaceLatitude, "
            + "ap.longitude AS arrivalPlaceLongitude, " + "apd.id AS arrivalPlaceDepartmentId, "
            + "VALUE(lap).name AS arrivalPlaceName, " + "et.id AS engineTypeId, "
            + "VALUE(let).name AS engineTypeName, " + "et.code AS engineTypeCode, " + "jr.distance AS distance, "
            + "jr.hours AS hours, " + "jr.minutes AS minutes, " + "jr.dateTime AS dateTime, "
            + "jr.workers AS workers, " + "jr.description AS description, " + "ua.oauthId AS clientOauthId, "
            + "ua.firstname AS clientFirstname, " + "pi.id AS clientProfileImageId, "
            + "pi.hash AS clientProfileImageHash "

            + "FROM JourneyRequestJpaEntity jr " + "JOIN jr.departurePlace dp " + "JOIN dp.department dpd "
            + "JOIN dp.localizations ldp " + "JOIN jr.arrivalPlace ap " + "JOIN ap.placeId appid "
            + "JOIN ap.department apd " + "JOIN ap.localizations lap " + "JOIN jr.engineType et "
            + "JOIN et.localizations let " + "JOIN jr.client ua " + "JOIN ua.profileImage pi " + "JOIN jr.status jrs "

            + "WHERE dpd.id = :departureDepartmentId "
            + "AND NOT EXISTS ( SELECT jr2 FROM JourneyRequestJpaEntity jr2 JOIN jr2.proposals jr2p JOIN jr2p.transporter jr2pt WHERE jr2pt.email = :username AND jr2.id = jr.id ) "
            + "AND apd.id IN :arrivalDepartmentIds " + "AND et.id IN :engineTypeIds " + "AND jrs.code IN :statusCodes "
            + "AND jr.dateTime BETWEEN :startDate AND :endDate " + "AND KEY(let) = :locale "
            + "AND KEY(ldp) = :locale  " + "AND KEY(lap) = :locale  ")
    Page<JourneyRequestSearchOutput> findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdsInAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
            @Param("departureDepartmentId") Long departurePlaceDepartmentId,
            @Param("arrivalDepartmentIds") Set<Long> arrivalPlaceDepartmentId,
            @Param("engineTypeIds") Set<Long> engineTypes, @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate, @Param("statusCodes") Set<JourneyRequestStatusCode> statusCodes,
            @Param("username") String transporterUsername, @Param("locale") String locale, Pageable pageable);

    @Query(value = "SELECT jr.id AS id, " + "dp.placeId.id AS departurePlaceId, "
            + "dp.placeId.type AS departurePlaceType, " + "dp.latitude AS departurePlaceLatitude, "
            + "dp.longitude AS departurePlaceLongitude, " + "dpd.id AS departurePlaceDepartmentId, "
            + "VALUE(ldp).name AS departurePlaceName, " + "ap.placeId.id AS arrivalPlaceId, "
            + "ap.placeId.type AS arrivalPlaceType, " + "ap.latitude AS arrivalPlaceLatitude, "
            + "ap.longitude AS arrivalPlaceLongitude, " + "apd.id AS arrivalPlaceDepartmentId, "
            + "VALUE(lap).name AS arrivalPlaceName, " + "et.id AS engineTypeId, "
            + "VALUE(let).name AS engineTypeName, " + "et.code AS engineTypeCode, " + "jr.distance AS distance, "
            + "jr.hours AS hours, " + "jr.minutes AS minutes, " + "jr.dateTime AS dateTime, "
            + "jr.workers AS workers, " + "jr.description AS description, " + "ua.oauthId AS clientOauthId, "
            + "ua.firstname AS clientFirstname, " + "pi.id AS clientProfileImageId, "
            + "pi.hash AS clientProfileImageHash "

            + "FROM JourneyRequestJpaEntity jr " + "JOIN jr.departurePlace dp " + "JOIN dp.department dpd "
            + "JOIN dp.localizations ldp " + "JOIN jr.arrivalPlace ap " + "JOIN ap.placeId appid "
            + "JOIN ap.department apd " + "JOIN ap.localizations lap " + "JOIN jr.engineType et "
            + "JOIN et.localizations let " + "JOIN jr.client ua " + "JOIN ua.profileImage pi " + "JOIN jr.status jrs "

            + "WHERE dpd.id = :departureDepartmentId "
            + "AND NOT EXISTS ( SELECT jr2 FROM JourneyRequestJpaEntity jr2 JOIN jr2.proposals jr2p JOIN jr2p.transporter jr2pt WHERE jr2pt.oauthId = :transporterSubject AND jr2.id = jr.id ) "
            + "AND et.id IN :engineTypeIds " + "AND jrs.code IN :statusCodes "
            + "AND jr.dateTime BETWEEN :startDate AND :endDate " + "AND KEY(let) = :locale "
            + "AND KEY(ldp) = :locale  " + "AND KEY(lap) = :locale  ")
    Page<JourneyRequestSearchOutput> findByDeparturePlace_DepartmentIdAndEngineType_IdsInAndDateBetweenAndStatus_CodesAndUsernameNoProposal(
            @Param("departureDepartmentId") Long departurePlaceDepartmentId,
            @Param("engineTypeIds") Set<Long> engineTypes, @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate, @Param("statusCodes") Set<JourneyRequestStatusCode> statusCodes,
            @Param("transporterSubject") String transporterSubject, @Param("locale") String locale, Pageable pageable);

    @Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap JOIN jr.client c LEFT JOIN jr.proposals p JOIN jr.status st WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3 AND KEY(l) = ?4 AND KEY(ldp) = ?4 AND KEY(lap) = ?4 AND st.code != com.excentria_it.wamya.domain.JourneyRequestStatusCode.CANCELED GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN jr.status st WHERE jr.creationDateTime BETWEEN ?1 AND ?2 AND c.email = ?3 AND ?4 = ?4 AND st.code != com.excentria_it.wamya.domain.JourneyRequestStatusCode.CANCELED")
    Page<ClientJourneyRequestDtoOutput> findByCreationDateTimeBetweenAndClient_Email(Instant lowerDateEdge,
                                                                                     Instant higherDateEdge, String email, String locale, Pageable pageable);

    @Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN c.icc ic JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap LEFT JOIN jr.proposals p WHERE jr.id = ?1 AND c.email = ?2 AND KEY(l) = ?3 AND KEY(ldp) = ?3 AND KEY(lap) = ?3 GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN c.icc ic WHERE jr.id = ?1 AND c.email = ?2 AND ?3 = ?3")
    Optional<ClientJourneyRequestDtoOutput> findByIdAndClient_Email(Long journeyRequestId, String clientEmail,
                                                                    String locale);

    @Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN c.icc ic JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap LEFT JOIN jr.proposals p WHERE jr.id = ?1 AND c.oauthId = ?2 AND KEY(l) = ?3 AND KEY(ldp) = ?3 AND KEY(lap) = ?3 GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN c.icc ic WHERE jr.id = ?1 AND c.oauthId = ?2 AND ?3 = ?3")
    Optional<ClientJourneyRequestDtoOutput> findByIdAndClient_OauthId(Long journeyRequestId, String clientOauthId,
                                                                      String locale);

    @Query(value = "SELECT jr.id AS id, dp.placeId.id AS departurePlaceId, dp.placeId.type AS departurePlaceType, dp.latitude AS departurePlaceLatitude, dp.longitude AS departurePlaceLongitude, dpd.id AS departurePlaceDepartmentId, VALUE(ldp).name AS departurePlaceName, ap.placeId.id AS arrivalPlaceId, ap.placeId.type AS arrivalPlaceType, ap.latitude AS arrivalPlaceLatitude, ap.longitude AS arrivalPlaceLongitude, apd.id AS arrivalPlaceDepartmentId, VALUE(lap).name AS arrivalPlaceName, et.id AS engineTypeId, VALUE(l).name AS engineTypeName, et.code AS engineTypeCode, jr.distance AS distance, jr.dateTime AS dateTime, jr.creationDateTime AS creationDateTime, jr.workers AS workers, jr.description AS description, COUNT(p) AS proposalsCount FROM JourneyRequestJpaEntity jr JOIN jr.engineType et JOIN et.localizations l JOIN jr.client c JOIN c.icc ic JOIN jr.departurePlace dp JOIN dp.department dpd JOIN dp.localizations ldp JOIN jr.arrivalPlace ap JOIN ap.department apd JOIN ap.localizations lap LEFT JOIN jr.proposals p WHERE jr.id = ?1 AND ic.value = ?2 AND c.mobileNumber = ?3 AND KEY(l) = ?4 AND KEY(ldp) = ?4 AND KEY(lap) = ?4 GROUP BY jr.id, dp.placeId.id, dp.placeId.type, dp.latitude, dp.longitude, dpd.id, VALUE(ldp).name, ap.placeId.id, ap.placeId.type, ap.latitude, ap.longitude, apd.id, VALUE(lap).name, et.id, VALUE(l).name, et.code, jr.distance, jr.dateTime, jr.creationDateTime, jr.workers, jr.description", countQuery = "SELECT COUNT(DISTINCT jr.id) FROM JourneyRequestJpaEntity jr JOIN jr.client c JOIN c.icc ic WHERE jr.id = ?1 AND ic.value = ?2 AND c.mobileNumber = ?3 AND ?4 = ?4")
    Optional<ClientJourneyRequestDtoOutput> findByIdAndClient_IccValueAndClient_MobileNumber(Long journeyRequestId, String icc,
                                                                                             String mobileNumber,
                                                                                             String locale);

    @Query(value = "SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JourneyRequestJpaEntity j JOIN j.client c WHERE j.id = ?1 AND j.dateTime > CURRENT_TIMESTAMP AND c.email = ?2")
    boolean existsAndNotExpiredByIdAndClient_Email(Long journeyRequestId, String clientEmail);

    @Query(value = "SELECT CASE WHEN COUNT(j) > 0 THEN true ELSE false END FROM JourneyRequestJpaEntity j JOIN j.client c WHERE j.id = ?1 AND j.dateTime > CURRENT_TIMESTAMP AND c.oauthId = ?2")
    boolean existsAndNotExpiredByIdAndClient_OauthId(Long journeyRequestId, String clientOauthId);

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

    default boolean existsAndNotExpiredByIdAndClientSubject(Long journeyRequestId, String clientSubject) {
        if (clientSubject == null) {
            return false;
        }
        if (clientSubject.contains("@")) {
            return existsAndNotExpiredByIdAndClient_Email(journeyRequestId, clientSubject);
        } else if (clientSubject.contains("_") && clientSubject.split("_").length == 2) {

            String[] mobileNumber = clientSubject.split("_");
            return existsAndNotExpiredByIdAndClient_Icc_ValueAndClient_MobileNumber(journeyRequestId, mobileNumber[0], mobileNumber[1]);

        } else {
            return existsAndNotExpiredByIdAndClient_OauthId(journeyRequestId, clientSubject);
        }
    }

    default Optional<ClientJourneyRequestDtoOutput> findByIdAndClientSubject(Long journeyRequestId, String clientSubject,
                                                                             String locale) {

        if (clientSubject == null) {
            return Optional.empty();
        }
        if (clientSubject.contains("@")) {
            return findByIdAndClient_Email(journeyRequestId, clientSubject, locale);
        } else if (clientSubject.contains("_") && clientSubject.split("_").length == 2) {
            
            String[] mobileNumber = clientSubject.split("_");
            Optional<ClientJourneyRequestDtoOutput> clientJourneyRequestOptional = findByIdAndClient_IccValueAndClient_MobileNumber(journeyRequestId, mobileNumber[0], mobileNumber[1], locale);
            if (clientJourneyRequestOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                clientJourneyRequestOptional = findByIdAndClient_IccValueAndClient_MobileNumber(journeyRequestId, mobileNumber[0], mobileNumber[1].substring(1), locale);
            }
            return clientJourneyRequestOptional;

        } else {
            return findByIdAndClient_OauthId(journeyRequestId, clientSubject, locale);
        }

    }


}
