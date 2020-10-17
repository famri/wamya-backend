package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.SMSMessage;

public interface MessagingPort {
	void sendSMSMessage(SMSMessage message);

	void sendEmailMessage(EmailMessage messaage);
}
