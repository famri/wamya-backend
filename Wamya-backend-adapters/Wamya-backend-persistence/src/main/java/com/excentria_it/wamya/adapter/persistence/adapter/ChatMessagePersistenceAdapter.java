package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.Instant;
import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.adapter.persistence.repository.MessageRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ChatMessagePersistenceAdapter implements AddMessageToDiscussionPort {

	private final MessageRepository messageRepository;
	private final DiscussionRepository discussionRepository;
	private final UserAccountRepository userAccountRepository;

	@Override
	public MessageOutput addMessage(Long discussionId, Long senderId, String messageContent) {
		Optional<UserAccountJpaEntity> userAccountOptional = userAccountRepository.findById(senderId);
		if (userAccountOptional.isEmpty())
			return null;

		Optional<DiscussionJpaEntity> discussionJpaEntityOptional = discussionRepository.findById(discussionId);
		if (discussionJpaEntityOptional.isEmpty())
			return null;

		DiscussionJpaEntity discussionJpaEntity = discussionJpaEntityOptional.get();

		MessageJpaEntity message = new MessageJpaEntity(userAccountOptional.get(), false, messageContent, Instant.now(),
				discussionJpaEntityOptional.get());

		message = messageRepository.save(message);

		discussionJpaEntity.setActive(true);
		discussionJpaEntity.setLatestMessage(message);

		discussionRepository.save(discussionJpaEntity);

		return new MessageOutput(message.getId(), userAccountOptional.get().getId(), messageContent,
				message.getDateTime(), message.getRead());
	}

}
