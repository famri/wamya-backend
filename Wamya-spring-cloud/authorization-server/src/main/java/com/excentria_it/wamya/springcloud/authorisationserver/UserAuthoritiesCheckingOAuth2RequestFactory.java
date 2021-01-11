package com.excentria_it.wamya.springcloud.authorisationserver;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

public class UserAuthoritiesCheckingOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

	private static final String GRANT_TYPE_PARAM_NAME = "grant_type";
	private static final String PASSWORD_GRANT_TYPE_VALUE = "password";
	private static final String USERNAME_PARAM_NAME = "username";
	private UserDetailsService userDetailsService;

	public UserAuthoritiesCheckingOAuth2RequestFactory(ClientDetailsService clientDetailsService,
			UserDetailsService userDetailsService) {
		super(clientDetailsService);
		// super.setCheckUserScopes(true);
		this.userDetailsService = userDetailsService;

	}

	@Override
	public TokenRequest createTokenRequest(Map<String, String> requestParameters, ClientDetails authenticatedClient) {

		if (PASSWORD_GRANT_TYPE_VALUE.equals(requestParameters.get(GRANT_TYPE_PARAM_NAME))) {
			Set<String> requestedScopes = OAuth2Utils.parseParameterList(requestParameters.get(OAuth2Utils.SCOPE));

			Collection<? extends GrantedAuthority> userGrantedAuthorities = this.userDetailsService
					.loadUserByUsername(requestParameters.get(USERNAME_PARAM_NAME)).getAuthorities();

			Set<String> userGrantedAuthoritiesStr = userGrantedAuthorities.stream().map(a -> a.getAuthority())
					.collect(Collectors.toSet());
			
			Set<String> filteredScopes = new LinkedHashSet<String>();

			for (String scope : requestedScopes) {
				if (userGrantedAuthoritiesStr.contains(scope) || userGrantedAuthoritiesStr.contains(scope.toUpperCase())
						|| userGrantedAuthoritiesStr.contains("ROLE_" + scope.toUpperCase())) {
					filteredScopes.add(scope);
				}
			}
			requestParameters.put(OAuth2Utils.SCOPE, StringUtils.join(filteredScopes, " "));

		}

		return super.createTokenRequest(requestParameters, authenticatedClient);
	}

}
