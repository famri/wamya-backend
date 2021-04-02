package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.InterlocutorOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

@Component
public class DiscussionMapper {

	public LoadDiscussionsOutput mapToLoadDiscussionsOutput(DiscussionJpaEntity discussionJpaEntity) {
		return LoadDiscussionsOutput.builder().id(discussionJpaEntity.getId()).active(discussionJpaEntity.getActive())
				.dateTime(discussionJpaEntity.getDateTime())
				.client(this.getInterlocutorOutput(discussionJpaEntity.getClient()))
				.transporter(this.getInterlocutorOutput(discussionJpaEntity.getTransporter()))
				.latestMessage(this.getMessageOutput(discussionJpaEntity.getLatestMessage())).build();
	}

	public InterlocutorOutput getInterlocutorOutput(UserAccountJpaEntity userAccount) {
		return InterlocutorOutput.builder().id(userAccount.getId()).name(userAccount.getFirstname())
				.email(userAccount.getEmail())
				.mobileNumber(userAccount.getIcc().getValue() + "_" + userAccount.getMobileNumber())
				.photoUrl(userAccount.getPhotoUrl()).build();
	}

	public MessageOutput getMessageOutput(MessageJpaEntity m) {
		return MessageOutput.builder().id(m.getId()).authorId(m.getAuthor().getId()).content(m.getContent())
				.dateTime(m.getDateTime()).read(m.getRead()).build();
	}


}
