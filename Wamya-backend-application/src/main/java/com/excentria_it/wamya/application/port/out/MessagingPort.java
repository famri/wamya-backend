package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.MessageBuilder.Message;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

public interface MessagingPort {
	void sendMessage(MobilePhoneNumber mobileNumber, Message message);
}
