package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

public interface AddMessageToDiscussionPort {

	MessageOutput addMessage(Long discussionId, Long senderId, String messageContent);

}
