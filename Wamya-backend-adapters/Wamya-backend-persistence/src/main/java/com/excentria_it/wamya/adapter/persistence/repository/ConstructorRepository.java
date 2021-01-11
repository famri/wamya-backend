package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.ConstructorJpaEntity;
import com.excentria_it.wamya.domain.ConstructorDto;

public interface ConstructorRepository extends JpaRepository<ConstructorJpaEntity, Long> {

	@Query(value = "SELECT c FROM ConstructorJpaEntity c JOIN FETCH c.models m WHERE c.id = ?1")
	Optional<ConstructorDto> findConstructorById(Long constructorId);

}
