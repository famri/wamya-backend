package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;

public class DiscussionJpaTestData {
	private static final Instant instant1 = ZonedDateTime.of(2021, 03, 19, 20, 00, 00, 0, ZoneId.of("UTC")).toInstant();
	private static final ClientJpaEntity client = defaultExistentClientJpaEntity();
	private static final TransporterJpaEntity transporter = defaultExistentTransporterJpaEntity();

	private static final List<MessageJpaEntity> messages = List.of(
			new MessageJpaEntity(client, true, "Hello!", instant1, null),
			new MessageJpaEntity(transporter, false, "Hello Sir! How can I help you?", instant1.plusSeconds(5), null));

	public static DiscussionJpaEntity defaultDiscussionJpaEntity() {

		DiscussionJpaEntity discussion = new DiscussionJpaEntity(client, transporter, true, instant1.plusSeconds(-5),
				null);

		discussion.setId(1L);
		discussion.setLatestMessage(new MessageJpaEntity(client, true, "Hello!", instant1, discussion));
		
		return discussion;
	}

	public static List<MessageJpaEntity> defaultMessageJpaEntityList() {
		messages.get(0).setId(1L);
		messages.get(1).setId(2L);

		return messages;
	}
}
