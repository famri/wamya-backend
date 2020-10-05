package com.excentria_it.wamya.adapter.messaging.adapter;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.common.annotation.MessagingAdapter;
import com.excentria_it.wamya.common.rabbitmq.RabbitMQMessage;
import com.excentria_it.wamya.domain.MessageBuilder.Message;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MessagingAdapter
public class RabbitMQMessagingAdapter implements MessagingPort {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void sendMessage(MobilePhoneNumber mobileNumber, Message message) {
		rabbitTemplate.convertAndSend(new RabbitMQMessage(
				mobileNumber.getInternationalCallingCode() + mobileNumber.getMobileNumber(), message.getContent()));
		System.out.println("Sent message =====> " + message.getContent() + "   TO  =====>"
				+ mobileNumber.getInternationalCallingCode() + mobileNumber.getMobileNumber());
	}

}
