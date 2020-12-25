package com.excentria_it.wamya.adapter.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.excentria_it.wamya.adapter.web.JwtConfiguration.JwtToJwtAuthenticationTokenConverterImpl;
import com.excentria_it.wamya.adapter.web.JwtConfiguration.SpringAuthorizationServerEmbeddedAuthoritiesConverter;

import net.minidev.json.JSONArray;

public class JwtConfigurationTest {
	SpringAuthorizationServerEmbeddedAuthoritiesConverter authoritiesConverter = new SpringAuthorizationServerEmbeddedAuthoritiesConverter();
	JwtToJwtAuthenticationTokenConverterImpl jwtToJwtAuthenticationTokenConverterImpl = new JwtToJwtAuthenticationTokenConverterImpl(
			authoritiesConverter);

	@Test
	void testConvertJwtToGrantedAuthorityCollection() {
		Jwt jwt = Mockito.mock(Jwt.class);
		Set<String> roles = Set.of("ROLE1", "ROLE2");
		JSONArray arrayOfRoles = new JSONArray();
		arrayOfRoles.addAll(roles);

		Set<String> scopes = Set.of("SCOPE1", "SCOPE2");
		JSONArray arrayOfScopes = new JSONArray();
		arrayOfScopes.addAll(scopes);

		Map<String, Object> claims = Map.of("authorities", arrayOfRoles, "scope", arrayOfScopes);

		given(jwt.getClaims()).willReturn(claims);

		Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
		assertTrue(grantedAuthorities.stream().map(a -> a.getAuthority().substring(5)).collect(Collectors.toSet())
				.containsAll(roles));
	}

	@Test
	void testConvertJwtToGrantedAuthorityCollectionWithNullRolesAndNullScopes() {
		Jwt jwt = Mockito.mock(Jwt.class);

		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", null);
		claims.put("scope", null);

		given(jwt.getClaims()).willReturn(claims);

		Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
		assertTrue(grantedAuthorities.isEmpty());
	}

	@Test
	void testConvertJwtToGrantedAuthorityCollectionWithEmptyRolesAndEmptyScopes() {
		Jwt jwt = Mockito.mock(Jwt.class);

		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", new JSONArray());
		claims.put("scope", new JSONArray());

		given(jwt.getClaims()).willReturn(claims);

		Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
		assertTrue(grantedAuthorities.isEmpty());
	}

	@Test
	void testConvertJwtToJwtAuthenticationToken() {
		Jwt jwt = Mockito.mock(Jwt.class);
		Set<String> roles = Set.of("ROLE1", "ROLE2");
		JSONArray arrayOfRoles = new JSONArray();
		arrayOfRoles.addAll(roles);

		Set<String> scopes = Set.of("SCOPE1", "SCOPE2");
		JSONArray arrayOfScopes = new JSONArray();
		arrayOfScopes.addAll(scopes);

		Map<String, Object> claims = Map.of("authorities", arrayOfRoles, "scope", arrayOfScopes);

		given(jwt.getClaims()).willReturn(claims);

		JwtAuthenticationToken jwtAuthenticationToken = jwtToJwtAuthenticationTokenConverterImpl.convert(jwt);

		assertTrue(jwtAuthenticationToken.getAuthorities().stream().map(a -> a.getAuthority().substring(5))
				.collect(Collectors.toSet()).containsAll(roles));

		assertEquals(jwt, jwtAuthenticationToken.getToken());
	}

	@Test
	void testAuthoritiesConverterBeanCreation() {
		JwtConfiguration conf = new JwtConfiguration();
		Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = conf.authoritiesConverter();
		assertNotNull(authoritiesConverter);
		assertEquals(authoritiesConverter.getClass(), SpringAuthorizationServerEmbeddedAuthoritiesConverter.class);
	}

	@Test
	void testAuthenticationConverterBeanCreation() {
		JwtConfiguration conf = new JwtConfiguration();

		Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = Mockito
				.mock(SpringAuthorizationServerEmbeddedAuthoritiesConverter.class);

		Converter<Jwt, JwtAuthenticationToken> authenticationConverter = conf
				.authenticationConverter(authoritiesConverter);
		assertNotNull(authenticationConverter);
		assertEquals(authenticationConverter.getClass(), JwtToJwtAuthenticationTokenConverterImpl.class);
	}

	@Test
	void testJwtDecoderByIssuerUriBeanCreation() {
		OAuth2ResourceServerProperties properties = Mockito.mock(OAuth2ResourceServerProperties.class);
		org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt jwt = Mockito
				.mock(org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt.class);
		given(jwt.getJwkSetUri()).willReturn("http://SOME-JWK-SET-URI/jwks.json");
		given(properties.getJwt()).willReturn(jwt);

		JwtConfiguration conf = new JwtConfiguration();
		JwtDecoder decoder = conf.jwtDecoderByIssuerUri(properties);

		assertNotNull(decoder);
		assertEquals(NimbusJwtDecoder.class, decoder.getClass());
	}
}
