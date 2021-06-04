package com.excentria_it.wamya.adapter.web;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.excentria_it.wamya.common.annotation.ValidationMessageSource;
import com.excentria_it.wamya.common.annotation.ViewMessageSource;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	// I18N validation messages
	@Bean(name = "validationMessageSource")
	@ValidationMessageSource
	public MessageSource validationMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/validation/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;

	}

	@Bean(name = "viewMessageSource")
	@ViewMessageSource
	public MessageSource viewMessageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages/view/messages");
		messageSource.setDefaultEncoding("UTF-8");

		return messageSource;

	}

	@Bean
	public LocalValidatorFactoryBean getValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(validationMessageSource());
		return localValidatorFactoryBean;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.US);		
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.stream().filter(converter -> converter instanceof MappingJackson2HttpMessageConverter).findFirst()
				.ifPresent(converter -> ((MappingJackson2HttpMessageConverter) converter)
						.setDefaultCharset(StandardCharsets.UTF_8));
	}
}
