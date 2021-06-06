package com.excentria_it.wamya.application.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import com.excentria_it.wamya.domain.Validity;

import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = "app.password.reset")
@Validated
@NoArgsConstructor
@Data
public class PasswordResetProperties {
	@NotNull
	private Validity requestValidity;
	@NotEmpty
	private String purgeCronExpression;

}
