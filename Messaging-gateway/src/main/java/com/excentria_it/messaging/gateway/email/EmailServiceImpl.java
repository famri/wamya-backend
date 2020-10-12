package com.excentria_it.messaging.gateway.email;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.EmailTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Component
@Slf4j
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender emailSender;

	private final TemplateManager templateManager;

	private final MimeMessageManager mimeMessageManager;

	public boolean sendEmailWithHTMTemplate(String from, String to, String subject, EmailTemplate template,
			String language, Map<String, String> templateParams, Map<String, String> attachements) {

		try {
			templateManager.loadTemplate(template.name(), templateParams, TemplateType.EMAIL, language);
		} catch (FileNotFoundException e1) {
			log.error(e1.getMessage(), e1);
			return false;
		}

		MimeMessage message = emailSender.createMimeMessage();

		try {

			String templateBody = templateManager.renderTemplate();

			MimeMessageHelper helper = this.createMimeMessageHelper(message);

			mimeMessageManager.prepareMimeMessage(helper, from, to, subject, templateBody,
					template.getTemplateResources(), attachements);

			emailSender.send(message);

		} catch (MessagingException | FileNotFoundException e) {
			log.error("Error occured when preparing mime message", e);
			return false;
		}

		return true;

	}

	protected MimeMessageHelper createMimeMessageHelper(MimeMessage message) throws MessagingException {
		return new MimeMessageHelper(message, true);
	}

}
