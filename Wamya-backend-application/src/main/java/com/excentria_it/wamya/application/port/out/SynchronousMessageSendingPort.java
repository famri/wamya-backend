package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;

public interface SynchronousMessageSendingPort {

	void sendMessage(MessageDto messageDto, Long discussionID, String receiverUsername);

}
