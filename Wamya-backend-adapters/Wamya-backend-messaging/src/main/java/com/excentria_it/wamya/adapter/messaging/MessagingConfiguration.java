package com.excentria_it.wamya.adapter.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

@Configuration
@EnableAsync
public class MessagingConfiguration {
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean(name = RabbitMqQueue.SMS_QUEUE)
	@Profile(value = { "messaging-local" })
	public Queue smsQueue() {
		return new Queue(RabbitMqQueue.SMS_QUEUE, false);
	}

	@Bean(name = RabbitMqQueue.EMAIL_QUEUE)
	@Profile(value = { "messaging-local" })
	public Queue emailQueue() {
		return new Queue(RabbitMqQueue.EMAIL_QUEUE, false);
	}
}
