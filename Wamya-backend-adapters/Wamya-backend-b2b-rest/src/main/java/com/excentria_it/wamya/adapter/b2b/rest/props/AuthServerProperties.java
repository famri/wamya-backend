package com.excentria_it.wamya.adapter.b2b.rest.props;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "app.oauth2")
@Validated
@Data
public class AuthServerProperties {
	@NotEmpty
	@NotNull
	private String clientCredentialsRegistrationId;

	@NotEmpty
	@NotNull
	private String passwordRegistrationId;

	@NotEmpty
	@NotNull
	private String createUserUri;

	@NotEmpty
	@NotNull
	private String tokenUri;

	@NotEmpty
	@NotNull
	private String clientId;

	@NotEmpty
	@NotNull
	private String clientSecret;
	
	@NotEmpty
	@NotNull
	private String resetPasswordUri;
	
	@NotEmpty
	@NotNull
	private String updateMobileUri;
	
	@NotEmpty
	@NotNull
	private String updateEmailUri;
	

}
