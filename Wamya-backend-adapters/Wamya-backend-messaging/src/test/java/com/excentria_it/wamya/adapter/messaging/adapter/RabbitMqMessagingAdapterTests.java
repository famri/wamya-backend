package com.excentria_it.wamya.adapter.messaging.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailMessage.EmailMessageBuilder;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSMessage.SMSMessageBuilder;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;
import com.excentria_it.wamya.test.data.common.EmailMessageTestData;
import com.excentria_it.wamya.test.data.common.SMSMessageTestData;

@ExtendWith(MockitoExtension.class)
public class RabbitMqMessagingAdapterTests {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@Spy
	@InjectMocks
	private RabbitMqMessagingAdapter rabbitMqMessagingAdapter;

	// Test validateSMSMessage
	@Test
	void givenValidSMSMessage_WhenValidateSMSMessage_ThenShouldNeverThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();

		assertDoesNotThrow(() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNullTo_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.to(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNullLanguage_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.language(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNullTemplate_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.template(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndNullParams_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.params(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndEmptyParams_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.params(Map.of());

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndWrongParamsNames_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.template(SMSTemplate.PHONE_VALIDATION);
		sMSMessageBuilder.params(
				Map.of(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0) + "_wrong_key", "any_param_value"));

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}

	@Test
	void givenNullToAndNullLanguage_WhenValidateSMSMessage_ThenShouldThrowIllegalArgumentException() {
		SMSMessageBuilder sMSMessageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		sMSMessageBuilder.to(null);
		sMSMessageBuilder.language(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateSMSMessage(sMSMessageBuilder.build()));
	}
	// Test validateEmailMessage

	@Test
	void givenValidEmailMessage_WhenValidateEmailMessage_ThenShouldNeverThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();

		assertDoesNotThrow(() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullFrom_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.from(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullTo_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.subject(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullSubject_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.to(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullLanguage_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.language(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullTemplate_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.template(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndNullParamse_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.params(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndEmptyParamse_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.params(Map.of());

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNonNullTemplateParamsAndWrongParamsNamesNullTemplate_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.template(EmailTemplate.EMAIL_VALIDATION);
		emailMessageBuilder.params(
				Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0) + "_wrong_key", "any_param_value"));
		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	@Test
	void givenNullFromAndNullTo_WhenValidateEmailMessage_ThenShouldThrowIllegalArgumentException() {
		EmailMessageBuilder emailMessageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		emailMessageBuilder.from(null);
		emailMessageBuilder.to(null);

		assertThrows(IllegalArgumentException.class,
				() -> rabbitMqMessagingAdapter.validateEmailMessage(emailMessageBuilder.build()));
	}

	// Test sendSMSMessage

	@Test
	void givenValidSMSMessage_WhenSendSMSMessage_ThenConvertAndSend() {

		SMSMessage message = SMSMessageTestData.defaultSMSMessageBuilder().build();

		rabbitMqMessagingAdapter.sendSMSMessage(message);

		then(rabbitTemplate).should(times(1)).convertAndSend(RabbitMqQueue.SMS_QUEUE, message);
	}

	@Test
	void givenInvalidSMSMessage_WhenSendSMSMessage_ThenNeverConvertAndSend() {

		SMSMessageBuilder messageBuilder = SMSMessageTestData.defaultSMSMessageBuilder();
		messageBuilder.to(null);
		SMSMessage message = messageBuilder.build();

		assertThrows(IllegalArgumentException.class, () -> rabbitMqMessagingAdapter.sendSMSMessage(message));

		then(rabbitTemplate).should(never()).convertAndSend(RabbitMqQueue.SMS_QUEUE, message);
	}

	// Test sendEmailMessage
	@Test
	void givenValidEmailMessage_WhenSendEmailMessage_ThenConvertAndSend() {
		EmailMessage message = EmailMessageTestData.defaultEmailMessageBuilder().build();

		rabbitMqMessagingAdapter.sendEmailMessage(message);

		then(rabbitTemplate).should(times(1)).convertAndSend(RabbitMqQueue.EMAIL_QUEUE, message);
	}

	@Test
	void givenValidEmailMessage_WhenSendEmailMessage_ThenNeverConvertAndSend() {
		EmailMessageBuilder messageBuilder = EmailMessageTestData.defaultEmailMessageBuilder();
		messageBuilder.from(null);

		EmailMessage message = messageBuilder.build();
		assertThrows(IllegalArgumentException.class, () -> rabbitMqMessagingAdapter.sendEmailMessage(message));

		then(rabbitTemplate).should(never()).convertAndSend(RabbitMqQueue.EMAIL_QUEUE, message);
	}

}
