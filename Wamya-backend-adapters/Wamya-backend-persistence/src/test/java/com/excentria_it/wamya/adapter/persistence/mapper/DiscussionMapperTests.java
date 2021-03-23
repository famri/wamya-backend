package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;

public class DiscussionMapperTests {

	private DiscussionMapper discussionMapper = new DiscussionMapper();

	@Test
	void testMapToTransporterLoadDiscussionsOutput() {
		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		LoadDiscussionsOutput loadDiscussionsOutput = discussionMapper
				.mapToTransporterLoadDiscussionsOutput(discussionJpaEntity);

		assertEquals(discussionJpaEntity.getId(), loadDiscussionsOutput.getId());
		assertEquals(discussionJpaEntity.getActive(), loadDiscussionsOutput.getActive());
		assertEquals(discussionJpaEntity.getDateTime(), loadDiscussionsOutput.getDateTime());
		assertEquals(discussionJpaEntity.getClient().getFirstname(), loadDiscussionsOutput.getInterlocutor().getName());
		assertEquals(discussionJpaEntity.getClient().getEmail(), loadDiscussionsOutput.getInterlocutor().getEmail());
		assertEquals(
				discussionJpaEntity.getClient().getIcc().getValue() + "_"
						+ discussionJpaEntity.getClient().getMobileNumber(),
				loadDiscussionsOutput.getInterlocutor().getMobileNumber());

		assertEquals(discussionJpaEntity.getClient().getPhotoUrl(),
				loadDiscussionsOutput.getInterlocutor().getPhotoUrl());

		assertEquals(discussionJpaEntity.getMessages().size(), loadDiscussionsOutput.getMessages().size());

		for (int i = 0; i < loadDiscussionsOutput.getMessages().size(); i++) {

			assertEquals(discussionJpaEntity.getMessages().get(i).getId(),
					loadDiscussionsOutput.getMessages().get(i).getId());

			assertEquals(discussionJpaEntity.getMessages().get(i).getRead(),
					loadDiscussionsOutput.getMessages().get(i).getRead());

			assertEquals(discussionJpaEntity.getMessages().get(i).getContent(),
					loadDiscussionsOutput.getMessages().get(i).getContent());

			assertEquals(discussionJpaEntity.getMessages().get(i).getDateTime(),
					loadDiscussionsOutput.getMessages().get(i).getDateTime());

			assertEquals(discussionJpaEntity.getMessages().get(i).getAuthor().getEmail(),
					loadDiscussionsOutput.getMessages().get(i).getAuthorEmail());

			assertEquals(
					discussionJpaEntity.getMessages().get(i).getAuthor().getIcc().getValue() + "_"
							+ discussionJpaEntity.getMessages().get(i).getAuthor().getMobileNumber(),
					loadDiscussionsOutput.getMessages().get(i).getAuthorMobileNumber());

		}
	}

	@Test
	void testMapToClientLoadDiscussionsOutput() {
		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		LoadDiscussionsOutput loadDiscussionsOutput = discussionMapper
				.mapToClientLoadDiscussionsOutput(discussionJpaEntity);

		assertEquals(discussionJpaEntity.getId(), loadDiscussionsOutput.getId());
		assertEquals(discussionJpaEntity.getActive(), loadDiscussionsOutput.getActive());
		assertEquals(discussionJpaEntity.getDateTime(), loadDiscussionsOutput.getDateTime());
		assertEquals(discussionJpaEntity.getTransporter().getFirstname(),
				loadDiscussionsOutput.getInterlocutor().getName());
		assertEquals(discussionJpaEntity.getTransporter().getEmail(),
				loadDiscussionsOutput.getInterlocutor().getEmail());
		assertEquals(
				discussionJpaEntity.getTransporter().getIcc().getValue() + "_"
						+ discussionJpaEntity.getTransporter().getMobileNumber(),
				loadDiscussionsOutput.getInterlocutor().getMobileNumber());

		assertEquals(discussionJpaEntity.getTransporter().getPhotoUrl(),
				loadDiscussionsOutput.getInterlocutor().getPhotoUrl());

		assertEquals(discussionJpaEntity.getMessages().size(), loadDiscussionsOutput.getMessages().size());

		for (int i = 0; i < loadDiscussionsOutput.getMessages().size(); i++) {

			assertEquals(discussionJpaEntity.getMessages().get(i).getId(),
					loadDiscussionsOutput.getMessages().get(i).getId());

			assertEquals(discussionJpaEntity.getMessages().get(i).getRead(),
					loadDiscussionsOutput.getMessages().get(i).getRead());

			assertEquals(discussionJpaEntity.getMessages().get(i).getContent(),
					loadDiscussionsOutput.getMessages().get(i).getContent());

			assertEquals(discussionJpaEntity.getMessages().get(i).getDateTime(),
					loadDiscussionsOutput.getMessages().get(i).getDateTime());

			assertEquals(discussionJpaEntity.getMessages().get(i).getAuthor().getEmail(),
					loadDiscussionsOutput.getMessages().get(i).getAuthorEmail());

			assertEquals(
					discussionJpaEntity.getMessages().get(i).getAuthor().getIcc().getValue() + "_"
							+ discussionJpaEntity.getMessages().get(i).getAuthor().getMobileNumber(),
					loadDiscussionsOutput.getMessages().get(i).getAuthorMobileNumber());

		}

	}
}
