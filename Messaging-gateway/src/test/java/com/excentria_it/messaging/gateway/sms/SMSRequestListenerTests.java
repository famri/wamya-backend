package com.excentria_it.messaging.gateway.sms;

import static com.excentria_it.wamya.test.data.common.SMSMessageTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.SMSMessage;

@ExtendWith(MockitoExtension.class)
public class SMSRequestListenerTests {

	@Mock
	private SMSGatewayProperties smsGatewayProperties;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private TemplateManager templateManager;

	@Spy
	@InjectMocks
	private SMSRequestReceiver smsRequestReceiver;

	@Test
	void givenValidSMSMessage_ThenShouldReturnTrue() {

		SMSMessage message = defaultSMSMessageBuilder().build();

		givenTemplateManagerLoadsTemplateCorrectly(message);

		givenTemplateManagerRendersTemplateCorrectly();

		givenSmsGatewayPropertiesSet();

		givenRestTemplateWillMakeTheCall(message);

		Boolean result = smsRequestReceiver.receiveSMSRequest(message);

		assertThat(result);

	}

	@Test
	void givenInexistingSMSTemplateName_ThenShouldReturnFalse() {
		SMSMessage message = defaultSMSMessageBuilder().build();
		givenTemplateManagerLoadTemplateReturnsFalse(message);
		Boolean result = smsRequestReceiver.receiveSMSRequest(message);
		assertThat(!result);
	}

	@Test
	void givenUnsupportedEncoding_ThenShouldReturnFalse() throws UnsupportedEncodingException {
		SMSMessage message = defaultSMSMessageBuilder().build();

		givenTemplateManagerLoadsTemplateCorrectly(message);

		String templateBody = givenTemplateManagerRendersTemplateCorrectly();

		givenToURIEncodedThrowsUnsupportedEncodingException(templateBody);

		Boolean result = smsRequestReceiver.receiveSMSRequest(message);

		assertThat(!result);
	}

	@Test
	void givenRestTemplateThrowsException_ThenShouldReturnFalse() {

		SMSMessage message = defaultSMSMessageBuilder().build();

		givenTemplateManagerLoadsTemplateCorrectly(message);

		String templateBody = givenTemplateManagerRendersTemplateCorrectly();

		doThrow(RestClientException.class).when(restTemplate).getForEntity(any(String.class), any(Class.class),
				any(String.class), any(String.class), any(String.class), any(String.class), any(String.class),
				any(String.class));

		Boolean result = smsRequestReceiver.receiveSMSRequest(message);

		assertThat(!result);
	}

	private void givenToURIEncodedThrowsUnsupportedEncodingException(String content) throws UnsupportedEncodingException {

		doThrow(UnsupportedEncodingException.class).when(smsRequestReceiver).toURIEncoded(content,
				java.nio.charset.StandardCharsets.UTF_8);

	}

	private void givenTemplateManagerLoadTemplateReturnsFalse(SMSMessage message) {

		given(templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.SMS,
				message.getLanguage())).willReturn(false);

	}

	private void givenRestTemplateWillMakeTheCall(SMSMessage message) {
		ResponseEntity<String> myEntity = new ResponseEntity<String>(HttpStatus.ACCEPTED);
		given(restTemplate.getForEntity(any(String.class), any(Class.class), any(String.class), any(String.class),
				any(String.class), any(String.class), any(String.class), any(String.class))).willReturn(myEntity);
	}

	private String givenTemplateManagerRendersTemplateCorrectly() {
		String templateBody = "Test SMS template.";
		given(templateManager.renderTemplate()).willReturn(templateBody);
		return templateBody;

	}

	private void givenTemplateManagerLoadsTemplateCorrectly(SMSMessage message) {

		given(templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.SMS,
				message.getLanguage())).willReturn(true);

	}

	private void givenSmsGatewayPropertiesSet() {
		given(smsGatewayProperties.getHost()).willReturn("HOST");
		given(smsGatewayProperties.getPort()).willReturn("POST");
		given(smsGatewayProperties.getUsername()).willReturn("USERNAME");
		given(smsGatewayProperties.getPassword()).willReturn("PASSWORD");

	}

}
