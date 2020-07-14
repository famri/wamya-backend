package com.codisiac.wamya;

import org.springframework.boot.context.properties.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.context.support.*;
import org.springframework.validation.beanvalidation.*;

import com.codisiac.wamya.common.*;
import com.codisiac.wamya.common.impl.*;

@Configuration
public class WamyaConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "code-generator")
	public CodeGenerator codeGenerator() {
		return new CodeGeneratorImpl();
	}

	// I18N validation messages
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:validation-messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;

	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		return localValidatorFactoryBean;
	}

}
