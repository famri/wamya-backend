package com.codisiac.wamya.application.port.out;

import com.codisiac.wamya.domain.MessageBuilder.Message;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;

public interface MessagingPort {
	void sendMessage(MobilePhoneNumber mobileNumber, Message message);
}
