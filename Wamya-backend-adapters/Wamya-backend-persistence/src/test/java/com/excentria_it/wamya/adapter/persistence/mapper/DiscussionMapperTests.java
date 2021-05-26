package com.excentria_it.wamya.adapter.persistence.mapper;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.InterlocutorOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

public class DiscussionMapperTests {

	private DiscussionMapper discussionMapper = new DiscussionMapper();

	@Test
	void testMapToLoadDiscussionsOutputFromNullDiscussionJpaEntity() {
		LoadDiscussionsOutput loadDiscussionsOutput = discussionMapper.mapToLoadDiscussionsOutput(null);
		assertEquals(null, loadDiscussionsOutput);
	}

	@Test
	void testGetInterlocutorOutputFromNullUserAccountJpaEntity() {
		InterlocutorOutput interlocutorOutput = discussionMapper.getInterlocutorOutput(null);
		assertEquals(null, interlocutorOutput);
	}

	@Test
	void testGetInterlocutorOutputFromNullUserAccountIccJpaEntity() {
		TransporterJpaEntity transporterJpaEntity = defaultExistentTransporterJpaEntity();
		transporterJpaEntity.setIcc(null);
		InterlocutorOutput interlocutorOutput = discussionMapper.getInterlocutorOutput(transporterJpaEntity);
		assertEquals(transporterJpaEntity.getMobileNumber(), interlocutorOutput.getMobileNumber());
	}

	@Test
	void testGetMessageOutputFromNullMessageJpaEntity() {
		MessageOutput messageOutput = discussionMapper.getMessageOutput(null);
		assertEquals(null, messageOutput);
	}

	@Test
	void testMapToClientLoadDiscussionsOutput() {
		DiscussionJpaEntity discussionJpaEntity = defaultDiscussionJpaEntity();
		LoadDiscussionsOutput loadDiscussionsOutput = discussionMapper.mapToLoadDiscussionsOutput(discussionJpaEntity);

		assertEquals(discussionJpaEntity.getId(), loadDiscussionsOutput.getId());
		assertEquals(discussionJpaEntity.getActive(), loadDiscussionsOutput.getActive());
		assertEquals(discussionJpaEntity.getDateTime(), loadDiscussionsOutput.getDateTime());

		assertEquals(discussionJpaEntity.getTransporter().getFirstname(),
				loadDiscussionsOutput.getTransporter().getName());
		assertEquals(discussionJpaEntity.getTransporter().getOauthId(), loadDiscussionsOutput.getTransporter().getId());
		assertEquals(
				discussionJpaEntity.getTransporter().getIcc().getValue() + "_"
						+ discussionJpaEntity.getTransporter().getMobileNumber(),
				loadDiscussionsOutput.getTransporter().getMobileNumber());

		assertEquals(
				DocumentUrlResolver.resolveUrl(discussionJpaEntity.getTransporter().getProfileImage().getId(),
						discussionJpaEntity.getTransporter().getProfileImage().getHash()),
				loadDiscussionsOutput.getTransporter().getPhotoUrl());

		assertEquals(discussionJpaEntity.getClient().getFirstname(), loadDiscussionsOutput.getClient().getName());
		assertEquals(discussionJpaEntity.getClient().getOauthId(), loadDiscussionsOutput.getClient().getId());
		assertEquals(
				discussionJpaEntity.getClient().getIcc().getValue() + "_"
						+ discussionJpaEntity.getClient().getMobileNumber(),
				loadDiscussionsOutput.getClient().getMobileNumber());

		assertEquals(
				DocumentUrlResolver.resolveUrl(discussionJpaEntity.getClient().getProfileImage().getId(),
						discussionJpaEntity.getTransporter().getProfileImage().getHash()),
				loadDiscussionsOutput.getClient().getPhotoUrl());

		assertEquals(discussionJpaEntity.getLatestMessage().getId(), loadDiscussionsOutput.getLatestMessage().getId());

		assertEquals(discussionJpaEntity.getLatestMessage().getRead(),
				loadDiscussionsOutput.getLatestMessage().getRead());

		assertEquals(discussionJpaEntity.getLatestMessage().getContent(),
				loadDiscussionsOutput.getLatestMessage().getContent());

		assertEquals(discussionJpaEntity.getLatestMessage().getDateTime(),
				loadDiscussionsOutput.getLatestMessage().getDateTime());

		assertEquals(discussionJpaEntity.getLatestMessage().getAuthor().getOauthId(),
				loadDiscussionsOutput.getLatestMessage().getAuthorId());

	}
}
