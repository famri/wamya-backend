package com.excentria_it.messaging.gateway.common;

import java.io.FileNotFoundException;
import java.util.Map;

public interface TemplateManager {
	void configure(String templatesBaseDir);

	boolean loadTemplate(String templateName, Map<String, String> templateParams, TemplateType templateType,
			String language) throws FileNotFoundException;

	String renderTemplate();

}
