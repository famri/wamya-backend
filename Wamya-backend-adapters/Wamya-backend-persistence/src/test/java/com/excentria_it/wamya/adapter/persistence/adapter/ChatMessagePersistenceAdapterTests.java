package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.MessageJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.adapter.persistence.repository.MessageRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class ChatMessagePersistenceAdapterTests {
	@Mock
	private MessageRepository messageRepository;
	@Mock
	private DiscussionRepository discussionRepository;
	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private DocumentUrlResolver documentUrlResolver;
	@Mock
	private DiscussionMapper discussionMapper;

	@InjectMocks
	private ChatMessagePersistenceAdapter chatMessagePersistenceAdapter;

	@Test
	void givenExistentDiscussion_WhenAddMessage_ThenAddMessageToDiscussion() {

		// given
		UserAccountJpaEntity userAccountJpaEntity = defaultExistentClientJpaEntity();
		given(userAccountRepository.findByOauthId(any(Long.class))).willReturn(Optional.of(userAccountJpaEntity));

		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		given(discussionRepository.findById(any(Long.class))).willReturn(Optional.of(discussionJpaEntity));

		MessageJpaEntity messageJpaEntity = new MessageJpaEntity(userAccountJpaEntity, false, "some message",
				Instant.now(), discussionJpaEntity);
		messageJpaEntity.setId(1L);

		given(messageRepository.save(any(MessageJpaEntity.class))).willReturn(messageJpaEntity);
		ArgumentCaptor<MessageJpaEntity> messageJpaEntityCaptor = ArgumentCaptor.forClass(MessageJpaEntity.class);

		ArgumentCaptor<DiscussionJpaEntity> discussionJpaEntityCaptor = ArgumentCaptor
				.forClass(DiscussionJpaEntity.class);

		// when
		Instant start = Instant.now();
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 100L, "some message");
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
		assertEquals(messageJpaEntity.getAuthor().getOauthId(), messageOutput.getAuthorId());
		assertEquals(messageJpaEntity.getContent(), messageOutput.getContent());
		assertEquals(messageJpaEntity.getDateTime(), messageOutput.getDateTime());
		assertEquals(messageJpaEntity.getRead(), messageOutput.getRead());

	}

	@Test
	void givenInexistentDiscussion_WhenAddMessage_ThenReturnNull() {

		// given
		UserAccountJpaEntity userAccountJpaEntity = defaultExistentClientJpaEntity();
		given(userAccountRepository.findByOauthId(any(Long.class))).willReturn(Optional.of(userAccountJpaEntity));

		given(discussionRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 1L, "some message");

		// Then
		assertNull(messageOutput);
	}

	@Test
	void givenInexistentUserAccount_WhenAddMessage_ThenReturnNull() {

		// given
		given(userAccountRepository.findByOauthId(any(Long.class))).willReturn(Optional.empty());

		// when
		MessageOutput messageOutput = chatMessagePersistenceAdapter.addMessage(1L, 1L, "some message");

		// Then
		assertNull(messageOutput);
	}

	@Test
	void testLoadMessages() {
		// given
		Page<MessageJpaEntity> messagesPage = defaultMessageJpaEntityPage();
		given(messageRepository.findByDiscussion_Id(any(Long.class), any(Pageable.class))).willReturn(messagesPage);
		given(discussionMapper.getMessageOutput(any(MessageJpaEntity.class)))
				.willReturn(new MessageOutput(1L, 1L, "message", Instant.now(), false));
		// When

		LoadMessagesOutputResult result = chatMessagePersistenceAdapter.loadMessages(1L, 0, 25,
				new SortCriterion("date-time", "desc"));
		// then
		assertEquals(messagesPage.getTotalPages(), result.getTotalPages());
		assertEquals(messagesPage.getTotalElements(), result.getTotalElements());
		assertEquals(messagesPage.getNumber(), result.getPageNumber());
		assertEquals(messagesPage.getSize(), result.getPageSize());
		assertEquals(messagesPage.hasNext(), result.isHasNext());
		assertEquals(messagesPage.getContent().stream().map(m -> discussionMapper.getMessageOutput(m))
				.collect(Collectors.toList()), result.getContent());
	}

	@Test
	void testUpdateRead() {
		// given
		List<Long> messagesIds = List.of(1L, 2L, 3L);

		// When
		chatMessagePersistenceAdapter.updateRead(messagesIds, true);
		// then
		then(messageRepository).should(times(1)).batchUpdateReadMessages(messagesIds, true);
	}

	@Test
	void testUpdateReadWithEmptyMessageIdsList() {
		// given
		List<Long> messagesIds = Collections.emptyList();

		// When
		chatMessagePersistenceAdapter.updateRead(messagesIds, true);
		// then
		then(messageRepository).should(never()).batchUpdateReadMessages(any(List.class), any(Boolean.class));
	}

	@Test
	void givenTransporter_whenCountMessages_thenReturnMessagesCount() {
		// given
		given(messageRepository.countTransporterMessages(TestConstants.DEFAULT_EMAIL, false)).willReturn(5L);
		// When
		Long count = chatMessagePersistenceAdapter.countMessages(TestConstants.DEFAULT_EMAIL, false, true);
		// then
		then(messageRepository).should(times(1)).countTransporterMessages(TestConstants.DEFAULT_EMAIL, false);
		assertEquals(5L, count);
	}

	@Test
	void givenClient_whenCountMessages_thenReturnMessagesCount() {
		// given
		given(messageRepository.countClientMessages(TestConstants.DEFAULT_EMAIL, false)).willReturn(5L);
		// When
		Long count = chatMessagePersistenceAdapter.countMessages(TestConstants.DEFAULT_EMAIL, false, false);
		// then
		then(messageRepository).should(times(1)).countClientMessages(TestConstants.DEFAULT_EMAIL, false);
		assertEquals(5L, count);
	}
}
