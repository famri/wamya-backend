package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import com.excentria_it.wamya.adapter.b2b.rest.dto.KeyCloakRealmRole;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.UserCreationException;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.excentria_it.wamya.domain.OAuthUserAccountAttribute;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.excentria_it.wamya.domain.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

import static com.excentria_it.wamya.adapter.b2b.rest.adapter.OAuthConstants.*;

@WebAdapter
@Slf4j
public class KeycloakUserAccountIntegrationAdapter implements OAuthUserAccountPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakUserAccountIntegrationAdapter.class);

    private static final String OAUTH2_CLIENT_REGISTRATION_NAME = "on-prem-auth-server-cc";
    private AuthServerProperties authServerProperties;

    private RestTemplate restTemplate;

    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    private ObjectMapper mapper;

    public KeycloakUserAccountIntegrationAdapter(AuthServerProperties authServerProperties, RestTemplate restTemplate,
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
                .withClientRegistrationId(OAUTH2_CLIENT_REGISTRATION_NAME).principal(authServerProperties.getClientId()).build();
        final OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);

        if (authorizedClient == null) {
            throw new UserCreationException("Cannot authorize backend client within authorization server.");
        }

        final OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // create the user
        final String userOAuthId = createUser(userAccount, accessToken);

        // get realm role corresponding to userAccount.getRealmRoles()
        final List<KeyCloakRealmRole> realmRolesForUser = getRealmRoles(userAccount.getRealmRoles(), accessToken);

        // create the user roles
        createRealmRoleForUser(userOAuthId, realmRolesForUser, accessToken);

        return userOAuthId;
    }


    private void createRealmRoleForUser(String userOAuthId, List<KeyCloakRealmRole> roles, OAuth2AccessToken accessToken) {


        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<KeyCloakRealmRole[]> request = new HttpEntity<>(roles.toArray(new KeyCloakRealmRole[roles.size()]), headers);

        try {
            restTemplate.postForEntity(authServerProperties.getAddRoleToUserUri(), request, Void.class, userOAuthId);
        } catch (RestClientException e) {
            LOGGER.error("Exception when creating real roles for user.", e);
            throw new UserCreationException(String.format("Cannot create user roles in the authorization server: %s", e.getMessage()));
        }

    }

    private List<KeyCloakRealmRole> getRealmRoles(Collection<UserRole> userRoles, OAuth2AccessToken accessToken) {

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<KeyCloakRealmRole[]> response = null;

        try {
            response = restTemplate.exchange(authServerProperties.getReadRealmRolesUri(), HttpMethod.GET, request, KeyCloakRealmRole[].class);
        } catch (RestClientException e) {
            LOGGER.error("Exception when reading real roles.", e);
            throw new UserCreationException(String.format("Cannot find user roles in the authorization server: %s", e.getMessage()));
        }

        final KeyCloakRealmRole[] keyCloakRealmRoles = response.getBody();

        final Collection<String> rolesNames = userRoles.stream().map(r -> r.bareName()).collect(Collectors.toSet());

        return Arrays.stream(keyCloakRealmRoles).filter(role -> rolesNames.contains(role.getName())).collect(Collectors.toList());
    }


    private String createUser(OAuthUserAccount userAccount, OAuth2AccessToken accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken.getTokenValue());

        final HttpEntity<OAuthUserAccount> request = new HttpEntity<>(userAccount, headers);
        try {
            URI locationUri = restTemplate.postForLocation(authServerProperties.getCreateUserUri(), request);
            // skip the leading slash from /${userId} location URI
            return locationUri.getPath().substring(locationUri.getPath().lastIndexOf("/") + 1);
        } catch (RestClientException e) {
            LOGGER.error("Exception when creating user.", e);
            throw new UserCreationException(String.format("Cannot create user in the authorization server:%s", e.getMessage()));
        }
    }

    @Override
    public OpenIdAuthResponse fetchJwtTokenForUser(String username, String password) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add(CLIENT_ID_KEY, authServerProperties.getClientId());
        formParams.add(CLIENT_SECRET_KEY, authServerProperties.getClientSecret());
        formParams.add(USERNAME_FORM_KEY, username);
        formParams.add(PASSWORD_FORM_KEY, password);
        formParams.add(GRANT_TYPE_FORM_KEY, GRANT_TYPE_FORM_VALUE);
        formParams.add(SCOPE_KEY, "openid email roles");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);

        try {
            return restTemplate.postForObject(authServerProperties.getTokenUri(), request, OpenIdAuthResponse.class);
        } catch (RestClientException e) {
            LOGGER.error("Exception when fetching JWT for user: {}", username, e);
            throw new RuntimeException(String.format("Cannot fetch JWT for user: %s", e.getMessage()));
        }

    }

    @Override
    public void resetPassword(String userOauthId, String password) {
        // Authorize / re-authorize the client with CLIENT_CREDENTIALS grant
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(OAUTH2_CLIENT_REGISTRATION_NAME).principal(authServerProperties.getClientId()).build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        if (authorizedClient == null) {
            throw new RuntimeException("Cannot authorize backend client within authorization server.");
        }
        // Obtain access token from authorized client
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // update user credentials
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        headers.setContentType(MediaType.APPLICATION_JSON);

        OAuthUserAccount.Credentials credentials = OAuthUserAccount.Credentials.builder().type("password").value(password).temporary(false).build();
        final HttpEntity<OAuthUserAccount.Credentials> updateRequest = new HttpEntity<>(credentials, headers);
        try {
            restTemplate.put(authServerProperties.getResetUserPasswordUri(), updateRequest, userOauthId);
        } catch (RestClientException e) {
            LOGGER.error("Exception when resetting password for user with OAuthId {}.", userOauthId, e);
            throw new RuntimeException(String.format("Exception when resetting user password on authorization server: %s", e.getMessage()));
        }

    }


    @Override
    public void updateMobileNumber(String userOauthId, String internationalCallingCode, String mobileNumber) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(OAUTH2_CLIENT_REGISTRATION_NAME).principal(authServerProperties.getClientId()).build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        if (authorizedClient == null) {
            throw new RuntimeException("Cannot authorize backend client within authorization server.");
        }
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();


        // GET the user representation
        OAuthUserAccount userAccount = getUser(userOauthId, accessToken);

        // update the user mobile number
        userAccount.getAttributes().put(OAuthUserAccountAttribute.PHONE_NUMBER, internationalCallingCode + "_" + mobileNumber);

        // save new user representation to keycloak
        updateUser(userAccount, accessToken);

    }

    private OAuthUserAccount getUser(String userOAuthId, OAuth2AccessToken accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        final HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<OAuthUserAccount> response;

        try {
            response = restTemplate.exchange(authServerProperties.getReadUserUri(), HttpMethod.GET, request, OAuthUserAccount.class, userOAuthId);
        } catch (RestClientException e) {
            throw new UserCreationException(String.format("Cannot create user in the authorization server:%s", e.getMessage()));
        }
        OAuthUserAccount userRepresentation = response.getBody();
        return userRepresentation;
    }

    private void updateUser(OAuthUserAccount userRepresentation, OAuth2AccessToken accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        headers.setContentType(MediaType.APPLICATION_JSON);

        final HttpEntity<OAuthUserAccount> updateRequest = new HttpEntity<>(userRepresentation, headers);
        try {
            restTemplate.put(authServerProperties.getUpdateUserUri(), updateRequest, userRepresentation.getId());
        } catch (RestClientException e) {
            throw new RuntimeException(String.format("Exception when updating user mobile number on authorization server: %s", e.getMessage()));
        }
    }

    @Override
    public void updateEmail(String userOauthId, String email) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(OAUTH2_CLIENT_REGISTRATION_NAME).principal(authServerProperties.getClientId()).build();
        OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);
        if (authorizedClient == null) {
            throw new RuntimeException("Cannot authorize backend client within authorization server.");
        }
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // GET the user representation
        OAuthUserAccount userAccount = getUser(userOauthId, accessToken);

        // update the user mobile number
        userAccount.setEmail(email);

        // save new user representation to keycloak
        updateUser(userAccount, accessToken);

    }

}
