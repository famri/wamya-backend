package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.LocaleJpaEntity;

public interface UserLocaleRepository extends JpaRepository<LocaleJpaEntity, Long> {

}
