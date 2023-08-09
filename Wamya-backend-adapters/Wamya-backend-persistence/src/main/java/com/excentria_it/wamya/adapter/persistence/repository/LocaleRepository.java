package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;

public interface LocaleRepository extends JpaRepository<LocaleJpaEntity, Long> {

	List<LocaleJpaEntity> findAll(Sort sort);
}
