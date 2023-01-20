package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.DownloadDocumentUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.Document;
import com.excentria_it.wamya.domain.DocumentType;
import com.tngtech.archunit.thirdparty.com.google.common.net.HttpHeaders;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {DocumentDownloadController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = DocumentDownloadController.class)
public class DocumentDownloadControllerTests {

    @MockBean
    private DownloadDocumentUseCase downloadDocumentUseCase;

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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenAuthenticatedUser_whenGetDocumentAsResponseEntity_ThenReturnResponseEntity() throws Exception {

        // given
        URL url = DocumentDownloadControllerTests.class.getResource("/Image.jpg");
        File file = new File(url.getFile());
        FileSystemResource res = new FileSystemResource(file.getAbsolutePath());

        Document document = new Document(DocumentType.IMAGE_JPEG, res);

        given(downloadDocumentUseCase.getDocument(any(Long.class), any(String.class))).willReturn(document);

        // when

        MvcResult mvcResult = mvc
                .perform(get("/documents/1").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("h", "someHash")).andExpect(status().isOk()).andReturn();

        // then
        assertEquals(DocumentType.IMAGE_JPEG.getMediaType(),
                mvcResult.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE));

        assertTrue(IOUtils.contentEquals(res.getInputStream(),
                new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())));

    }

    @Test
    void givenAnonymousUser_whenGetDocumentAsResponseEntity_ThenReturnResponseEntity() throws Exception {

        // given
        URL url = DocumentDownloadControllerTests.class.getResource("/Image.jpg");
        File file = new File(url.getFile());
        FileSystemResource res = new FileSystemResource(file.getAbsolutePath());

        Document document = new Document(DocumentType.IMAGE_JPEG, res);

        given(downloadDocumentUseCase.getDocument(any(Long.class), any(String.class))).willReturn(document);

        // when

        MvcResult mvcResult = mockMvc.perform(get("/documents/1").param("h", "someHash")).andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(DocumentType.IMAGE_JPEG.getMediaType(),
                mvcResult.getResponse().getHeaderValue(HttpHeaders.CONTENT_TYPE));

        assertTrue(IOUtils.contentEquals(res.getInputStream(),
                new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())));

    }
}
