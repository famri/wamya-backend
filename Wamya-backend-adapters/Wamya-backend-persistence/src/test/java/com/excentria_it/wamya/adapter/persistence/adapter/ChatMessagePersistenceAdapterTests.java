package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.adapter.persistence.repository.MessageRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

@ExtendWith(MockitoExtension.class)
public class ChatMessagePersistenceAdapterTests {
	@Mock
	private MessageRepository messageRepository;
	@Mock
	private DiscussionRepository discussionRepository;
	@Mock
	private UserAccountRepository userAccountRepository;
	@InjectMocks
	private ChatMessagePersistenceAdapter chatMessagePersistenceAdapter;

	@Test
	void givenExistentDiscussion_WhenAddMessage_ThenAddMEssageToDiscussion() {

		// given
		UserAccountJpaEntity userAccountJpaEntity = defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccountJpaEntity));

		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		given(discussionRepository.findById(any(Long.class))).willReturn(Optional.of(discussionJpaEntity));

		MessageJpaEntity messageJpaEntity = new MessageJpaEntity(userAccountJpaEntity, false, "some message",
				Instant.now(), discussionJpaEntity);
		given(messageRepository.save(any(MessageJpaEntity.class))).willReturn(messageJpaEntity);
		ArgumentCaptor<MessageJpaEntity> messageJpaEntityCaptor = ArgumentCaptor.forClass(MessageJpaEntity.class);

		ArgumentCaptor<DiscussionJpaEntity> discussionJpaEntityCaptor = ArgumentCaptor
				.forClass(DiscussionJpaEntity.class);

		// when
		Instant start = Instant.now();
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 1L, "some message");
		Instant end = Instant.now();

		// Then
		then(discussionRepository).should(times(1)).save(discussionJpaEntityCaptor.capture());
		assertEquals(true, discussionJpaEntityCaptor.getValue().getActive());
		assertEquals(messageJpaEntity, discussionJpaEntityCaptor.getValue().getLatestMessage());

		then(messageRepository).should(times(1)).save(messageJpaEntityCaptor.capture());
		assertEquals(userAccountJpaEntity, messageJpaEntityCaptor.getValue().getAuthor());
		assertEquals(false, messageJpaEntityCaptor.getValue().getRead());
		assertEquals("some message", messageJpaEntityCaptor.getValue().getContent());

		assertThat(messageJpaEntityCaptor.getValue().getDateTime()).isAfter(start);
		assertThat(messageJpaEntityCaptor.getValue().getDateTime()).isBefore(end);

		assertEquals(discussionJpaEntity, messageJpaEntityCaptor.getValue().getDiscussion());

		assertEquals(messageJpaEntity.getId(), messageOutput.getId());
		assertEquals(messageJpaEntity.getAuthor().getId(), messageOutput.getAuthorId());
		assertEquals(messageJpaEntity.getContent(), messageOutput.getContent());
		assertEquals(messageJpaEntity.getDateTime(), messageOutput.getDateTime());
		assertEquals(messageJpaEntity.getRead(), messageOutput.getRead());

	}

	@Test
	void givenInexistentDiscussion_WhenAddMessage_ThenReturnNull() {

		// given
		UserAccountJpaEntity userAccountJpaEntity = defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccountJpaEntity));

		given(discussionRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 1L, "some message");

		// Then
		assertNull(messageOutput);
	}
	
	@Test
	void givenInexistentUserAccount_WhenAddMessage_ThenReturnNull() {

		// given
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());


		// when
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 1L, "some message");

		// Then
		assertNull(messageOutput);
	}

}
