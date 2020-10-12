package com.excentria_it.messaging.gateway.common;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TemplateManagerImplTests {

	private static final String TEST_TEMPLATES_DIR = "test/templates";
	@InjectMocks
	private TemplateManagerImpl templateManager;

	// General tests
	@Test
	void testConfigure() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		assertEquals(TEST_TEMPLATES_DIR, templateManager.getTemplatesBaseDir());
		assertEquals(true, templateManager.isConfigured());
		assertEquals(false, templateManager.isLoaded());
	}

	@Test
	void testRenderTemplateBeforeLoad() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		assertThrows(IllegalStateException.class, () -> templateManager.renderTemplate());
	}

	@Test
	void testRenderTemplateBeforeConfigure() throws FileNotFoundException {

		assertThrows(IllegalStateException.class, () -> templateManager.renderTemplate());
	}

	// SMS Template tests
	@Test
	void testLoadTemplateOfSMS() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.SMS, "fr");
		assertTrue(result);
	}

	@Test
	void testLoadTemplateOfSMSWithEmptyParams() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", Map.of(), TemplateType.SMS, "fr");
		assertTrue(result);
	}

	@Test
	void testLoadTemplateOfSMSWithNullParams() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", null, TemplateType.SMS, "fr");
		assertTrue(result);
	}

	@Test
	void testRenderTemplateOfSMS() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean loadingResult = templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.SMS,
				"fr");
		String renderingResult = templateManager.renderTemplate();
		assertTrue(loadingResult);
		assertEquals(renderingResult, "This is a dummy SMS template:TEST");
	}

	@Test
	void testLoadInexistingTemplateOfSMS() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);

		assertThrows(FileNotFoundException.class, () -> templateManager.loadTemplate("I_do_not_exist",
				Map.of("content", "TEST"), TemplateType.SMS, "fr"));

	}

	@Test
	void testLoadTemplateOfSMSBeforeConfigure() throws FileNotFoundException {

		assertThrows(IllegalStateException.class,
				() -> templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.SMS, "fr"));
	}

	// Email Template tests

	@Test
	void testLoadTemplateOfEmail() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.EMAIL, "fr");
		assertTrue(result);
	}

	@Test
	void testRenderTemplateOfEmail() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean loadingResult = templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.EMAIL,
				"fr");
		String renderingResult = templateManager.renderTemplate();
		assertTrue(loadingResult);
		assertEquals(renderingResult, "This is a dummy email template:TEST");
	}

	@Test
	void testLoadInexistingTemplateOfEmail() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);

		assertThrows(FileNotFoundException.class, () -> templateManager.loadTemplate("I_do_not_exist",
				Map.of("content", "TEST"), TemplateType.EMAIL, "fr"));
	}

	@Test
	void testLoadTemplateOfEmailWithEmptyParams() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", Map.of(), TemplateType.EMAIL, "fr");
		assertTrue(result);
	}

	@Test
	void testLoadTemplateOfEmailWithNullParams() throws FileNotFoundException {
		templateManager.configure(TEST_TEMPLATES_DIR);
		Boolean result = templateManager.loadTemplate("dummy", null, TemplateType.EMAIL, "fr");
		assertTrue(result);
	}

	@Test
	void testLoadTemplateOfEmailBeforeConfigure() throws FileNotFoundException {

		assertThrows(IllegalStateException.class,
				() -> templateManager.loadTemplate("dummy", Map.of("content", "TEST"), TemplateType.EMAIL, "fr"));
	}

}
