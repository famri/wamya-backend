package com.excentria_it.messaging.gateway;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.messaging.gateway.email.EmailRequestReceiver;
import com.excentria_it.messaging.gateway.push.PushRequestReceiver;
import com.excentria_it.messaging.gateway.sms.SMSGatewayProperties;
import com.excentria_it.messaging.gateway.sms.SMSRequestReceiver;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

@Configuration
@EnableConfigurationProperties(value = SMSGatewayProperties.class)
//TODO add @ActiveProfiles("localtest") and configure local rabbitmq in application.yml
public class MessagingGatewayConfiguration {
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("mailhog");
		mailSender.setPort(1025);

		return mailSender;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public MessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public SimpleMessageListenerContainer smsMessageListenerContainer(ConnectionFactory connectionFactory,
			SMSRequestReceiver receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(RabbitMqQueue.SMS_QUEUE);
		container.setExposeListenerChannel(true);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setDefaultRequeueRejected(false);
		container.setExclusive(true);
		container.setConcurrentConsumers(1);
		container.setMessageListener(getSMSMessageMessageListener(receiver));

		return container;
	}

	protected ChannelAwareMessageListener getSMSMessageMessageListener(SMSRequestReceiver receiver) {
		return (ChannelAwareMessageListener) (message, channel) -> {
			SMSMessage smsMessage = (SMSMessage) jackson2JsonMessageConverter().fromMessage(message);
			boolean success = receiver.receiveSMSRequest(smsMessage);
			if (success) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		};
	}
//	@Bean
//	MessageListenerAdapter smsMessageListenerAdapter(SMSRequestReceiver receiver) {
//		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveSMSRequest");
//		messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter());
//
//		return messageListenerAdapter;
//	}

	@Bean
	public SimpleMessageListenerContainer emailMessageListenerContainer(ConnectionFactory connectionFactory,
			EmailRequestReceiver receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		container.setQueueNames(RabbitMqQueue.EMAIL_QUEUE);
		container.setExposeListenerChannel(true);
		container.setAcknowledgeMode(AcknowledgeMode.AUTO);
		container.setDefaultRequeueRejected(false);
		container.setExclusive(true);
		container.setConcurrentConsumers(1);
		container.setMessageListener(getEmailMessageMessageListener(receiver));

		return container;
	}

	protected ChannelAwareMessageListener getEmailMessageMessageListener(EmailRequestReceiver receiver) {
		return (ChannelAwareMessageListener) (message, channel) -> {
			EmailMessage emailMessage = (EmailMessage) jackson2JsonMessageConverter().fromMessage(message);
			boolean success = receiver.receiveEmailRequest(emailMessage);
			if (success) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		};
	}
//	@Bean
//	MessageListenerAdapter emailMessageListenerAdapter(EmailRequestReceiver receiver) {
//		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, "receiveEmailRequest");
//		messageListenerAdapter.setMessageConverter(jackson2JsonMessageConverter());
//		return messageListenerAdapter;
//
//	}

	@Bean
	public SimpleMessageListenerContainer pushMessageListenerContainer(ConnectionFactory connectionFactory,
			PushRequestReceiver receiver) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(RabbitMqQueue.PUSH_QUEUE);
		container.setExposeListenerChannel(true);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		container.setDefaultRequeueRejected(false);
		container.setExclusive(true);
		container.setConcurrentConsumers(1);
		container.setMessageListener(getPushMessageMessageListener(receiver));

		return container;
	}

	protected ChannelAwareMessageListener getPushMessageMessageListener(PushRequestReceiver receiver) {
		return (ChannelAwareMessageListener) (message, channel) -> {
			PushMessage pushMessage = (PushMessage) jackson2JsonMessageConverter().fromMessage(message);
			boolean success = receiver.receivePushRequest(pushMessage);
			if (success) {
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			}
		};
	}
}
