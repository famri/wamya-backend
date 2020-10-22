package com.excentria_it.messaging.gateway.email;

import static com.excentria_it.wamya.test.data.common.EmailMessageTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class MimeMessageManagerImplTests {
	@Spy
	@InjectMocks
	private MimeMessageManagerImpl mimeMessageManager;

	@Mock
	private MimeMessage mimeMessage;

	@Mock
	private MimeMessageHelper mimeMessageHelper;

	@Mock
	private ResourceLoader resourceLoader;

	@Test
	void testAddAttachements() {

		try {
			mimeMessageManager.addAttachements(mimeMessageHelper,
					Map.of("File_1", getClass().getClassLoader().getResource("dummy_email_attachement.txt").getFile(),
							"File_2",
							getClass().getClassLoader().getResource("dummy_email_attachement_2.txt").getFile()));
			then(mimeMessageHelper).should(times(2)).addInline(any(String.class), any(File.class));

		} catch (FileNotFoundException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testAddEmptyAttachements() {
		try {
			mimeMessageManager.addAttachements(mimeMessageHelper, Map.of());
			then(mimeMessageHelper).should(never()).addInline(any(String.class), any(File.class));

		} catch (FileNotFoundException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	void testAddInexistingAttachements() {

		assertThrows(FileNotFoundException.class, () -> mimeMessageManager.addAttachements(mimeMessageHelper,
				Map.of("File1", "this_file_does_not_exist.txt")));

	}

	@Test
	void testAddInexistingTemplateResources() {

		assertThrows(FileNotFoundException.class, () -> mimeMessageManager.addTemplateResources(mimeMessageHelper,
				Map.of("File1", "this_file_does_not_exist.txt")));

	}

	@Test
	void testAddNullTemplateResources() throws IOException, MessagingException {

		mimeMessageManager.addTemplateResources(mimeMessageHelper, null);
		then(mimeMessageHelper).should(never()).addInline(any(String.class), any(File.class));

	}

	@Test
	void testEmptyTemplateResources() throws MessagingException, IOException {

		mimeMessageManager.addTemplateResources(mimeMessageHelper, Map.of());
		then(mimeMessageHelper).should(never()).addInline(any(String.class), any(File.class));

	}

	@Test
	void testPrepareMimeMessage() {

		EmailMessage emailMessage = defaultEmailMessageBuilder().attachements(
				Map.of("File_1", getClass().getClassLoader().getResource("dummy_email_attachement.txt").getFile()))
				.build();

		try {

			mimeMessageManager.prepareMimeMessage(mimeMessageHelper, emailMessage.getFrom(), emailMessage.getTo(),
					emailMessage.getSubject(), TestConstants.DEFAULT_TEMPLATE_BODY,
					emailMessage.getTemplate().getTemplateResources(), emailMessage.getAttachements());

			then(mimeMessageManager).should(times(1)).addTemplateResources(any(MimeMessageHelper.class),
					eq(emailMessage.getTemplate().getTemplateResources()));
			then(mimeMessageManager).should(times(1)).addAttachements(any(MimeMessageHelper.class),
					eq(emailMessage.getAttachements()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
