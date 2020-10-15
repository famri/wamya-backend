package com.excentria_it.wamya.adapter.web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.excentria_it.wamya.common.annotation.ValidationMessageSource;

@SpringBootApplication
public class TestWamyaApplication {
	// I18N validation messages
	@Bean
	@ValidationMessageSource
	public MessageSource validationMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/validation/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;

	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(validationMessageSource());
		return localValidatorFactoryBean;
	}

}
