package com.excentria_it.messaging.gateway.push;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excentria_it.wamya.common.domain.PushMessage;

public class PushRequestDummyReceiverImpl implements PushRequestReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(PushRequestDummyReceiverImpl.class);

	@Override
	public boolean receivePushRequest(PushMessage message) {
		LOGGER.info(message.toString());
		return true;
	}

}