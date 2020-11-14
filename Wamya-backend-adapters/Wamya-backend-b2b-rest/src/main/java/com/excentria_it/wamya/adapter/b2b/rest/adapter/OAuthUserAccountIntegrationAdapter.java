package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.OAuthUserAccount;

@WebAdapter
public class OAuthUserAccountIntegrationAdapter implements OAuthUserAccountPort {

	private AuthServerProperties authServerProperties;

	private RestTemplate restTemplate;

	public OAuthUserAccountIntegrationAdapter(AuthServerProperties authServerProperties, RestTemplate restTemplate) {
		this.authServerProperties = authServerProperties;
		this.restTemplate = restTemplate;

	}

	@Override
	public Long createOAuthUserAccount(OAuthUserAccount userAccount) {

		OAuthUserAccount user = restTemplate.postForObject(authServerProperties.getCreateUserUri(), userAccount,
				OAuthUserAccount.class);
		return user.getOauthId();

	}

	@Override
	public JwtOAuth2AccessToken fetchJwtTokenForUser(String username, String password) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(authServerProperties.getClientId(), authServerProperties.getClientSecret());
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
		formParams.add("username", username);
		formParams.add("password", password);
		formParams.add("grant_type", "password");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

		JwtOAuth2AccessToken jwtToken = restTemplate
				.exchange(authServerProperties.getTokenUri(), HttpMethod.POST, request, JwtOAuth2AccessToken.class)
				.getBody();

		return jwtToken;

	}

}
