package com.excentria_it.wamya.common.rabbitmq;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RabbitMqQueueTests {
	@Test
	void testRabbitMqQueueTypes() {
		assertEquals(RabbitMqQueue.EMAIL_QUEUE, "wamya.email.queue");
		assertEquals(RabbitMqQueue.SMS_QUEUE, "wamya.sms.queue");
	}

	@Test
	void testRabbitMqQueueInstance() {
		assertNotNull(new RabbitMqQueue());

	}
}
