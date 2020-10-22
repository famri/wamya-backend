package com.excentria_it.messaging.gateway.common;

import java.io.File;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Getter
@Slf4j
public class TemplateManagerImpl implements TemplateManager {

	private ST stTemplate;

	private String templatesBaseDir;

	private boolean loaded = false;

	private boolean configured = false;

	public void configure(String templatesBaseDir) {
		this.templatesBaseDir = templatesBaseDir;
		this.loaded = false;
		this.configured = true;
	}

	public boolean loadTemplate(String templateName, Map<String, String> templateParams, TemplateType templateType,
			String language) {
		if (!configured)
			throw new IllegalStateException(String
					.format("TemplateManager should be configured via configure() before calling loadTemplate()"));

		String localeTemplateName = new StringBuilder(templateName).append("_").append(language).toString()
				.toLowerCase();

		String templateSubDir = new StringBuilder(templatesBaseDir).append(File.separator)
				.append(templateType.name().toLowerCase()).toString();

		STGroup group = new STRawGroupDir(templateSubDir, "UTF-8", '$', '$');

		stTemplate = group.getInstanceOf(localeTemplateName);

		if (stTemplate == null) {
			log.error("Template not found:" + templateSubDir + File.separator + localeTemplateName);
			return false;
		}
		if (templateParams != null) {
			templateParams.forEach((k, v) -> {

				stTemplate.add(k, v);
			});
		}

		this.loaded = true;
		return true;

	}

	public String renderTemplate() {
		if (!configured) {

			throw new IllegalStateException(String
					.format("TemplateManager should be configured via configure() before calling loadTemplate()"));

		}
		if (!loaded)

			throw new IllegalStateException(
					String.format("Template should be loaded via loadTemplate() before calling renderTemplate()"));

		return this.stTemplate.render();
	}
}
