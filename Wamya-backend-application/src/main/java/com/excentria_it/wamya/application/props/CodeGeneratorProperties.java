package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "code.generator")
@Validated
@Data
public class CodeGeneratorProperties {
	@NotNull
	private Integer length;
}
