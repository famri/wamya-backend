package com.excentria_it.messaging.gateway.email;

import static com.excentria_it.wamya.test.data.common.EmailMessageTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.excentria_it.messaging.gateway.common.TemplateManager;
import com.excentria_it.messaging.gateway.common.TemplateType;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class EmailServiceImplTests {

	@Mock
	private JavaMailSender emailSender;

	@Mock
	private TemplateManager templateManager;

	@Mock
	private MimeMessageManager mimeMessageManager;

	@Mock
	private MimeMessageHelper mimeMessageHelper;

	@Mock
	private MimeMessage mimeMessage;

	@InjectMocks
	@Spy
	private EmailServiceImpl emailService;

	@Test
	void testSendEmailWithHTMTemplate() throws MessagingException {

		givenTemplateManagerLoadsTemplateCorrectly();
		String templateBody = givenTemplateManagerRendersTemplateCorrectly();
		MimeMessage message = givenEmailSenderCreatesMimeMessage();
		MimeMessageHelper mimeMessageHelper = givenEmailServiceImplCreatesMimeMessageHelper(message);

		Map<String, String> templateParams = TestConstants.DEFAULT_TEMPLATE_PARAMS;
		Map<String, String> templateAttachements = TestConstants.DEFAULT_TEMPLATE_ATTACHEMENTS;

		emailService.sendEmailWithHTMTemplate(TestConstants.DEFAULT_FROM_EMAIL, TestConstants.DEFAULT_EMAIL,
				TestConstants.DEFAULT_EMAIL_SUBJECT, TestConstants.DEFAULT_EMAIL_TEMPLATE,
				TestConstants.DEFAULT_TEMPLATE_LANGUAGE, templateParams, templateAttachements);

		InOrder inOrder = inOrder(templateManager, emailSender, mimeMessageManager);
		try {
			inOrder.verify(templateManager).loadTemplate(TestConstants.DEFAULT_EMAIL_TEMPLATE.name(), templateParams,
					TemplateType.EMAIL, TestConstants.DEFAULT_TEMPLATE_LANGUAGE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inOrder.verify(emailSender).createMimeMessage();
		inOrder.verify(templateManager).renderTemplate();
		try {
			inOrder.verify(mimeMessageManager).prepareMimeMessage(mimeMessageHelper, TestConstants.DEFAULT_FROM_EMAIL,
					TestConstants.DEFAULT_EMAIL, TestConstants.DEFAULT_EMAIL_SUBJECT, templateBody,
					TestConstants.DEFAULT_EMAIL_TEMPLATE.getTemplateResources(), templateAttachements);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inOrder.verify(emailSender).send(message);

	}

	@Test
	void givenInexistingEmailTemplateName_ThenShouldReturnFalse() {
		EmailMessage message = defaultEmailMessageBuilder().build();
		givenTemplateManagerLoadTemplateThrowsFileNotFoundExcepion(message);
		Boolean result = emailService.sendEmailWithHTMTemplate(TestConstants.DEFAULT_FROM_EMAIL,
				TestConstants.DEFAULT_EMAIL, TestConstants.DEFAULT_EMAIL_SUBJECT, TestConstants.DEFAULT_EMAIL_TEMPLATE,
				TestConstants.DEFAULT_TEMPLATE_LANGUAGE, TestConstants.DEFAULT_TEMPLATE_PARAMS,
				TestConstants.DEFAULT_TEMPLATE_ATTACHEMENTS);
		assertThat(!result);
	}

	@Test
	void givenMimeMessageManagerThrowsMessagingException_ThenShouldReturnFalse() throws MessagingException {

		String templateBody = givenTemplateManagerRendersTemplateCorrectly();
		MimeMessage message = givenEmailSenderCreatesMimeMessage();
		MimeMessageHelper mimeMessageHelper = givenEmailServiceImplCreatesMimeMessageHelper(message);

		givenMimeMessageManagerThrowsMessagingException(templateBody, mimeMessageHelper);

		Boolean result = emailService.sendEmailWithHTMTemplate(TestConstants.DEFAULT_FROM_EMAIL,
				TestConstants.DEFAULT_EMAIL, TestConstants.DEFAULT_EMAIL_SUBJECT, TestConstants.DEFAULT_EMAIL_TEMPLATE,
				TestConstants.DEFAULT_TEMPLATE_LANGUAGE, TestConstants.DEFAULT_TEMPLATE_PARAMS,
				TestConstants.DEFAULT_TEMPLATE_ATTACHEMENTS);
		assertThat(!result);
	}

	@Test
	void testCreateMimeMessageHelper() throws MessagingException {

		MimeMessageHelper helper = emailService.createMimeMessageHelper(mimeMessage);
		assertEquals(helper.getMimeMessage(), mimeMessage);
	}

	private MimeMessageHelper givenEmailServiceImplCreatesMimeMessageHelper(MimeMessage message) {
		try {
			doReturn(mimeMessageHelper).when(emailService).createMimeMessageHelper(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mimeMessageHelper;
	}

	private void givenMimeMessageManagerThrowsMessagingException(String templateBody, MimeMessageHelper helper) {
		try {
			doThrow(MessagingException.class).when(mimeMessageManager).prepareMimeMessage(eq(helper),
					eq(TestConstants.DEFAULT_FROM_EMAIL), eq(TestConstants.DEFAULT_EMAIL),
					eq(TestConstants.DEFAULT_EMAIL_SUBJECT), eq(templateBody),
					eq(TestConstants.DEFAULT_EMAIL_TEMPLATE.getTemplateResources()),
					eq(TestConstants.DEFAULT_TEMPLATE_ATTACHEMENTS));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void givenTemplateManagerLoadTemplateThrowsFileNotFoundExcepion(EmailMessage message) {

		try {
			given(templateManager.loadTemplate(message.getTemplate().name(), message.getParams(), TemplateType.EMAIL,
					message.getLocale().getLanguage())).willThrow(FileNotFoundException.class);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private MimeMessage givenEmailSenderCreatesMimeMessage() {

		MimeMessage message = Mockito.mock(MimeMessage.class);
		given(emailSender.createMimeMessage()).willReturn(message);
		return message;

	}

	private String givenTemplateManagerRendersTemplateCorrectly() {
		String templateBody = TestConstants.DEFAULT_TEMPLATE_BODY;
		given(templateManager.renderTemplate()).willReturn(templateBody);
		return templateBody;
	}

	private void givenTemplateManagerLoadsTemplateCorrectly() {
		try {
			given(templateManager.loadTemplate(TestConstants.DEFAULT_EMAIL_TEMPLATE.name(),
					TestConstants.DEFAULT_TEMPLATE_PARAMS, TemplateType.EMAIL, TestConstants.DEFAULT_TEMPLATE_LANGUAGE))
							.willReturn(Boolean.TRUE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
