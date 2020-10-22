package com.excentria_it.messaging.gateway.sms;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.SMSMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SMSRequestReceiver {

	private static String SMS_GATEWAY_URL = "http://{host}:{port}/cgi-bin/sendsms?username={username}&password={password}&to={destination}&text={content}";

	private final SMSGatewayProperties smsGatewayProperties;

	private final RestTemplate restTemplate;

	private final TemplateManager templateManager;

	private static final String TEMPLATES_BASEDIR = "templates";

	public boolean receiveSMSRequest(SMSMessage message) {
		log.info("Start processing SMSMessage: {}", message);
		String to = message.getTo();

		templateManager.configure(TEMPLATES_BASEDIR);
		boolean loadingResult = templateManager.loadTemplate(message.getTemplate().name(), message.getParams(),
				TemplateType.SMS, message.getLanguage());

		if (!loadingResult)
			return false;

		String encodedContent;
		try {
			encodedContent = this.toURIEncoded(templateManager.renderTemplate(),
					java.nio.charset.StandardCharsets.UTF_8);

			ResponseEntity<String> response = restTemplate.getForEntity(SMS_GATEWAY_URL, String.class,
					smsGatewayProperties.getHost(), smsGatewayProperties.getPort(), smsGatewayProperties.getUsername(),
					smsGatewayProperties.getPassword(), to, encodedContent);

			log.debug("RESULT OF SENDING SMS TO {} ===> {}", to, response.getBody());
		} catch (UnsupportedEncodingException e) {
			log.error("Encoding SMS template content to %s", e);
			return false;
		} catch (Exception e) {
			log.error("Exception when processiong SMSMessage: ", e);
			return false;
		}
		log.info("End processing SMSMessage: {}", message);
		return true;

	}

	protected String toURIEncoded(String content, java.nio.charset.Charset encoding)
			throws UnsupportedEncodingException {

		return java.net.URLEncoder.encode(content, encoding);

	}
}
