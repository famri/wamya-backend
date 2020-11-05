package com.excentria_it.wamya.adapter.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.common.rabbitmq.RabbitMqQueue;

@SpringBootTest
@ActiveProfiles(value = { "messaging-local" })
public class MessagingModuleTest {

	@Autowired
	@Qualifier(RabbitMqQueue.SMS_QUEUE)
	private Queue smsQueue;
	@Autowired
	@Qualifier(RabbitMqQueue.EMAIL_QUEUE)
	private Queue emailQueue;

	@Test
	void testBeansCreation() {

	}
}
