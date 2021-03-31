package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;

public interface MessageRepository extends JpaRepository<MessageJpaEntity, Long> {
	Page<MessageJpaEntity> findByDiscussion_Id(Long discussionId, Pageable pagingSort);
}
