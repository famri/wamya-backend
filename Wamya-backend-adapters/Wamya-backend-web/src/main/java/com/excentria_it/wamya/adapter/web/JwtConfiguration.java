package com.excentria_it.wamya.adapter.web;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import net.minidev.json.JSONArray;

//@Configuration
public class JwtConfiguration {

	// @Bean
	public JwtDecoder jwtDecoderByIssuerUri(OAuth2ResourceServerProperties properties) {
		final String jwkSetUri = properties.getJwt().getJwkSetUri();
		final NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

		return jwtDecoder;
	}

	// @Bean
	public Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter() {
		return new SpringAuthorizationServerEmbeddedAuthoritiesConverter();
	}

	// @Bean
	public Converter<Jwt, JwtAuthenticationToken> authenticationConverter(
			Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter) {
		return new JwtToJwtAuthenticationTokenConverterImpl(authoritiesConverter);
	}

	static class JwtToJwtAuthenticationTokenConverterImpl implements Converter<Jwt, JwtAuthenticationToken> {
		private final Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter;

		public JwtToJwtAuthenticationTokenConverterImpl(
				Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter) {
			super();
			this.authoritiesConverter = authoritiesConverter;
		}

		@Override
		public JwtAuthenticationToken convert(Jwt jwt) {
			return new JwtAuthenticationToken(jwt, authoritiesConverter.convert(jwt));
		}

	}

	static class SpringAuthorizationServerEmbeddedAuthoritiesConverter
			implements Converter<Jwt, Collection<GrantedAuthority>> {

		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {

			final JSONArray roles = (JSONArray) jwt.getClaims().get("authorities");
			final JSONArray scopes = (JSONArray) jwt.getClaims().get("scope");

			Set<GrantedAuthority> rolesAuthorities = (roles != null && !roles.isEmpty())
					? roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet())
					: Collections.<GrantedAuthority>emptySet();

			Set<GrantedAuthority> scopesAuthorities = (roles != null && !roles.isEmpty()) ? scopes.stream()
					.map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope)).collect(Collectors.toSet())
					: Collections.<GrantedAuthority>emptySet();

			Set<GrantedAuthority> allAuthorities = new HashSet<>();

			allAuthorities.addAll(rolesAuthorities);
			allAuthorities.addAll(scopesAuthorities);

			return allAuthorities;
		}

	}
}
