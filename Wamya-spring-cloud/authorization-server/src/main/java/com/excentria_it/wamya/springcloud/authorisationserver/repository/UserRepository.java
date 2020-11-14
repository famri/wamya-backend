package com.excentria_it.wamya.springcloud.authorisationserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByPhoneNumber(String phoneNumber);
}