package com.excentria_it.wamya.application.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "code.generator")
@Data
public class CodeGeneratorProperties {

	private int length;
}
