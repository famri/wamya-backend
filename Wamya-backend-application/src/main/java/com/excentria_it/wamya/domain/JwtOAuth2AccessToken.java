package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtOAuth2AccessToken {
	@JsonProperty(value = "access_token")
	private String accessToken;
	
	@JsonProperty(value = "token_type")
	private String tokenType;
	
	@JsonProperty(value = "refresh_token")
	private String refreshToken;
	
	@JsonProperty(value = "expires_in")
	private Long expiresIn;
	
	@JsonProperty(value = "scope")
	private String scope;
	
	@JsonProperty(value = "jti")
	private String jti;
}
