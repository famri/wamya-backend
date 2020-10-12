package com.excentria_it.messaging.gateway.email;

import java.util.Map;

import com.excentria_it.wamya.common.domain.EmailTemplate;

public interface EmailService {

	boolean sendEmailWithHTMTemplate(String from, String to, String subject, EmailTemplate templateName, String language,
			Map<String, String> templateParams, Map<String, String> attachements);

}
