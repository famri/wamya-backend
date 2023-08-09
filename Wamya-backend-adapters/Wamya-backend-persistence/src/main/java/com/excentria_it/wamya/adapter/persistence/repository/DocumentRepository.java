package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;

public interface DocumentRepository extends JpaRepository<DocumentJpaEntity, Long> {

}
