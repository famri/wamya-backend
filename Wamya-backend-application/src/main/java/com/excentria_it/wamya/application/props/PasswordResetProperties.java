package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.excentria_it.wamya.domain.Validity;

import lombok.Data;

@ConfigurationProperties(prefix = "app.password-reset")
@Validated
@Data
public class PasswordResetProperties {
	@NotEmpty
	private Validity requestValidity;

}
