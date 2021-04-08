package com.excentria_it.wamya.application.port.out;

import java.util.List;

public interface SendMessageNotificationPort {

	void sendReadNotification(String receiverUsername, Long discussionId, List<Long> messagesIds);

}
