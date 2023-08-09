package com.excentria_it.messaging.gateway.email;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.test.data.common.EmailMessageTestData;

@ExtendWith(MockitoExtension.class)
public class EmailRequestListenerTests {

	@Mock
	private EmailService emailService;

	@InjectMocks
	private EmailRequestReceiver emailRequestReceiver;

	@Test
	void whenReceiveEmailRequest_ThenSendEmailWithHTMTemplateShouldBeCalledOnce() {
		EmailMessage emailMessage = EmailMessageTestData.defaultEmailMessageBuilder().build();

		emailRequestReceiver.receiveEmailRequest(emailMessage);

		then(emailService).should(times(1)).sendEmailWithHTMTemplate(emailMessage.getFrom(), emailMessage.getTo(),
				emailMessage.getSubject(), emailMessage.getTemplate(), emailMessage.getLanguage(),
				emailMessage.getParams(), emailMessage.getAttachements());

	}
}
