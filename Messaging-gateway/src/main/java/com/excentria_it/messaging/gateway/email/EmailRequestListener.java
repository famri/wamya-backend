package com.excentria_it.messaging.gateway.email;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailRequestListener {

	private final EmailService emailService;

	@RabbitListener(queues = RabbitMqQueue.EMAIL_QUEUE)
	public void receiveEmailRequest(EmailMessage message) {

		emailService.sendEmailWithHTMTemplate(message.getFrom(), message.getTo(), message.getSubject(),
				message.getTemplate(), message.getLanguage(), message.getParams(),
				message.getAttachements());
	}

}