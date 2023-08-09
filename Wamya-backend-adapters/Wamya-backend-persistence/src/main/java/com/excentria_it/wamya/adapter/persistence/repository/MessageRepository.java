package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;

public interface MessageRepository extends JpaRepository<MessageJpaEntity, Long> {
	Page<MessageJpaEntity> findByDiscussion_Id(Long discussionId, Pageable pagingSort);

	@Modifying(flushAutomatically = true)
	@Query(value = "UPDATE MessageJpaEntity m SET m.read = ?2 WHERE m.id in ?1")
	void batchUpdateReadMessages(List<Long> messagesIds, boolean isRead);

	@Query(value = "SELECT COUNT(m) FROM MessageJpaEntity m JOIN m.author a JOIN m.discussion d JOIN d.client c WHERE d.active = true AND c.email = ?1 AND a.id <> c.id AND m.read = ?2")
	Long countClientMessages(String username, Boolean read);

	@Query(value = "SELECT COUNT(m) FROM MessageJpaEntity m JOIN m.author a JOIN m.discussion d JOIN d.transporter t WHERE d.active = true AND t.email = ?1 AND a.id <> t.id AND m.read = ?2")
	Long countTransporterMessages(String username, Boolean read);

}
