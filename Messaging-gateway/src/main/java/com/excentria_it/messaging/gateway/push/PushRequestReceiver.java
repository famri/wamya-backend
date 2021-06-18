package com.excentria_it.messaging.gateway.push;

import com.excentria_it.wamya.common.domain.PushMessage;

public interface PushRequestReceiver {
	boolean receivePushRequest(PushMessage message);
}
