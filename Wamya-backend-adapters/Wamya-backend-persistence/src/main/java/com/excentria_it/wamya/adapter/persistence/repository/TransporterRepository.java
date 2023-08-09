package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.TransporterVehicleOutput;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.excentria_it.wamya.domain.InternationalCallingCodeConstants.FRANCE_ICC;


public interface TransporterRepository extends JpaRepository<TransporterJpaEntity, Long> {

    Optional<TransporterJpaEntity> findTransporterByIcc_ValueAndMobileNumber(String internationalCallingCode, String mobileNumber);

    Optional<TransporterJpaEntity> findTransporterByEmail(String email);

    @Query("SELECT t FROM TransporterJpaEntity t LEFT JOIN t.vehicles v WHERE  t.email = :email")
    Optional<TransporterJpaEntity> findTransporterWithVehiclesByEmail(@Param("email") String email);

    @Query("SELECT v.id AS id, v.registration AS registrationNumber, c.id AS constructorId, c.name AS constructorName, tm.constructorName AS temporaryConstructorName, m.id AS modelId, m.name AS modelName, tm.modelName AS temporaryModelName, et.id AS engineTypeId, VALUE(etl).name AS engineTypeName, eti.id AS engineTypeImageId, eti.hash AS engineTypeImageHash, v.circulationDate AS circulationDate, i.id AS imageId, i.hash AS imageHash FROM TransporterJpaEntity t LEFT JOIN t.vehicles v LEFT JOIN v.model m LEFT JOIN m.constructor c  LEFT JOIN v.type et  LEFT JOIN  et.localizations etl LEFT JOIN et.image eti LEFT JOIN v.temporaryModel tm  LEFT JOIN v.image i WHERE t.email = :email AND KEY(etl) = :locale")
    List<TransporterVehicleOutput> findTransporterVehiclesByEmail(@Param("email") String email,
                                                                  @Param("locale") String locale, Sort sort);

    Optional<TransporterJpaEntity> findTransporterByOauthId(String userOauthId);

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM TransporterJpaEntity t INNER JOIN t.vehicles v  WHERE t.icc.value = :internationalCallingCode AND t.mobileNumber = :mobileNumber AND v.id = :vehicleId")
    boolean existsByIccAndMobileNumberAndVehicleId(@Param("internationalCallingCode") String internationalCallingCode, @Param("mobileNumber") String mobileNumber,
                                                   @Param("vehicleId") Long vehicleId);

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM TransporterJpaEntity t INNER JOIN t.vehicles v  WHERE t.email = :email AND v.id = :vehicleId")
    boolean existsByEmailAndVehicleId(@Param("email") String email, @Param("vehicleId") Long vehicleId);

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM TransporterJpaEntity t INNER JOIN t.vehicles v  WHERE t.oauthId = :oauthId AND v.id = :vehicleId")
    boolean existsByOauthAndVehicleId(@Param("oauthId") String oauthId, @Param("vehicleId") Long vehicleId);

    @Query("SELECT t FROM TransporterJpaEntity t LEFT JOIN t.vehicles v WHERE  t.oauthId = :oauthId")
    Optional<TransporterJpaEntity> findTransporterWithVehiclesByOauthId(@Param("oauthId") String oauthId);

    @Query("SELECT t FROM TransporterJpaEntity t LEFT JOIN t.vehicles v WHERE t.icc.value = :internationalCallingCode AND t.mobileNumber = :mobileNumber")
    Optional<TransporterJpaEntity> findTransporterWithVehiclesByIccAndMobileNumber(@Param("internationalCallingCode") String internationalCallingCode, @Param("mobileNumber") String mobileNumber);

    default Optional<TransporterJpaEntity> findTransporterWithVehiclesBySubject(String subject) {
        if (subject.contains("@"))
            return findTransporterWithVehiclesByEmail(subject);
        else if (subject.contains("_") && subject.split("_").length == 2) {
            String[] mobileNumber = subject.split("_");
            Optional<TransporterJpaEntity> userEntityOptional = findTransporterWithVehiclesByIccAndMobileNumber(mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findTransporterWithVehiclesByIccAndMobileNumber(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;
        } else {
            return findTransporterWithVehiclesByOauthId(subject);
        }
    }

    default Optional<TransporterJpaEntity> findTransporterBySubject(String subject) {
        if (subject == null) {
            return Optional.empty();
        }

        if (subject.contains("@")) {
            return findTransporterByEmail(subject);
        } else if (subject.contains("_") && subject.split("_").length == 2) {

            String[] mobileNumber = subject.split("_");
            Optional<TransporterJpaEntity> userEntityOptional = findTransporterByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findTransporterByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;

        } else {
            return findTransporterByOauthId(subject);
        }
    }

    default boolean existsBySubjectAndVehicleId(String subject, Long vehicleId) {
        if (subject == null || vehicleId == null) {
            return false;
        }
        if (subject.contains("@")) {
            return existsByEmailAndVehicleId(subject, vehicleId);
        } else if (subject.contains("_") && subject.split("_").length == 2) {
            String[] mobileNumber = subject.split("_");
            boolean exists = existsByIccAndMobileNumberAndVehicleId(mobileNumber[0], mobileNumber[1], vehicleId);
            if (!exists && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                exists = existsByIccAndMobileNumberAndVehicleId(mobileNumber[0], mobileNumber[1].substring(1), vehicleId);
            }
            return exists;
        } else {
            return existsByOauthAndVehicleId(subject, vehicleId);
        }
    }

}
