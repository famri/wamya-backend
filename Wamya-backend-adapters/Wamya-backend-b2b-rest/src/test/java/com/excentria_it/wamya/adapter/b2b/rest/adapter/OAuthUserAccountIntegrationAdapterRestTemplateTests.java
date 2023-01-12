package com.excentria_it.wamya.adapter.b2b.rest.adapter;

import com.excentria_it.wamya.adapter.b2b.rest.dto.KeyCloakRealmRole;
import com.excentria_it.wamya.adapter.b2b.rest.props.AuthServerProperties;
import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@ActiveProfiles("b2b-rest-local")
public class OAuthUserAccountIntegrationAdapterRestTemplateTests {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthServerProperties authServerProperties;


    private MockRestServiceServer mockServer;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String ACCESS_TOKEN_STRING = "SOME_TOKEN_STRING";

    @Autowired
    private OAuthUserAccountIntegrationAdapter oAuthUserAccountIntegrationAdapter;

    @MockBean
    private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testCreateOAuthUserAccount() throws URISyntaxException, JsonProcessingException {

        String userOAuthId = UUID.randomUUID().toString();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, ACCESS_TOKEN_STRING, Instant.now(), Instant.now().plusSeconds(60));
        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(Mockito.mock(ClientRegistration.class), "CLIENT_ID", accessToken);
        given(authorizedClientServiceAndManager.authorize(any(OAuth2AuthorizeRequest.class))).willReturn(authorizedClient);

        // user creation request
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("/" + userOAuthId));
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getCreateUserUri())))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .headers(httpHeaders));

        // realm roles reading request
        KeyCloakRealmRole clientRole = KeyCloakRealmRole.builder().id("client-realm-role-uuid").containerId("container-1").name("ROLE_CLIENT").clientRole(false).composite(false).description("Client role").build();
        KeyCloakRealmRole transporterRole = KeyCloakRealmRole.builder().id("transporter-realm-role-uuid").containerId("container-1").name("ROLE_TRANSPORTER").clientRole(false).composite(false).description("Transporter role").build();

        KeyCloakRealmRole[] realmRoles = new KeyCloakRealmRole[]{clientRole, transporterRole};
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getReadRealmRolesUri())))
                .andExpect(method(HttpMethod.GET)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(realmRoles)));

        // user roles creation request
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getAddRoleToUserUri().replace("{userId}", userOAuthId))))
                .andExpect(method(HttpMethod.POST)).andExpect(header(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN_STRING))
                .andRespond(withStatus(HttpStatus.NO_CONTENT)
                        .contentType(MediaType.APPLICATION_JSON).body(objectMapper.writeValueAsString(new KeyCloakRealmRole[]{transporterRole})));

        OAuthUserAccount oAuthUserAccount = new OAuthUserAccount("foued", "amri", "test@test.com", "test@test.com", true, true, List.of(OAuthUserAccount.Credentials.builder().type("password").value("password").temporary(false).build()),
                Map.of("phoneNumber", "+216_22222222"), List.of("ROLE_TRANSPORTER"), Collections.emptyList());

        String result = oAuthUserAccountIntegrationAdapter.createOAuthUserAccount(oAuthUserAccount);

        assertEquals(userOAuthId, result);

    }

    @Test
    void testAuthorizeOAuthUser() throws JsonProcessingException, URISyntaxException {
        OpenIdAuthResponse oAuth2AccessTokenResponse = new OpenIdAuthResponse(ACCESS_TOKEN_STRING, 300L, 1800L, "REFRESH_TOKEN", "Bearer", "ID_TOKEN", "read write");


        MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
        formParams.add("username", "test");
        formParams.add("password", "test");
        formParams.add("grant_type", "password");

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(authServerProperties.getTokenUri())))
                .andExpect(method(HttpMethod.POST)).andExpect(content().formData(formParams)).andExpect(header("Authorization", "Basic " + Base64.encodeBase64String((authServerProperties.getClientId() + ":" + authServerProperties.getClientSecret()).getBytes(StandardCharsets.UTF_8))))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(oAuth2AccessTokenResponse)));

        OpenIdAuthResponse oAuth2AccessToken = oAuthUserAccountIntegrationAdapter.fetchJwtTokenForUser("test",
                "test");

        assertEquals(ACCESS_TOKEN_STRING, oAuth2AccessToken.getAccessToken());

    }
}
