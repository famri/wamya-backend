package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.TransporterVehiculeOutput;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransporterRepository extends JpaRepository<TransporterJpaEntity, Long> {

    Optional<TransporterJpaEntity> findByIcc_ValueAndMobileNumber(String internationalCallingCode, String mobileNumber);

    Optional<TransporterJpaEntity> findByEmail(String email);

    @Query("SELECT t FROM TransporterJpaEntity t LEFT JOIN t.vehicules v WHERE  t.email = :email")
    Optional<TransporterJpaEntity> findTransporterWithVehiculesByEmail(@Param("email") String email);

    @Query("SELECT v.id AS id, v.registration AS registrationNumber, c.id AS constructorId, c.name AS constructorName, tm.constructorName AS temporaryConstructorName, m.id AS modelId, m.name AS modelName, tm.modelName AS temporaryModelName, et.id AS engineTypeId, VALUE(etl).name AS engineTypeName, eti.id AS engineTypeImageId, eti.hash AS engineTypeImageHash, v.circulationDate AS circulationDate, i.id AS imageId, i.hash AS imageHash FROM TransporterJpaEntity t LEFT JOIN t.vehicules v LEFT JOIN v.model m LEFT JOIN m.constructor c  LEFT JOIN v.type et  LEFT JOIN  et.localizations etl LEFT JOIN et.image eti LEFT JOIN v.temporaryModel tm  LEFT JOIN v.image i WHERE t.email = :email AND KEY(etl) = :locale")
    List<TransporterVehiculeOutput> findTransporterVehiculesByEmail(@Param("email") String email,
                                                                    @Param("locale") String locale, Sort sort);

    Optional<TransporterJpaEntity> findByOauthId(String userOauthId);

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM TransporterJpaEntity t INNER JOIN t.vehicules v  WHERE t.icc.value = :internationalCallingCode AND t.mobileNumber = :mobileNumber AND v.id = :vehiculeId")
    boolean existsByIccAndMobileNumberAndVehiculeId(@Param("internationalCallingCode") String internationalCallingCode, @Param("mobileNumber") String mobileNumber,
                                                    @Param("vehiculeId") Long vehiculeId);

    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM TransporterJpaEntity t INNER JOIN t.vehicules v  WHERE t.email = :email AND v.id = :vehiculeId")
    boolean existsByEmailAndVehiculeId(@Param("email") String email, @Param("vehiculeId") Long vehiculeId);
}
