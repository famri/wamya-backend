package com.excentria_it.wamya.application.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.LoginOrPasswordNotFoundException;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.RegexPattern;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthenticateUserService implements AuthenticateUserUseCase {

	final private LoadUserAccountPort loadUserAccountPort;

	final private OAuthUserAccountPort oAuthUserAccountPort;

	final private PasswordEncoder passwordEncoder;

	@Override
	public JwtOAuth2AccessToken loginUser(LoginUserCommand command) {
		Optional<UserAccount> userAccount = null;

		String encodedPassword = passwordEncoder.encode(command.getPassword());

		if (isEmailUsername(command.getUsername())) {

			userAccount = loadUserAccountPort.loadUserAccountByEmailAndPassword(command.getUsername(), encodedPassword);

		} else if (isMobileNumberUsername(command.getUsername())) {

			userAccount = loadUserAccountPort.loadUserAccountByIccAndMobileNumberAndPassword(
					command.getUsername().split("_")[0], command.getUsername().split("_")[1], encodedPassword);
		}
		if (userAccount != null && userAccount.isPresent()) {
			JwtOAuth2AccessToken jwtToken = oAuthUserAccountPort.fetchJwtTokenForUser(command.getUsername(),
					command.getPassword());
			return jwtToken;
		}
		throw new LoginOrPasswordNotFoundException("Login or password not found.");
	}

	protected boolean isEmailUsername(String username) {
		Matcher emailMatcher = Pattern.compile(RegexPattern.EMAIL_PATTERN).matcher(username);
		return emailMatcher.matches();
	}

	protected boolean isMobileNumberUsername(String username) {
		Matcher mobileNumberMatcher = Pattern.compile(RegexPattern.MOBILE_USERNAME_PATTERN).matcher(username);
		return mobileNumberMatcher.matches();
	}

}
