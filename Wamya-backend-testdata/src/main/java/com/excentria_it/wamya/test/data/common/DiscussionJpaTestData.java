package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.MessageJpaTestData.*;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;

public class DiscussionJpaTestData {

	public static DiscussionJpaEntity defaultDiscussionJpaEntity() {

		DiscussionJpaEntity discussion = new DiscussionJpaEntity(
				(ClientJpaEntity) defaultMessageJpaEntityList().get(0).getAuthor(),
				(TransporterJpaEntity) defaultMessageJpaEntityList().get(1).getAuthor(), true,
				defaultMessageJpaEntityList().get(0).getDateTime().plusSeconds(-5),
				defaultMessageJpaEntityList().get(1));

		discussion.setId(1L);

		return discussion;
	}

}
