package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.excentria_it.wamya.domain.InternationalCallingCodeConstants.FRANCE_ICC;

public interface ClientRepository extends JpaRepository<ClientJpaEntity, Long> {

    Optional<ClientJpaEntity> findClientByIcc_ValueAndMobileNumber(String internationalCallingCode, String mobileNumber);

    Optional<ClientJpaEntity> findClientByEmail(String email);

    Optional<ClientJpaEntity> findClientByOauthId(String userOauthId);

    default Optional<ClientJpaEntity> findClientBySubject(String subject) {
        if (subject == null) {
            return Optional.empty();
        }

        if (subject.contains("@")) {
            return findClientByEmail(subject);
        } else if (subject.contains("_") && subject.split("_").length == 2) {

            String[] mobileNumber = subject.split("_");
            Optional<ClientJpaEntity> userEntityOptional = findClientByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findClientByIcc_ValueAndMobileNumber(mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;

        } else {
            return findClientByOauthId(subject);
        }

    }

}
