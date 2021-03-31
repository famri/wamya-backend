package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.Interlocutor;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

@Component
public class DiscussionMapper {

	public LoadDiscussionsOutput mapToLoadDiscussionsOutput(DiscussionJpaEntity discussionJpaEntity) {
		return LoadDiscussionsOutput.builder().id(discussionJpaEntity.getId()).active(discussionJpaEntity.getActive())
				.dateTime(discussionJpaEntity.getDateTime())
				.client(this.getInterlocutor(discussionJpaEntity.getClient()))
				.transporter(this.getInterlocutor(discussionJpaEntity.getTransporter()))
				.latestMessage(this.getMessageOutput(discussionJpaEntity.getLatestMessage())).build();
	}

	public Interlocutor getInterlocutor(UserAccountJpaEntity userAccount) {
		return Interlocutor.builder().id(userAccount.getId()).name(userAccount.getFirstname())
				.mobileNumber(userAccount.getIcc().getValue() + "_" + userAccount.getMobileNumber())
				.photoUrl(userAccount.getPhotoUrl()).build();
	}

	public MessageOutput getMessageOutput(MessageJpaEntity m) {
		return MessageOutput.builder().id(m.getId()).authorId(m.getAuthor().getId()).content(m.getContent())
				.dateTime(m.getDateTime()).read(m.getRead()).build();
	}

}
