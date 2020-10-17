package com.excentria_it.messaging.gateway.sms;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class SMSRequestListener {

	private static String SMS_GATEWAY_URL = "http://{host}:{port}/cgi-bin/sendsms?username={username}&password={password}&to={destination}&text={content}";

	private final SMSGatewayProperties smsGatewayProperties;

	private final RestTemplate restTemplate;

	private final TemplateManager templateManager;

	@RabbitListener(queues = RabbitMqQueue.SMS_QUEUE)
	public boolean receiveSMSRequest(SMSMessage message) {

		String to = message.getTo();

		try {
			templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.SMS,
					message.getLanguage());
		} catch (FileNotFoundException e1) {

			log.error(e1.getMessage(), e1);
			return false;
		}

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
		}

		return true;

	}

	protected String toURIEncoded(String content, java.nio.charset.Charset encoding)
			throws UnsupportedEncodingException {

		return java.net.URLEncoder.encode(content, encoding);

	}
}
