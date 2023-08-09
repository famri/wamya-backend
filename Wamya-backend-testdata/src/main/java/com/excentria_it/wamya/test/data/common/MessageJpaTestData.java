package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.DiscussionJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;

public class MessageJpaTestData {

	private static final Instant instant1 = ZonedDateTime.of(2021, 03, 19, 20, 00, 00, 0, ZoneId.of("UTC")).toInstant();
	private static final ClientJpaEntity client = defaultExistentClientJpaEntity();
	private static final TransporterJpaEntity transporter = defaultExistentTransporterJpaEntity();

	private static final DiscussionJpaEntity discussion = defaultDiscussionJpaEntity();
	private static List<MessageJpaEntity> messages;

	public static List<MessageJpaEntity> defaultMessageJpaEntityList() {

		messages = List.of(new MessageJpaEntity(client, true, "Hello!", instant1, discussion), new MessageJpaEntity(
				transporter, false, "Hello Sir! How can I help you?", instant1.plusSeconds(5), discussion));

		messages.get(0).setId(1L);
		messages.get(1).setId(2L);

		return messages;

	}

	public static Page<MessageJpaEntity> defaultMessageJpaEntityPage() {
		return new Page<MessageJpaEntity>() {

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

				return 2;
			}

			@Override
			public List<MessageJpaEntity> getContent() {

				return defaultMessageJpaEntityList();
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return Sort.by(Direction.DESC, "dateTime");
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
			public Iterator<MessageJpaEntity> iterator() {
				return defaultMessageJpaEntityList().iterator();
			}

			@Override
			public int getTotalPages() {

				return 1;
			}

			@Override
			public long getTotalElements() {

				return 2;
			}

			@Override
			public <U> Page<U> map(Function<? super MessageJpaEntity, ? extends U> converter) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
