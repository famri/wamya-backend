package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.DiscussionJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.MessageJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.DiscussionMapper;
import com.excentria_it.wamya.adapter.persistence.repository.DiscussionRepository;
import com.excentria_it.wamya.adapter.persistence.repository.MessageRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.AddMessageToDiscussionPort;
import com.excentria_it.wamya.application.port.out.LoadMessagesPort;
import com.excentria_it.wamya.application.port.out.UpdateMessagePort;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.utils.ParameterUtils;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;
import com.excentria_it.wamya.domain.LoadMessagesOutputResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@PersistenceAdapter
public class ChatMessagePersistenceAdapter implements AddMessageToDiscussionPort, LoadMessagesPort, UpdateMessagePort {

    private final MessageRepository messageRepository;
    private final DiscussionRepository discussionRepository;
    private final UserAccountRepository userAccountRepository;
    private final DiscussionMapper discussionMapper;

    @Override
    public MessageOutput addMessage(Long discussionId, String senderOauthId, String messageContent) {
        Optional<UserAccountJpaEntity> userAccountOptional = userAccountRepository.findByOauthId(senderOauthId);
        if (userAccountOptional.isEmpty())
            return null;

        Optional<DiscussionJpaEntity> discussionJpaEntityOptional = discussionRepository.findById(discussionId);
        if (discussionJpaEntityOptional.isEmpty())
            return null;

        DiscussionJpaEntity discussionJpaEntity = discussionJpaEntityOptional.get();

        MessageJpaEntity message = new MessageJpaEntity(userAccountOptional.get(), false, messageContent, Instant.now(),
                discussionJpaEntityOptional.get());

        message = messageRepository.save(message);

        discussionJpaEntity.setActive(true);
        discussionJpaEntity.setLatestMessage(message);

        discussionRepository.save(discussionJpaEntity);

        return new MessageOutput(message.getId(), userAccountOptional.get().getOauthId(), messageContent,
                message.getDateTime(), message.getRead());
    }

    @Override
    public LoadMessagesOutputResult loadMessages(Long discussionId, Integer pageNumber, Integer pageSize,
                                                 SortCriterion sortingCriterion) {

        Sort sort = convertToSort(sortingCriterion);

        Pageable pagingSort = PageRequest.of(pageNumber, pageSize, sort);
        Page<MessageJpaEntity> messagesPage = messageRepository.findByDiscussion_Id(discussionId, pagingSort);

        return new LoadMessagesOutputResult(messagesPage.getTotalPages(), messagesPage.getTotalElements(),
                messagesPage.getNumber(), messagesPage.getSize(), messagesPage.hasNext(), messagesPage.getContent()
                .stream().map(m -> discussionMapper.getMessageOutput(m)).collect(Collectors.toList()));
    }

    protected Sort convertToSort(SortCriterion sortingCriterion) {

        return Sort.by(Direction.valueOf(sortingCriterion.getDirection().toUpperCase()),
                ParameterUtils.kebabToCamelCase(sortingCriterion.getField()));

    }

    @Override
    public void updateRead(List<Long> messagesIds, boolean isRead) {
        if (CollectionUtils.isNotEmpty(messagesIds)) {
            messageRepository.batchUpdateReadMessages(messagesIds, isRead);
        }
    }

    @Override
    public Long countMessages(String username, Boolean read, Boolean isTransporter) {
        if (isTransporter) {
            return messageRepository.countTransporterMessages(username, read);

        } else {
            return messageRepository.countClientMessages(username, read);
        }

    }


}
