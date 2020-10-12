package com.excentria_it.messaging.gateway.sms;

import static com.excentria_it.wamya.test.data.common.SMSMessageTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private SMSRequestListener smsRequestListener;

	@Test
	void givenValidSMSMessage_ThenShouldReturnTrue() {

		SMSMessage message = defaultSMSMessageBuilder().build();

		givenTemplateManagerLoadsTemplateCorrectly(message);

		givenTemplateManagerRendersTemplateCorrectly();

		givenSmsGatewayPropertiesSet();

		givenRestTemplateWillMakeTheCall(message);

		Boolean result = smsRequestListener.receiveSMSRequest(message);

		assertThat(result);

	}

	@Test
	void givenInexistingSMSTemplateName_ThenShouldReturnFalse() {
		SMSMessage message = defaultSMSMessageBuilder().build();
		givenTemplateManagerLoadTemplateThrowsFileNotFoundExcepion(message);
		Boolean result = smsRequestListener.receiveSMSRequest(message);
		assertThat(!result);
	}

	@Test
	void givenUnsupportedEncoding_ThenShouldReturnFalse() {
		SMSMessage message = defaultSMSMessageBuilder().build();

		givenTemplateManagerLoadsTemplateCorrectly(message);

		String templateBody = givenTemplateManagerRendersTemplateCorrectly();

		givenToURIEncodedThrowsUnsupportedEncodingException(templateBody);

		Boolean result = smsRequestListener.receiveSMSRequest(message);

		assertThat(!result);
	}

	private void givenToURIEncodedThrowsUnsupportedEncodingException(String content) {

		try {
			doThrow(UnsupportedEncodingException.class).when(smsRequestListener).toURIEncoded(content,
					java.nio.charset.StandardCharsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void givenTemplateManagerLoadTemplateThrowsFileNotFoundExcepion(SMSMessage message) {

		try {
			given(templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.SMS,
					message.getLocale().getLanguage())).willThrow(FileNotFoundException.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		try {

			given(templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.SMS,
					message.getLocale().getLanguage())).willReturn(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void givenSmsGatewayPropertiesSet() {
		given(smsGatewayProperties.getHost()).willReturn("HOST");
		given(smsGatewayProperties.getPort()).willReturn("POST");
		given(smsGatewayProperties.getUsername()).willReturn("USERNAME");
		given(smsGatewayProperties.getPassword()).willReturn("PASSWORD");

	}

}
