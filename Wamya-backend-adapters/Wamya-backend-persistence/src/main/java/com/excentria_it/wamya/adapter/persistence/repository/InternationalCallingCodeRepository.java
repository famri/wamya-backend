package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;

public interface InternationalCallingCodeRepository extends JpaRepository<InternationalCallingCodeJpaEntity, Long> {

	Optional<InternationalCallingCodeJpaEntity> findByValue(String value);

}
