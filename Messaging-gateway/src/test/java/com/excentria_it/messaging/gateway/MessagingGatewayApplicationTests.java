package com.excentria_it.messaging.gateway;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.excentria_it.messaging.gateway.email.EmailRequestReceiver;
import com.excentria_it.messaging.gateway.push.PushRequestReceiver;
import com.excentria_it.messaging.gateway.push.PushTestConfiguration;
import com.excentria_it.messaging.gateway.sms.SMSRequestReceiver;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.rabbitmq.client.Channel;

@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "localtest")
@Import(PushTestConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "eureka.client.enabled=false" })
public class MessagingGatewayApplicationTests {

	@Mock
	private SMSRequestReceiver properties;

	@Mock
	private SMSRequestReceiver smsRequestReceiver;

	@Mock
	private EmailRequestReceiver emailRequestReceiver;

	@Mock
	private PushRequestReceiver pushRequestReceiver;

	@InjectMocks
	@Spy
	private MessagingGatewayConfiguration conf;

	@Test
	void contextLoads() {

	}

	@Test
	void testSMSMessageListener() throws Exception {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = Mockito.mock(Jackson2JsonMessageConverter.class);
		Message message = Mockito.mock(Message.class);
		SMSMessage smsMessage = Mockito.mock(SMSMessage.class);
		MessageProperties properties = Mockito.mock(MessageProperties.class);
		Channel channel = Mockito.mock(Channel.class);
		doReturn(jackson2JsonMessageConverter).when(conf).jackson2JsonMessageConverter();
		doReturn(smsMessage).when(jackson2JsonMessageConverter).fromMessage(message);
		doReturn(true).when(smsRequestReceiver).receiveSMSRequest(smsMessage);
		doReturn(properties).when(message).getMessageProperties();
		doReturn(-1L).when(properties).getDeliveryTag();

		ChannelAwareMessageListener smsMessageListener = conf.getSMSMessageMessageListener(smsRequestReceiver);

		smsMessageListener.onMessage(message, channel);

		then(smsRequestReceiver).should(times(1)).receiveSMSRequest(smsMessage);
		then(channel).should(times(1)).basicAck(-1, false);
	}

	@Test
	void testEmailMessageListener() throws Exception {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = Mockito.mock(Jackson2JsonMessageConverter.class);
		Message message = Mockito.mock(Message.class);
		EmailMessage emailMessage = Mockito.mock(EmailMessage.class);
		MessageProperties properties = Mockito.mock(MessageProperties.class);
		Channel channel = Mockito.mock(Channel.class);
		doReturn(jackson2JsonMessageConverter).when(conf).jackson2JsonMessageConverter();
		doReturn(emailMessage).when(jackson2JsonMessageConverter).fromMessage(message);
		doReturn(true).when(emailRequestReceiver).receiveEmailRequest(emailMessage);
		doReturn(properties).when(message).getMessageProperties();
		doReturn(-1L).when(properties).getDeliveryTag();

		ChannelAwareMessageListener emailMessageListener = conf.getEmailMessageMessageListener(emailRequestReceiver);

		emailMessageListener.onMessage(message, channel);

		then(emailRequestReceiver).should(times(1)).receiveEmailRequest(emailMessage);
		then(channel).should(times(1)).basicAck(-1, false);
	}

	@Test
	void testPushMessageListener() throws Exception {
		Jackson2JsonMessageConverter jackson2JsonMessageConverter = Mockito.mock(Jackson2JsonMessageConverter.class);
		Message message = Mockito.mock(Message.class);
		PushMessage pushMessage = Mockito.mock(PushMessage.class);
		MessageProperties properties = Mockito.mock(MessageProperties.class);
		Channel channel = Mockito.mock(Channel.class);
		doReturn(jackson2JsonMessageConverter).when(conf).jackson2JsonMessageConverter();
		doReturn(pushMessage).when(jackson2JsonMessageConverter).fromMessage(message);
		doReturn(true).when(pushRequestReceiver).receivePushRequest(pushMessage);
		doReturn(properties).when(message).getMessageProperties();
		doReturn(-1L).when(properties).getDeliveryTag();

		ChannelAwareMessageListener pushMessageListener = conf.getPushMessageMessageListener(pushRequestReceiver);

		pushMessageListener.onMessage(message, channel);

		then(pushRequestReceiver).should(times(1)).receivePushRequest(pushMessage);
		then(channel).should(times(1)).basicAck(-1, false);
	}
}
