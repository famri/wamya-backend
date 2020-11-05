package com.excentria_it.wamya.adapter.b2b.rest.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app")
@Validated
@Data
public class AuthServerProperties {
	@NotEmpty
	@NotNull
	private String authServer;
}
