package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.excentria_it.wamya.domain.InternationalCallingCodeConstants.FRANCE_ICC;

public interface UserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {

    Optional<UserAccountJpaEntity> findByOauthId(String userOauthId);

    Optional<UserAccountJpaEntity> findByEmail(String email);

    @Query("SELECT u FROM UserAccountJpaEntity u  WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
    Optional<UserAccountJpaEntity> findByMobilePhoneNumber(
            @Param("internationalCallingCode") String internationalCallingCode,
            @Param("mobileNumber") String mobileNumber);


    @Query("SELECT u FROM UserAccountJpaEntity u LEFT JOIN FETCH u.preferences p WHERE u.oauthId = :userOauthId")
    Optional<UserAccountJpaEntity> findByOauthIdWithUserPreferences(@Param("userOauthId") String userOauthId);

    @Query("SELECT u FROM UserAccountJpaEntity u LEFT JOIN FETCH u.preferences p WHERE u.email = :email")
    Optional<UserAccountJpaEntity> findByEmailWithUserPreferences(@Param("email") String email);

    @Query("SELECT u FROM UserAccountJpaEntity u LEFT JOIN FETCH u.preferences p WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
    Optional<UserAccountJpaEntity> findByMobilePhoneNumberWithUserPreferences(
            @Param("internationalCallingCode") String internationalCallingCode,
            @Param("mobileNumber") String mobileNumber);


    Boolean existsByOauthId(String userOauthId);

    Boolean existsByEmail(String userEmail);

    Boolean existsByIcc_ValueAndMobileNumber(String userIccValue, String userMobileNumber);


    @Query("SELECT u.mobileNumberValidationCode FROM UserAccountJpaEntity u WHERE u.email = :email")
    Optional<String> findMobileNumberValidationCodeByEmail(String email);

    default Optional<UserAccountJpaEntity> findBySubject(String subject) {
        if (subject == null) {
            return Optional.empty();
        }

        if (subject.contains("@")) {
            return findByEmail(subject);
        } else if (subject.contains("_")) {

            String[] mobileNumber = subject.split("_");
            Optional<UserAccountJpaEntity> userEntityOptional = findByMobilePhoneNumber(mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findByMobilePhoneNumber(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;

        } else {
            return findByOauthId(subject);
        }

    }

    default Optional<UserAccountJpaEntity> findBySubjectWithUserPreferences(String subject) {
        if (subject == null) {
            return Optional.empty();
        }

        if (subject.contains("@")) {
            return findByEmailWithUserPreferences(subject);
        } else if (subject.contains("_") && subject.split("_").length == 2) {

            String[] mobileNumber = subject.split("_");
            Optional<UserAccountJpaEntity> userEntityOptional = findByMobilePhoneNumberWithUserPreferences(mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findByMobilePhoneNumberWithUserPreferences(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;

        } else {
            return findByOauthIdWithUserPreferences(subject);
        }

    }

    default Boolean existsBySubject(String userSubject) {

        if (userSubject == null) {
            return false;
        }

        if (userSubject.contains("@")) {
            return existsByEmail(userSubject);
        } else if (userSubject.contains("_") && userSubject.split("_").length == 2) {

            String[] mobileNumber = userSubject.split("_");
            Boolean userExists = existsByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1]);
            if (!userExists && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userExists = existsByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userExists;

        } else {
            return existsByOauthId(userSubject);
        }
    }

    ;
}
