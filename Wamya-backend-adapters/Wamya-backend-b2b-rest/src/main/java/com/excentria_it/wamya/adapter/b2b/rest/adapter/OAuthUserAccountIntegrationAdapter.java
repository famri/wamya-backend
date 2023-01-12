package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import com.excentria_it.wamya.adapter.b2b.rest.dto.KeyCloakRealmRole;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UserCreationException;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebAdapter
@Slf4j
public class OAuthUserAccountIntegrationAdapter implements OAuthUserAccountPort {

    private static final String USERNAME_FORM_KEY = "username";
    private static final String PASSWORD_FORM_KEY = "password";
    private static final String GRANT_TYPE_FORM_KEY = "grant_type";
    private static final String GRANT_TYPE_FORM_VALUE = "password";

    private AuthServerProperties authServerProperties;

    private RestTemplate restTemplate;

    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    private ObjectMapper mapper;

    public OAuthUserAccountIntegrationAdapter(AuthServerProperties authServerProperties, RestTemplate restTemplate,
                                              ObjectMapper mapper,
                                              AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager) {
        this.authServerProperties = authServerProperties;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.authorizedClientServiceAndManager = authorizedClientServiceAndManager;

    }

    @Override
    public String createOAuthUserAccount(OAuthUserAccount userAccount) throws UserCreationException {

        // get access token for server-to-server api calls
        final OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("on-prem-auth-server-cc").principal("fretto-backend").build();
        final OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);

        if (authorizedClient == null) {
            throw new UserCreationException("Cannot authorize backend client within authorization server.");
        }

        final OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        String userLocation = null;
        try {
            // create the user
            final URI locationUri = createUser(userAccount, accessToken);
            userLocation = locationUri.getPath();
        } catch (RestClientException e) {
            throw new UserCreationException(String.format("Cannot create user in the authorization server:%s", e.getMessage()));
        }

        List<KeyCloakRealmRole> realmRolesForUser = null;
        try {
            // get realm role corresponding to userAccount.getRealmRoles()
            realmRolesForUser = getRealmRoles(userAccount.getRealmRoles(), accessToken);
        } catch (RestClientException e) {
            throw new UserCreationException(String.format("Cannot find user roles in the authorization server: %s", e.getMessage()));
        }

        // skip the leading slash
        final String userOAuthId = userLocation.substring(1);

        try {
            // create the user roles
            createRealmRoleForUser(userOAuthId, realmRolesForUser, accessToken);
        } catch (RestClientException e) {
            throw new UserCreationException(String.format("Cannot create user roles in the authorization server: %s", e.getMessage()));
        }

        return userOAuthId;
    }


    private void createRealmRoleForUser(String userLocation, List<KeyCloakRealmRole> roles, OAuth2AccessToken accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<KeyCloakRealmRole[]> request = new HttpEntity<>(roles.toArray(new KeyCloakRealmRole[roles.size()]), headers);
        restTemplate.postForEntity(authServerProperties.getAddRoleToUserUri(), request, Void.class, userLocation);
    }

    private List<KeyCloakRealmRole> getRealmRoles(Collection<String> userRoles, OAuth2AccessToken accessToken) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<Void> request = new HttpEntity<>(headers);

        final ResponseEntity<KeyCloakRealmRole[]> response = restTemplate.exchange(authServerProperties.getReadRealmRolesUri(), HttpMethod.GET, request, KeyCloakRealmRole[].class);

        final KeyCloakRealmRole[] keyCloakRealmRoles = response.getBody();

        return Arrays.stream(keyCloakRealmRoles).filter(role -> userRoles.contains(role.getName())).collect(Collectors.toList());
    }


    private URI createUser(OAuthUserAccount userAccount, OAuth2AccessToken accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<OAuthUserAccount> request = new HttpEntity<>(userAccount, headers);

        final URI locationUri = restTemplate.postForLocation(authServerProperties.getCreateUserUri(), request);
        return locationUri;
    }

    @Override
    public OpenIdAuthResponse fetchJwtTokenForUser(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(authServerProperties.getClientId(), authServerProperties.getClientSecret());

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<String, String>();
        formParams.add(USERNAME_FORM_KEY, username);
        formParams.add(PASSWORD_FORM_KEY, password);
        formParams.add(GRANT_TYPE_FORM_KEY, GRANT_TYPE_FORM_VALUE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

        return restTemplate.postForObject(authServerProperties.getTokenUri(), request, OpenIdAuthResponse.class);
    }

    @Override
    public void resetPassword(String userOauthId, String password) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

//		getWebClient().post()
//				.uri(authServerProperties.getResetPasswordUri().replace("{oAuthId}", userOauthId.toString()))
//				.headers(headers -> {
//					headers.setBearerAuth(accessToken.getTokenValue());
//					headers.setContentType(MediaType.APPLICATION_JSON);
//				}).bodyValue(new PasswordBody(password)).retrieve().bodyToMono(Void.class).block();

    }

    private Object getWebClient() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateMobileNumber(String userOauthId, String internationalCallingCode, String mobileNumber) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

//		getWebClient().patch()
//				.uri(authServerProperties.getUpdateMobileUri().replace("{oAuthId}", userOauthId.toString()))
//				.headers(headers -> {
//					headers.setBearerAuth(accessToken.getTokenValue());
//					headers.setContentType(MediaType.APPLICATION_JSON);
//				}).bodyValue(new UpdateMobileBody(internationalCallingCode, mobileNumber)).retrieve()
//				.bodyToMono(Void.class).block();

    }

    @Override
    public void updateEmail(String userOauthId, String email) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("on-prem-auth-server-cc").principal("wamya-mobile-app").build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        OAuth2AccessToken accessToken = Objects.requireNonNull(authorizedClient).getAccessToken();

//		getWebClient().patch()
//				.uri(authServerProperties.getUpdateEmailUri().replace("{oAuthId}", userOauthId.toString()))
//				.headers(headers -> {
//					headers.setBearerAuth(accessToken.getTokenValue());
//					headers.setContentType(MediaType.APPLICATION_JSON);
//				}).bodyValue(new UpdateEmailBody(email)).retrieve().bodyToMono(Void.class).block();

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class PasswordBody {
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMobileBody {
        private String icc;
        private String mobileNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateEmailBody {
        private String email;

    }

}
