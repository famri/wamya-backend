package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionsRepository;
import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.Interlocutor;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutputResult;

@ExtendWith(MockitoExtension.class)
public class DiscussionsPersistenceAdapterTests {
	@Mock
	private DiscussionsRepository discussionsRepository;
	@Mock
	private DiscussionMapper discussionMapper;
	@InjectMocks
	private DiscussionsPersistenceAdapter discussionsPersistenceAdapter;

	@Test
	void givenTransporterDiscussion_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given
		Page<DiscussionJpaEntity> discussionsPage = givenDiscussionsPage();
		given(discussionsRepository.findByTransporter_IdAndActiveWithMessages(any(Long.class), any(Boolean.class),
				any(Pageable.class))).willReturn(discussionsPage);

		DiscussionJpaEntity discussion = discussionsPage.getContent().get(0);

		LoadDiscussionsOutput loadDiscussionsOutput = new LoadDiscussionsOutput(discussion.getId(),
				discussion.getActive(), discussion.getDateTime(), this.getMessageOutputList(discussion.getMessages()),
				this.getInterlocutor(discussion.getClient()));

		given(discussionMapper.mapToTransporterLoadDiscussionsOutput(discussionsPage.getContent().get(0)))
				.willReturn(loadDiscussionsOutput);
		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, true, 0, 25,
				new FilterCriterion("active", "true"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(discussionsPage.getNumber(), result.getPageNumber());
		assertEquals(discussionsPage.getSize(), result.getPageSize());
		assertEquals(discussionsPage.hasNext(), result.isHasNext());
		assertEquals(discussionsPage.getContent().size(), result.getTotalElements());
		assertEquals(discussionsPage.getTotalPages(), result.getTotalPages());
		assertEquals(discussionsPage.getContent().size(), result.getContent().size());
		assertEquals(loadDiscussionsOutput, result.getContent().get(0));

	}

	@Test
	void givenTransporterDiscussionWithEmptyFilter_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given
		Page<DiscussionJpaEntity> discussionsPage = givenDiscussionsPage();
		given(discussionsRepository.findByTransporter_IdWithMessages(any(Long.class), any(Pageable.class)))
				.willReturn(discussionsPage);

		DiscussionJpaEntity discussion = discussionsPage.getContent().get(0);

		LoadDiscussionsOutput loadDiscussionsOutput = new LoadDiscussionsOutput(discussion.getId(),
				discussion.getActive(), discussion.getDateTime(), this.getMessageOutputList(discussion.getMessages()),
				this.getInterlocutor(discussion.getClient()));

