package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenIdAuthResponse {
	@JsonProperty(value = "access_token")
	private String accessToken;

	@JsonProperty(value = "expires_in")
	private Long expiresIn;

	@JsonProperty(value = "refresh_expires_in")
	private Long refreshExpiresIn;

	@JsonProperty(value = "refresh_token")
	private String refreshToken;

	@JsonProperty(value = "token_type")
	private String tokenType;

	@JsonProperty(value = "id_token")
	private String idToken;

	@JsonProperty(value = "scope")
	private String scope;

}
