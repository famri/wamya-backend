package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.UploadIdentityDocumentUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {UploadIdentityDocumentController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = UploadIdentityDocumentController.class)
public class UploadIdentityDocumentControllerTests {
    @Autowired
    private WebApplicationContext context;
    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private UploadIdentityDocumentUseCase uploadIdentityDocumentUseCase;

    @Test
    void givenMultipartFileAndTransporterRole_WhenUploadProfileImage_ThenSucceed() throws Exception {
        InputStream imageIs = UploadIdentityDocumentControllerTests.class.getClassLoader()
                .getResourceAsStream("Image.jpg");

        MockMultipartFile identityDocument = new MockMultipartFile("document", "image.jpg", "image/jpeg", imageIs);

        mvc.perform(MockMvcRequestBuilders.multipart("/users/me/identities").file(identityDocument).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))))
                .andExpect(status().isCreated());

        ArgumentCaptor<BufferedInputStream> isArgumentCaptor = ArgumentCaptor.forClass(BufferedInputStream.class);

        then(uploadIdentityDocumentUseCase).should(times(1)).uploadIdentityDocument(isArgumentCaptor.capture(),
                eq(identityDocument.getOriginalFilename()), eq(TestConstants.DEFAULT_EMAIL));

        assertTrue(IOUtils.contentEquals(identityDocument.getInputStream(), isArgumentCaptor.getValue()));

    }

    @Test
    void givenMultipartFileAndClientRole_WhenUploadProfileImage_ThenSucceed() throws Exception {
        InputStream imageIs = UploadIdentityDocumentControllerTests.class.getClassLoader()
                .getResourceAsStream("Image.jpg");

        MockMultipartFile identityDocument = new MockMultipartFile("document", "image.jpg", "image/jpeg", imageIs);

        mvc.perform(MockMvcRequestBuilders.multipart("/users/me/identities").file(identityDocument).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))))
                .andExpect(status().isCreated());

        ArgumentCaptor<BufferedInputStream> isArgumentCaptor = ArgumentCaptor.forClass(BufferedInputStream.class);

        then(uploadIdentityDocumentUseCase).should(times(1)).uploadIdentityDocument(isArgumentCaptor.capture(),
                eq(identityDocument.getOriginalFilename()), eq(TestConstants.DEFAULT_EMAIL));

        assertTrue(IOUtils.contentEquals(identityDocument.getInputStream(), isArgumentCaptor.getValue()));

    }

    @Test
    void givenMultipartFileAndBadRole_WhenUploadProfileImage_ThenReturnForbidden() throws Exception {
        InputStream imageIs = UploadIdentityDocumentControllerTests.class.getClassLoader()
                .getResourceAsStream("Image.jpg");

        MockMultipartFile identityDocument = new MockMultipartFile("document", "image.jpg", "image/jpeg", imageIs);

        mvc.perform(MockMvcRequestBuilders.multipart("/users/me/identities").file(identityDocument).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE")))))
                .andExpect(status().isForbidden());

        then(uploadIdentityDocumentUseCase).should(never()).uploadIdentityDocument(any(BufferedInputStream.class),
                any(String.class), any(String.class));

    }

}
