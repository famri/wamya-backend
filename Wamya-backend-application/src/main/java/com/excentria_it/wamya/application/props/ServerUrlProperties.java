package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "front.url")
@Validated
@Data
public class ServerUrlProperties {
	@NotEmpty
	private String protocol;
	@NotEmpty
	private String host;
	@NotEmpty
	private String port;

}
