package com.excentria_it.messaging.gateway.common;

import java.util.Map;

public interface TemplateManager {
	void configure(String templatesBaseDir);

	boolean loadTemplate(String templateName, Map<String, String> templateParams, TemplateType templateType,
			String language);

	String renderTemplate();

}
