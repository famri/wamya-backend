package com.excentria_it.wamya.application;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.excentria_it.wamya.application.props.CodeGeneratorProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;

@Configuration
@EnableConfigurationProperties(value = { CodeGeneratorProperties.class, ServerUrlProperties.class })
public class ApplicationConfiguration {

}
