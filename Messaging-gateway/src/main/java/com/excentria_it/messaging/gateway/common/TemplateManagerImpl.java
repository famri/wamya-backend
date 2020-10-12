package com.excentria_it.messaging.gateway.common;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STRawGroupDir;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TemplateManagerImpl implements TemplateManager {

	@Getter
	private String templatesBaseDir;

	private ST stTemplate;

	@Getter
	private boolean loaded = false;
	@Getter
	private boolean configured = false;

	public void configure(String templatesBaseDir) {
		this.templatesBaseDir = templatesBaseDir;
		this.loaded = false;
		this.configured = true;
	}

	public boolean loadTemplate(String templateName, Map<String, String> templateParams, TemplateType templateType,
			String language) throws FileNotFoundException {
		if (!configured)
			throw new IllegalStateException(String
					.format("TemplateManager should be configured via configure() before calling loadTemplate()"));
		String localeTemplateName = new StringBuilder(templateName).append("_").append(language).toString()
				.toLowerCase();

		URL templateURL = getClass().getClassLoader()
				.getResource(templatesBaseDir + "/" + templateType.name() + "/" + localeTemplateName + ".st");

		if (templateURL == null) {
			throw new FileNotFoundException(String.format("Template %s does not exist: ", localeTemplateName));
		}
		String templateSubDir = new StringBuilder(templatesBaseDir).append("/")
				.append(templateType.name().toLowerCase()).toString();
		STGroup group = new STRawGroupDir(templateSubDir, "UTF-8", '$', '$');

		stTemplate = group.getInstanceOf(localeTemplateName);

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
