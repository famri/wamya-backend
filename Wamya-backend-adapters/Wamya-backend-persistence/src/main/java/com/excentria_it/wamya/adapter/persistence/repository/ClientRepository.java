package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;

public interface ClientRepository extends JpaRepository<ClientJpaEntity, Long> {

	Optional<ClientJpaEntity> findByIcc_ValueAndMobileNumber(String internationalCallingCode, String mobileNumber);

	Optional<ClientJpaEntity> findByEmail(String email);

	Optional<ClientJpaEntity> findByOauthId(Long userOauthId);

}
