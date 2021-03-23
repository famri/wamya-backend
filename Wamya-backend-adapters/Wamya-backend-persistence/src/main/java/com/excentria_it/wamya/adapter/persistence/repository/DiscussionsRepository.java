package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;

public interface DiscussionsRepository extends JpaRepository<DiscussionJpaEntity, Long> {

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t LEFT JOIN FETCH  d.messages m WHERE t.id = ?1", countQuery = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1")
	Page<DiscussionJpaEntity> findByTransporter_IdWithMessages(Long userAccountId, Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t LEFT JOIN FETCH  d.messages m WHERE t.id = ?1 AND d.active = ?2", countQuery = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1 AND d.active = ?2")
	Page<DiscussionJpaEntity> findByTransporter_IdAndActiveWithMessages(Long userAccountId, Boolean isActive,
			Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c LEFT JOIN FETCH  d.messages m WHERE c.id = ?1", countQuery = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1")
	Page<DiscussionJpaEntity> findByClient_IdWithMessages(Long userAccountId, Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c LEFT JOIN FETCH  d.messages m WHERE c.id = ?1 AND d.active = ?2", countQuery = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1 AND d.active = ?2")
	Page<DiscussionJpaEntity> findByClient_IdAndActiveWithMessages(Long userAccountId, Boolean isActive,
			Pageable pagingSort);

}
