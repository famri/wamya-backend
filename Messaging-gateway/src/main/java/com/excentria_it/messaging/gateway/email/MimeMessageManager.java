package com.excentria_it.messaging.gateway.email;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;

public interface MimeMessageManager {
	void prepareMimeMessage(MimeMessageHelper helper, String from, String to, String subject, String templateBody,
			Map<String, String> templateResources, Map<String, String> attachements)
			throws MessagingException, FileNotFoundException;
}
