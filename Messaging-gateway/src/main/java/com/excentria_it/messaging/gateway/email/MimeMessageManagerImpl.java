package com.excentria_it.messaging.gateway.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class MimeMessageManagerImpl implements MimeMessageManager {

	public void prepareMimeMessage(MimeMessageHelper helper, String from, String to, String subject,
			String templateBody, Map<String, String> templateResources, Map<String, String> attachements)
			throws MessagingException, FileNotFoundException {

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(templateBody, true);

		this.addTemplateResources(helper, templateResources);
		this.addAttachements(helper, attachements);

	}

	protected void addTemplateResources(MimeMessageHelper helper, Map<String, String> templateResources)
			throws FileNotFoundException, MessagingException {
		if (templateResources == null || templateResources.isEmpty())
			return;

		for (String resourceId : templateResources.keySet()) {
			URL resourceURI = getClass().getClassLoader().getResource(templateResources.get(resourceId));

			if (resourceURI != null) {

				helper.addInline(resourceId, new File(resourceURI.getFile()));

			} else {
				throw new FileNotFoundException(String.format("Template resource path %s of resource ID %s not found.",
						templateResources.get(resourceId), resourceId));
			}
		}

	}

	protected void addAttachements(MimeMessageHelper helper, Map<String, String> attachements)
			throws MessagingException, FileNotFoundException {
		for (String contentId : attachements.keySet()) {

			File attachementFile = new File(attachements.get(contentId));

			log.debug(String.format("Attachement : %s exists ?: %b", attachements.get(contentId),
					attachementFile.exists()));
			if (attachementFile.exists()) {

				helper.addInline(contentId, attachementFile);

			} else {
				throw new FileNotFoundException(
						String.format("Template attachement path %s of attachement ID %s not found.",
								attachements.get(contentId), contentId));
			}
		}

	}
}
