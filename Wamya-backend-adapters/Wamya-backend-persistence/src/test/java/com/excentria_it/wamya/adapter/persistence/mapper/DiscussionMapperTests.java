package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;

public class DiscussionMapperTests {

	private DiscussionMapper discussionMapper = new DiscussionMapper();

	@Test
	void testMapToClientLoadDiscussionsOutput() {
		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		LoadDiscussionsOutput loadDiscussionsOutput = discussionMapper.mapToLoadDiscussionsOutput(discussionJpaEntity);

		assertEquals(discussionJpaEntity.getId(), loadDiscussionsOutput.getId());
		assertEquals(discussionJpaEntity.getActive(), loadDiscussionsOutput.getActive());
		assertEquals(discussionJpaEntity.getDateTime(), loadDiscussionsOutput.getDateTime());

		assertEquals(discussionJpaEntity.getTransporter().getFirstname(),
				loadDiscussionsOutput.getTransporter().getName());
		assertEquals(discussionJpaEntity.getTransporter().getId(), loadDiscussionsOutput.getTransporter().getId());
		assertEquals(
				discussionJpaEntity.getTransporter().getIcc().getValue() + "_"
						+ discussionJpaEntity.getTransporter().getMobileNumber(),
				loadDiscussionsOutput.getTransporter().getMobileNumber());

		assertEquals(discussionJpaEntity.getTransporter().getPhotoUrl(),
				loadDiscussionsOutput.getTransporter().getPhotoUrl());

		assertEquals(discussionJpaEntity.getClient().getFirstname(), loadDiscussionsOutput.getClient().getName());
		assertEquals(discussionJpaEntity.getClient().getId(), loadDiscussionsOutput.getClient().getId());
		assertEquals(
				discussionJpaEntity.getClient().getIcc().getValue() + "_"
						+ discussionJpaEntity.getClient().getMobileNumber(),
				loadDiscussionsOutput.getClient().getMobileNumber());

		assertEquals(discussionJpaEntity.getClient().getPhotoUrl(), loadDiscussionsOutput.getClient().getPhotoUrl());

		assertEquals(discussionJpaEntity.getLatestMessage().getId(), loadDiscussionsOutput.getLatestMessage().getId());

		assertEquals(discussionJpaEntity.getLatestMessage().getRead(),
				loadDiscussionsOutput.getLatestMessage().getRead());

		assertEquals(discussionJpaEntity.getLatestMessage().getContent(),
				loadDiscussionsOutput.getLatestMessage().getContent());

		assertEquals(discussionJpaEntity.getLatestMessage().getDateTime(),
				loadDiscussionsOutput.getLatestMessage().getDateTime());

		assertEquals(discussionJpaEntity.getLatestMessage().getAuthor().getId(),
				loadDiscussionsOutput.getLatestMessage().getAuthorId());

	}
}
