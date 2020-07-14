package com.codisiac.wamya.adapter.messaging.adapter;

import com.codisiac.wamya.application.port.out.*;
import com.codisiac.wamya.common.annotation.*;
import com.codisiac.wamya.domain.MessageBuilder.*;
import com.codisiac.wamya.domain.UserAccount.*;

import lombok.*;

@RequiredArgsConstructor
@PersistenceAdapter
public class SmsMessagingAdapter implements MessagingPort {

	@Override
	public void sendMessage(MobilePhoneNumber mobileNumber, Message message) {
		System.out.println("Sent message ====================> " + message.getContent());
	}

}
