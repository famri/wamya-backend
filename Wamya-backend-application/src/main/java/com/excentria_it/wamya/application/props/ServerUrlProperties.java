package com.excentria_it.wamya.application.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "front.url")
@Data
public class ServerUrlProperties {
	private String protocol;
	private String host;
	private String port;

}
