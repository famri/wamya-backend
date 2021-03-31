package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;

public interface DiscussionRepository extends JpaRepository<DiscussionJpaEntity, Long> {

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1", countQuery = "SELECT COUNT(d) FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1")
	Page<DiscussionJpaEntity> findByTransporter_Id(Long userAccountId, Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1 AND d.active = ?2", countQuery = "SELECT COUNT(d) FROM DiscussionJpaEntity d JOIN d.transporter t WHERE t.id = ?1 AND d.active = ?2")
	Page<DiscussionJpaEntity> findByTransporter_IdAndActive(Long userAccountId, Boolean isActive,
			Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1", countQuery = "SELECT COUNT(d) FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1")
	Page<DiscussionJpaEntity> findByClient_Id(Long userAccountId, Pageable pagingSort);

	@Query(value = "SELECT d FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1 AND d.active = ?2", countQuery = "SELECT COUNT(d) FROM DiscussionJpaEntity d JOIN d.client c WHERE c.id = ?1 AND d.active = ?2")
	Page<DiscussionJpaEntity> findByClient_IdAndActive(Long userAccountId, Boolean isActive,
			Pageable pagingSort);

	Optional<DiscussionJpaEntity> findByClient_IdAndTransporter_Id(Long clientId, Long transporterId);

}
