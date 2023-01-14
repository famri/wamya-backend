package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import com.excentria_it.wamya.adapter.b2b.rest.dto.KeyCloakRealmRole;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.common.exception.UserCreationException;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.excentria_it.wamya.domain.OAuthUserAccountAttribute;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.excentria_it.wamya.domain.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@ActiveProfiles("b2b-rest-local")
public class KeycloakUserAccountIntegrationAdapterTests {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthServerProperties authServerProperties;
    private MockRestServiceServer mockRestServiceServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static String ACCESS_TOKEN_STRING;
    private static String userOAuthId;
    private static OAuth2AccessToken accessToken;
    private static OAuth2AuthorizedClient authorizedClient;
    private static OAuthUserAccount oAuthUserAccount;
    private static KeyCloakRealmRole clientRole;
    private static KeyCloakRealmRole transporterRole;
    private static KeyCloakRealmRole[] realmRoles;
    @Autowired
    private KeycloakUserAccountIntegrationAdapter oAuthUserAccountIntegrationAdapter;

    @MockBean
    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    @BeforeAll
    public static void initData() {
        ACCESS_TOKEN_STRING = "ACCESS_TOKEN_STRING";
        userOAuthId = UUID.randomUUID().toString();
        accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, ACCESS_TOKEN_STRING, Instant.now(), Instant.now().plusSeconds(60));
        authorizedClient = new OAuth2AuthorizedClient(Mockito.mock(ClientRegistration.class), "CLIENT_ID", accessToken);

        final Map<OAuthUserAccountAttribute, String> oAuthUserAccountAttributes = new HashMap<>();
        oAuthUserAccountAttributes.put(OAuthUserAccountAttribute.PHONE_NUMBER, "+33_0711111111");

        oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com", "test@test.com", true, true, List.of(OAuthUserAccount.Credentials.builder().type("password").value("password").temporary(false).build()),
                oAuthUserAccountAttributes, List.of(UserRole.ROLE_TRANSPORTER), Collections.emptyList());

        clientRole = KeyCloakRealmRole.builder().id("client-realm-role-uuid").containerId("container-1").name(UserRole.ROLE_CLIENT.name()).clientRole(false).composite(false).description("Client role").build();
        transporterRole = KeyCloakRealmRole.builder().id("transporter-realm-role-uuid").containerId("container-1").name(UserRole.ROLE_TRANSPORTER.name()).clientRole(false).composite(false).description("Transporter role").build();

        realmRoles = new KeyCloakRealmRole[]{clientRole, transporterRole};
    }

    @BeforeEach
    public void resetMockServer() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }


    @Test
    void testCreateOAuthUserAccount() throws URISyntaxException, JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // user creation request
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("/" + userOAuthId));
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .headers(httpHeaders));

        // realm roles reading request

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getReadRealmRolesUri())))
                .andExpect(method(HttpMethod.GET)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(realmRoles)));

        // user roles creation request
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getAddRoleToUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.POST)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(new KeyCloakRealmRole[]{transporterRole})))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        // When

        final String result = oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount);

        // Then
        assertEquals(userOAuthId, result);

    }

    @Test
    void givenClientAuthorizationError_whenCreateOAuthUserAccount_thenThrowUserCreationException() {
        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(null);

        // When
        final OAuthUserAccount oAuthUserAccount = new OAuthUserAccount(null, "foued", "amri", "test@test.com", "test@test.com", true, true, List.of(OAuthUserAccount.Credentials.builder().type("password").value("password").temporary(false).build()),
                Map.of(OAuthUserAccountAttribute.PHONE_NUMBER, "+216_22222222"), List.of(UserRole.ROLE_TRANSPORTER), Collections.emptyList());

        // Then
        Assert.assertThrows(UserCreationException.class, () -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

    }

    @Test
    void givenGetRealmRoleForUserThrowsRestClientException_whenCreateOAuthUserAccount_thenThrowUserCreationException() throws URISyntaxException, JsonProcessingException {
        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // user creation request
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("/" + userOAuthId));
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .headers(httpHeaders));

        // getRealmRoles throws RestClientException
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getReadRealmRolesUri())))
                .andExpect(method(HttpMethod.GET)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));


        // When
        // Then

        Assert.assertThrows(UserCreationException.class, () -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

    }

    @Test
    void givenCreateRealmRolesForUserThrowsRestClientException_whenCreateOAuthUserAccount_thenThrowUserCreationException() throws URISyntaxException, JsonProcessingException {
        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // user creation request
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("/" + userOAuthId));
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .headers(httpHeaders));

        // realm roles reading request
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getReadRealmRolesUri())))
                .andExpect(method(HttpMethod.GET)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(realmRoles)));

        // user roles creation request
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(
                        UriComponentsBuilder.fromHttpUrl(authServerProperties.getAddRoleToUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.POST)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(new KeyCloakRealmRole[]{transporterRole})))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then

        Assert.assertThrows(UserCreationException.class, () -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

    }

    @Test
    void givenCreateUserThrowsRestClientException_whenCreateOAuthUserAccount_thenThrowUserCreationException() throws URISyntaxException, JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // user creation request
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        Assert.assertThrows(UserCreationException.class, () -> oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount));

    }

    @Test
    void testAuthorizeOAuthUser() throws JsonProcessingException, URISyntaxException {
        final OpenIdAuthResponse oAuth2AccessTokenResponse = new OpenIdAuthResponse(ACCESS_TOKEN_STRING, 300L, 1800L, "REFRESH_TOKEN", "Bearer", "ID_TOKEN", "read write");

        final MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("username", "test");
        formParams.add("password", "test");
        formParams.add("grant_type", "password");

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getTokenUri())))
                .andExpect(method(HttpMethod.POST)).andExpect(content().formData(formParams)).andExpect(header(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBase64String((authServerProperties.getClientId() + ":" + authServerProperties.getClientSecret()).getBytes(StandardCharsets.UTF_8))))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(oAuth2AccessTokenResponse)));

        final OpenIdAuthResponse oAuth2AccessToken = oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test",
                "test");

        assertEquals(ACCESS_TOKEN_STRING, oAuth2AccessToken.getAccessToken());

    }

    @Test
    void givenClientAuthorizationError_whenRestPassword_thenThrowRuntimeException() {
        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(null);

        // When
        // Then
        Assert.assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.resetPassword("someUserOAuthId", "newPassword"));

    }

    @Test
    void testRestPassword() throws JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // password reset ok
        final String newPassword = "newPassword";
        final OAuthUserAccount.Credentials credentials = OAuthUserAccount.Credentials.builder().type("password").value(newPassword).temporary(false).build();

        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getResetUserPasswordUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(credentials)))
                .andRespond(withStatus(HttpStatus.OK));

        // When

        oAuthUserAccountIntegrationAdapter.resetPassword(userOAuthId, newPassword);

    }

    @Test
    void givenPasswordResetCallToAuthorizationServerThrowsRestClientException_whenRestPassword_thenThrowRuntimeException() throws JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // password reset ok
        final String newPassword = "newPassword";
        final OAuthUserAccount.Credentials credentials = OAuthUserAccount.Credentials.builder().type("password").value(newPassword).temporary(false).build();

        // call to update password is not OK
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getResetUserPasswordUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                .andExpect(content().json(objectMapper.writeValueAsString(credentials)))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.resetPassword(userOAuthId, newPassword));

    }

    @Test
    void testUpdateMobileNumber() throws JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(oAuthUserAccount)));

        // update user request is OK
        oAuthUserAccount.getAttributes().put(OAuthUserAccountAttribute.PHONE_NUMBER, "+33_0722222222");
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getUpdateUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.OK));
        // When

        // Then
        oAuthUserAccountIntegrationAdapter.updateMobileNumber(userOAuthId, "+33", "0722222222");
    }

    @Test
    void givenClientAuthorizationError_whenUpdateMobileNumber_thenThrowRuntimeException() {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(null);

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateMobileNumber(userOAuthId, "+33", "0722222222"));
    }

    @Test
    void givenGetUserRequestThrowsRestClientException_whenUpdateMobileNumber_thenThrowRuntimeException() {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is not OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateMobileNumber(userOAuthId, "+33", "0722222222"));

    }

    @Test
    void givenUpdateUserRequestThrowsRestClientException_whenUpdateMobileNumber_thenThrowRuntimeException() throws JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(oAuthUserAccount)));

        // update user request is not OK
        oAuthUserAccount.getAttributes().put(OAuthUserAccountAttribute.PHONE_NUMBER, "+33_0722222222");
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getUpdateUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateMobileNumber(userOAuthId, "+33", "0722222222"));

    }

    @Test
    void testUpdateEmail() throws JsonProcessingException {
        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(oAuthUserAccount)));

        // update user request is OK
        final String newEmail = "new-email@gmail.com";
        oAuthUserAccount.setEmail(newEmail);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getUpdateUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.OK));

        // When
        // Then
        oAuthUserAccountIntegrationAdapter.updateEmail(userOAuthId, newEmail);

    }

    @Test
    void givenClientAuthorizationError_whenUpdateEmail_thenThrowRuntimeException() {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(null);

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateEmail(userOAuthId, "new-email@gmail.com"));
    }

    @Test
    void givenGetUserRequestThrowsRestClientException_whenUpdateEmail_thenThrowRuntimeException() {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is not OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateEmail(userOAuthId, "new-email@gmail.com"));

    }

    @Test
    void givenUpdateUserRequestThrowsRestClientException_whenUpdateEmail_thenThrowRuntimeException() throws JsonProcessingException {

        // Given
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // getUser request is OK
        oAuthUserAccount.setId(userOAuthId);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getReadUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(oAuthUserAccount)));

        // update user request is not OK
        final String newEmail = "new-email@gmail.com";
        oAuthUserAccount.setEmail(newEmail);
        mockRestServiceServer.expect(ExpectedCount.once(), requestTo(UriComponentsBuilder.fromHttpUrl(authServerProperties.getUpdateUserUri()).build(userOAuthId)))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andExpect(content().json(objectMapper.writeValueAsString(oAuthUserAccount)))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // When
        // Then
        assertThrows(RuntimeException.class, () -> oAuthUserAccountIntegrationAdapter.updateEmail(userOAuthId, "new-email@gmail.com"));

    }

}
