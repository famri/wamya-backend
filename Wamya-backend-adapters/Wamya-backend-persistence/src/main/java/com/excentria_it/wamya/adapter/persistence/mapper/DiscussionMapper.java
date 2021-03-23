package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.Interlocutor;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

@Component
public class DiscussionMapper {

	public LoadDiscussionsOutput mapToTransporterLoadDiscussionsOutput(DiscussionJpaEntity discussionJpaEntity) {
		return LoadDiscussionsOutput.builder().id(discussionJpaEntity.getId()).active(discussionJpaEntity.getActive())
				.dateTime(discussionJpaEntity.getDateTime())
				.interlocutor(this.getInterlocutor(discussionJpaEntity.getClient())).messages(discussionJpaEntity
						.getMessages().stream().map(m -> this.getMessageOutput(m)).collect(Collectors.toList()))
				.build();
	}

	public LoadDiscussionsOutput mapToClientLoadDiscussionsOutput(DiscussionJpaEntity discussionJpaEntity) {
		return LoadDiscussionsOutput.builder().id(discussionJpaEntity.getId()).active(discussionJpaEntity.getActive())
				.dateTime(discussionJpaEntity.getDateTime())
				.interlocutor(this.getInterlocutor(discussionJpaEntity.getTransporter())).messages(discussionJpaEntity
						.getMessages().stream().map(m -> this.getMessageOutput(m)).collect(Collectors.toList()))
				.build();
	}

	private Interlocutor getInterlocutor(UserAccountJpaEntity userAccount) {
		return Interlocutor.builder().id(userAccount.getId()).email(userAccount.getEmail())
				.mobileNumber(userAccount.getIcc().getValue() + "_" + userAccount.getMobileNumber())
				.name(userAccount.getFirstname()).photoUrl(userAccount.getPhotoUrl()).build();
	}

	private MessageOutput getMessageOutput(MessageJpaEntity m) {
		return MessageOutput.builder().id(m.getId()).authorEmail(m.getAuthor().getEmail())
				.authorMobileNumber(m.getAuthor().getIcc().getValue() + "_" + m.getAuthor().getMobileNumber())
				.content(m.getContent()).dateTime(m.getDateTime()).read(m.getRead()).build();
	}
}
