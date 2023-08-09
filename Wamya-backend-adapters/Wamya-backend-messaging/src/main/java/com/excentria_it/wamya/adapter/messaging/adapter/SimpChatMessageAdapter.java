package com.excentria_it.wamya.adapter.messaging.adapter;

import com.excentria_it.wamya.application.port.out.SendMessageNotificationPort;
import com.excentria_it.wamya.application.port.out.SynchronousMessageSendingPort;
import com.excentria_it.wamya.common.annotation.WebSocketAdapter;
import com.excentria_it.wamya.domain.ChatMessage;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.ReadUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.List;

@WebSocketAdapter
@RequiredArgsConstructor
public class SimpChatMessageAdapter implements SynchronousMessageSendingPort, SendMessageNotificationPort {

    private final SimpMessageSendingOperations simpMessagingTemplate;

    @Override
    public void sendMessage(MessageDto messageDto, Long discussionId, String receiverSubject) {
        simpMessagingTemplate.convertAndSendToUser(receiverSubject, "/exchange/amq.direct/messages",
                new ChatMessage(messageDto, discussionId));

    }

    @Override
    public void sendReadNotification(String receiverSubject, Long discussionId, List<Long> messagesIds) {
        simpMessagingTemplate.convertAndSendToUser(receiverSubject, "/exchange/amq.direct/read-messages",
                new ReadUpdate(discussionId, messagesIds));

    }

}
