package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.DownloadDocumentUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.Document;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.tngtech.archunit.thirdparty.com.google.common.net.HttpHeaders;

@ActiveProfiles(value = { "web-local" })
@Import(value = { DocumentDownloadController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = DocumentDownloadController.class)
public class DocumentDownloadControllerTests {

	@MockBean
	private DownloadDocumentUseCase downloadDocumentUseCase;

	@Autowired
	private MockMvcSupport api;

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

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_document:read"))
				.perform(get("/documents/1").param("h", "someHash")).andExpect(status().isOk()).andReturn();

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
