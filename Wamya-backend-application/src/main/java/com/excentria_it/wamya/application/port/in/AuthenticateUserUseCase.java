package com.excentria_it.wamya.application.port.in;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.RegexPattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AuthenticateUserUseCase {

	JwtOAuth2AccessToken loginUser(LoginUserCommand command);

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	class LoginUserCommand {
		@NotNull
		@Pattern(regexp = RegexPattern.USERNAME_PATTERN, message = "{com.excentria_it.wamya.domain.login.username.or.password.message}")
		private String username;

		@NotNull
		@Pattern(regexp = RegexPattern.USER_PASSWORD_PATTERN, message = "{com.excentria_it.wamya.domain.login.username.or.password.message}")
		private String password;
	}
}
