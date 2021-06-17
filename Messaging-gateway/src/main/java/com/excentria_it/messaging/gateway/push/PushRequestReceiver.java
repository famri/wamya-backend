package com.excentria_it.messaging.gateway.push;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.stereotype.Component;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.extern.slf4j.Slf4j;

@Component

@Slf4j
public class PushRequestReceiver {

	private final TemplateManager templateManager;

	private static final String TEMPLATES_BASEDIR = "templates";

	public PushRequestReceiver(TemplateManager templateManager) throws IOException {
		super();
		this.templateManager = templateManager;

		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.getApplicationDefault())
				.build();

		FirebaseApp.initializeApp(options);
	}

	public boolean receivePushRequest(PushMessage message) {
		log.info("Start processing PushMessage: {}", message);
		String to = message.getTo();

		templateManager.configure(TEMPLATES_BASEDIR);
		boolean loadingResult = templateManager.loadTemplate(message.getTemplate().name(), message.getParams(),
				TemplateType.PUSH, message.getLanguage());

		if (!loadingResult)
			return false;

		try {
			String templateContent = templateManager.renderTemplate();

			// This registration token comes from the client FCM SDKs.
			String registrationToken = message.getTo();

			// See documentation on defining a message payload.
			Message notification = Message.builder()
					.setNotification(Notification.builder().setTitle("FRETTO").setBody(templateContent).build())
					.putData("route", "/")

					.setToken(registrationToken).build();

			// Send a message to the device corresponding to the provided
			// registration token.
			String response = FirebaseMessaging.getInstance().send(notification);

			log.debug("RESULT OF SENDING PUSH TO {} ===> {}", to, response);
		} catch (Exception e) {
			log.error("Exception when processiong PushMessage: ", e);
			return false;
		}
		log.info("End processing PushMessage: {}", message);
		return true;

	}


}
