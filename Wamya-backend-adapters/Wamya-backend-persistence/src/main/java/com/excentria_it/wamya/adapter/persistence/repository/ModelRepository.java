package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.ModelJpaEntity;
import com.excentria_it.wamya.domain.LoadModelsDto;

public interface ModelRepository extends JpaRepository<ModelJpaEntity, Long> {

	List<LoadModelsDto> findByConstructor_Id(Long constructorId, Sort sort);

}
