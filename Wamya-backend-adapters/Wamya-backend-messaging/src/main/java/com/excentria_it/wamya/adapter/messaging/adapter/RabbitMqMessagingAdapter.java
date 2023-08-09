package com.excentria_it.wamya.adapter.messaging.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.common.annotation.MessagingAdapter;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@MessagingAdapter
public class RabbitMqMessagingAdapter implements AsynchronousMessagingPort {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void sendSMSMessage(SMSMessage message) {
		validateSMSMessage(message);
		rabbitTemplate.convertAndSend(RabbitMqQueue.SMS_QUEUE, message);

	}

	@Override
	public void sendEmailMessage(EmailMessage message) {
		validateEmailMessage(message);
		rabbitTemplate.convertAndSend(RabbitMqQueue.EMAIL_QUEUE, message);

	}

	protected boolean validateSMSMessage(SMSMessage message) {
		List<String> invalidParametersMessages = new ArrayList<>();

		if (message.getTo() == null) {
			invalidParametersMessages.add("SMSMessage.to is null");
		}
		if (message.getLanguage() == null) {
			invalidParametersMessages.add("SMSMessage.language is null");
		}
		if (message.getTemplate() == null) {
			invalidParametersMessages.add("SMSMessage.template is null");
		}
		if (message.getTemplate() != null) {
			List<String> templateParams = message.getTemplate().getTemplateParams();
			Map<String, String> givenParams = message.getParams();

			if (templateParams != null && !templateParams.isEmpty() && (givenParams == null || givenParams.isEmpty()
					|| !givenParams.keySet().containsAll(templateParams))) {
				invalidParametersMessages.add("SMSMessage.params do not match SMSMessage.template parameters");
			}

		}

		if (!invalidParametersMessages.isEmpty()) {
			StringBuilder validationErrorMessage = new StringBuilder("[");

			for (int i = 0; i < invalidParametersMessages.size() - 1; i++) {
				validationErrorMessage.append(invalidParametersMessages.get(i)).append(",");
			}
			validationErrorMessage.append(invalidParametersMessages.get(invalidParametersMessages.size() - 1))
					.append("]");

			throw new IllegalArgumentException("Invalid SMSMessage parameter: " + validationErrorMessage.toString());
		}
		return true;
	}

	protected boolean validateEmailMessage(EmailMessage message) {
		List<String> invalidParametersMessages = new ArrayList<>();
		if (message.getFrom() == null) {
			invalidParametersMessages.add("EmailMessage.from is null");
		}
		if (message.getTo() == null) {
			invalidParametersMessages.add("EmailMessage.to is null");
		}
		if (message.getSubject() == null) {
			invalidParametersMessages.add("EmailMessage.subject is null");
		}
		if (message.getLanguage() == null) {
			invalidParametersMessages.add("EmailMessage.locale is null");
		}
		if (message.getTemplate() == null) {
			invalidParametersMessages.add("EmailMessage.template is null");
		}
		if (message.getTemplate() != null) {
			List<String> templateParams = message.getTemplate().getTemplateParams();
			Map<String, String> givenParams = message.getParams();

			if (templateParams != null && !templateParams.isEmpty() && (givenParams == null || givenParams.isEmpty()
					|| !givenParams.keySet().containsAll(templateParams))) {
				invalidParametersMessages.add("EmailMessage.params do not match EmailMessage.template parameters");
			}
		}

		if (!invalidParametersMessages.isEmpty()) {
			StringBuilder validationErrorMessage = new StringBuilder("[");

			for (int i = 0; i < invalidParametersMessages.size() - 1; i++) {
				validationErrorMessage.append(invalidParametersMessages.get(i)).append(",");
			}
			validationErrorMessage.append(invalidParametersMessages.get(invalidParametersMessages.size() - 1))
					.append("]");

			throw new IllegalArgumentException("Invalid EmailMessage parameter: " + validationErrorMessage.toString());
		}

		return true;
	}

	protected boolean validatePushMessage(PushMessage message) {

		List<String> invalidParametersMessages = new ArrayList<>();

		if (message.getTo() == null) {
			invalidParametersMessages.add("PushMessage.to is null");
		}
		if (message.getLanguage() == null) {
			invalidParametersMessages.add("PushMessage.language is null");
		}
		if (message.getTemplate() == null) {
			invalidParametersMessages.add("PushMessage.template is null");
		}
		if (message.getTemplate() != null) {
			List<String> templateParams = message.getTemplate().getTemplateParams();
			Map<String, String> givenParams = message.getParams();

			if (templateParams != null && !templateParams.isEmpty() && (givenParams == null || givenParams.isEmpty()
					|| !givenParams.keySet().containsAll(templateParams))) {
				invalidParametersMessages.add("PushMessage.params do not match PushMessage.template parameters");
			}

		}

		if (!invalidParametersMessages.isEmpty()) {
			StringBuilder validationErrorMessage = new StringBuilder("[");

			for (int i = 0; i < invalidParametersMessages.size() - 1; i++) {
				validationErrorMessage.append(invalidParametersMessages.get(i)).append(",");
			}
			validationErrorMessage.append(invalidParametersMessages.get(invalidParametersMessages.size() - 1))
					.append("]");

			throw new IllegalArgumentException("Invalid PushMessage parameter: " + validationErrorMessage.toString());
		}
		return true;

	}

	@Override
	public void sendPushMessage(PushMessage message) {
		validatePushMessage(message);
		rabbitTemplate.convertAndSend(RabbitMqQueue.PUSH_QUEUE, message);

	}

}
