package com.excentria_it.wamya.adapter.messaging.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app.fcm-api")
@Validated
@Data
public class FcmProperties {
	@NotEmpty
	@NotNull
	private String apiKey;

}
