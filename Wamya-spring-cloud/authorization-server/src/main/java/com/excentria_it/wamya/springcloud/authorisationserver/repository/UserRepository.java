package com.excentria_it.wamya.springcloud.authorisationserver.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}