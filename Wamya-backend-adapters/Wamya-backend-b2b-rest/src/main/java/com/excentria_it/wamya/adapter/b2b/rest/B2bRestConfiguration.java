package com.excentria_it.wamya.adapter.b2b.rest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.adapter.b2b.rest.props.GoogleApiProperties;
import com.excentria_it.wamya.common.exception.RestTemplateResponseErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(value = { AuthServerProperties.class, GoogleApiProperties.class })
@ComponentScan
public class B2bRestConfiguration {

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder, ObjectMapper mapper) {
		RestTemplate restTemplate = restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler(mapper))
				.requestFactory(HttpComponentsClientHttpRequestFactory.class)

				.build();

		return restTemplate;
	}

}
