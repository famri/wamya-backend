package com.excentria_it.messaging.gateway.email;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.common.domain.EmailMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailRequestReceiver {

	private final EmailService emailService;

	public boolean receiveEmailRequest(EmailMessage message) {
		log.info("Start processing EmailMessage: {}", message);
		boolean result = emailService.sendEmailWithHTMTemplate(message.getFrom(), message.getTo(), message.getSubject(),
				message.getTemplate(), message.getLanguage(), message.getParams(), message.getAttachements());
		log.info("End processing EmailMessage: {}", message);
		return result;
	}

}