		given(discussionMapper.mapToTransporterLoadDiscussionsOutput(discussionsPage.getContent().get(0)))
				.willReturn(loadDiscussionsOutput);
		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, true, 0, 25,
				new SortCriterion("date-time", "desc"));
		// then
		assertEquals(discussionsPage.getNumber(), result.getPageNumber());
		assertEquals(discussionsPage.getSize(), result.getPageSize());
		assertEquals(discussionsPage.hasNext(), result.isHasNext());
		assertEquals(discussionsPage.getContent().size(), result.getTotalElements());
		assertEquals(discussionsPage.getTotalPages(), result.getTotalPages());
		assertEquals(discussionsPage.getContent().size(), result.getContent().size());
		assertEquals(loadDiscussionsOutput, result.getContent().get(0));

	}

	@Test
	void givenTransporterDiscussionWithBadFilterField_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given

		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, true, 0, 25,
				new FilterCriterion("someFilter", "someValue"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(0, result.getPageNumber());
		assertEquals(25, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertThat(result.getContent()).isEmpty();
		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());

	}

	@Test
	void givenClientDiscussion_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given
		Page<DiscussionJpaEntity> discussionsPage = givenDiscussionsPage();
		given(discussionsRepository.findByClient_IdAndActiveWithMessages(any(Long.class), any(Boolean.class),
				any(Pageable.class))).willReturn(discussionsPage);

		DiscussionJpaEntity discussion = discussionsPage.getContent().get(0);

		LoadDiscussionsOutput loadDiscussionsOutput = new LoadDiscussionsOutput(discussion.getId(),
				discussion.getActive(), discussion.getDateTime(), this.getMessageOutputList(discussion.getMessages()),
				this.getInterlocutor(discussion.getTransporter()));

		given(discussionMapper.mapToClientLoadDiscussionsOutput(discussionsPage.getContent().get(0)))
				.willReturn(loadDiscussionsOutput);
		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, false, 0, 25,
				new FilterCriterion("active", "true"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(discussionsPage.getNumber(), result.getPageNumber());
		assertEquals(discussionsPage.getSize(), result.getPageSize());
		assertEquals(discussionsPage.hasNext(), result.isHasNext());
		assertEquals(discussionsPage.getContent().size(), result.getTotalElements());
		assertEquals(discussionsPage.getTotalPages(), result.getTotalPages());
		assertEquals(discussionsPage.getContent().size(), result.getContent().size());
		assertEquals(loadDiscussionsOutput, result.getContent().get(0));

	}

	@Test
	void givenClientDiscussionWithEmptyFilter_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given
		Page<DiscussionJpaEntity> discussionsPage = givenDiscussionsPage();
		given(discussionsRepository.findByClient_IdWithMessages(any(Long.class), any(Pageable.class)))
				.willReturn(discussionsPage);

		DiscussionJpaEntity discussion = discussionsPage.getContent().get(0);

		LoadDiscussionsOutput loadDiscussionsOutput = new LoadDiscussionsOutput(discussion.getId(),
				discussion.getActive(), discussion.getDateTime(), this.getMessageOutputList(discussion.getMessages()),
				this.getInterlocutor(discussion.getTransporter()));

		given(discussionMapper.mapToClientLoadDiscussionsOutput(discussionsPage.getContent().get(0)))
				.willReturn(loadDiscussionsOutput);
		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, false, 0, 25,
				new SortCriterion("date-time", "desc"));
		// then
		assertEquals(discussionsPage.getNumber(), result.getPageNumber());
		assertEquals(discussionsPage.getSize(), result.getPageSize());
		assertEquals(discussionsPage.hasNext(), result.isHasNext());
		assertEquals(discussionsPage.getContent().size(), result.getTotalElements());
		assertEquals(discussionsPage.getTotalPages(), result.getTotalPages());
		assertEquals(discussionsPage.getContent().size(), result.getContent().size());
		assertEquals(loadDiscussionsOutput, result.getContent().get(0));

	}

	@Test
	void givenClientDiscussionWithBadFilterField_WhenLoadDiscussion_ThenReturnLoadDiscussionsOutputResult() {
		// given

		// when
		LoadDiscussionsOutputResult result = discussionsPersistenceAdapter.loadDiscussions(1L, false, 0, 25,
				new FilterCriterion("someFilter", "someValue"), new SortCriterion("date-time", "desc"));
		// then
		assertEquals(0, result.getPageNumber());
		assertEquals(25, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertThat(result.getContent()).isEmpty();
		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());

	}

	private List<MessageOutput> getMessageOutputList(List<MessageJpaEntity> messages) {
		return messages.stream()
				.map(m -> MessageOutput.builder().id(m.getId()).authorEmail(m.getAuthor().getEmail())
						.authorMobileNumber(m.getAuthor().getIcc().getValue() + "_" + m.getAuthor().getMobileNumber())
						.content(m.getContent()).dateTime(m.getDateTime()).read(m.getRead()).build())
				.collect(Collectors.toList());
	}

	private Interlocutor getInterlocutor(UserAccountJpaEntity userAccount) {
		return Interlocutor.builder().id(userAccount.getId()).email(userAccount.getEmail())
				.mobileNumber(userAccount.getIcc().getValue() + "_" + userAccount.getMobileNumber())
				.name(userAccount.getFirstname()).photoUrl(userAccount.getPhotoUrl()).build();
	}

	private Page<DiscussionJpaEntity> givenDiscussionsPage() {
		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();

		List<DiscussionJpaEntity> discussions = List.of(discussionJpaEntity);
		return new Page<DiscussionJpaEntity>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 25;
			}

			@Override
			public int getNumberOfElements() {

				return 1;
			}

			@Override
			public List<DiscussionJpaEntity> getContent() {

				return discussions;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return null;
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return true;
			}

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public boolean hasPrevious() {
				return false;
			}

			@Override
			public Pageable nextPageable() {
				return null;
			}

			@Override
			public Pageable previousPageable() {
				return null;
			}

			@Override
			public Iterator<DiscussionJpaEntity> iterator() {
				return discussions.iterator();
			}

			@Override
			public int getTotalPages() {

				return 1;
			}

			@Override
			public long getTotalElements() {

				return 1;
			}

			@Override
			public <U> Page<U> map(Function<? super DiscussionJpaEntity, ? extends U> converter) {

				return null;
			}

		};

	}

}
