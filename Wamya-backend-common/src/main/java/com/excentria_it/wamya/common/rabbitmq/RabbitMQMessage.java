package com.excentria_it.wamya.common.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RabbitMQMessage {
	private String destination;
	private String content;
}
