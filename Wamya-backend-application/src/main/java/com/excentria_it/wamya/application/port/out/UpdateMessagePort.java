package com.excentria_it.wamya.application.port.out;

import java.util.List;

public interface UpdateMessagePort {

	void updateRead(List<Long> messagesIds, boolean isRead);

}
