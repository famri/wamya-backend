package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.LoadTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.port.out.UpdateTransporterRatingRequestRecordPort;
import com.excentria_it.wamya.application.props.SendTransporterRatingRequestProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.domain.EmailSubject;
import com.excentria_it.wamya.domain.TransporterRatingRequestRecordOutput;
import com.excentria_it.wamya.test.data.common.TransporterRatingRequestTestData;

@ExtendWith(MockitoExtension.class)
public class SendTransporterRatingRequestTaskTests {
	@Mock
	private LoadTransporterRatingRequestRecordPort loadTransporterRatingRequestRecordPort;
	@Mock
	private UpdateTransporterRatingRequestRecordPort updateTransporterRatingRequestRecordPort;
	@Mock
	private AsynchronousMessagingPort asynchronousMessagingPort;
	@Mock
	private ServerUrlProperties serverUrlProperties;
	@Mock
	private DateTimeHelper dateTimeHelper;
	@Mock
	private MessageSource messageSource;
	@Mock
	private SendTransporterRatingRequestProperties sendTransporterRatingRequestProperties;

	@InjectMocks
	private SendTransporterRatingRequestTask sendTransporterRatingRequestTask;

	@Test
	void testSendTransporterRatingRequest() {
		// given
		TransporterRatingRequestRecordOutput trrro = TransporterRatingRequestTestData
				.defaultTransporterRatingRequestRecordOutput();
		Set<TransporterRatingRequestRecordOutput> records = Set.of(trrro);
		given(sendTransporterRatingRequestProperties.getRevivals()).willReturn(3);
		given(loadTransporterRatingRequestRecordPort.loadUnfulfilledRecords(3)).willReturn(records);
		given(dateTimeHelper.systemToUserLocalDateTime(any(Instant.class), any(ZoneId.class)))
				.willReturn(Instant.now().atZone(ZoneId.of("Africa/Tunis")).toLocalDateTime());

		String receiverLocale = trrro.getClient().getLocale();
		String[] receiverLanguageAndCountry = receiverLocale.split("_");
		Locale clientLocale = new Locale(receiverLanguageAndCountry[0], receiverLanguageAndCountry[1]);
		given(messageSource.getMessage(EmailSubject.RATING_REQUEST, null, clientLocale)).willReturn("Some subject");
		
		given(serverUrlProperties.getProtocol()).willReturn("https");
		given(serverUrlProperties.getHost()).willReturn("localhost");
		given(serverUrlProperties.getPort()).willReturn("80");

		ArgumentCaptor<EmailMessage> captor = ArgumentCaptor.forClass(EmailMessage.class);

		// when
		sendTransporterRatingRequestTask.sendTransporterRatingRequest();
		// then

		then(asynchronousMessagingPort).should(times(1)).sendEmailMessage(captor.capture());
		assertEquals(trrro.getClient().getEmail(), captor.getValue().getTo());
		assertEquals("Some subject", captor.getValue().getSubject());
		assertEquals(EmailTemplate.RATING_REQUEST, captor.getValue().getTemplate());
		assertEquals(receiverLocale, captor.getValue().getLanguage());

		then(updateTransporterRatingRequestRecordPort).should(times(1)).incrementRevivals(Set.of(trrro.getId()));
	}
}
