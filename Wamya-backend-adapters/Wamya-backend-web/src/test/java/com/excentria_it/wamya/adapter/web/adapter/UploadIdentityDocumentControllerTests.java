package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.UploadIdentityDocumentUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UploadIdentityDocumentController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UploadIdentityDocumentController.class)
public class UploadIdentityDocumentControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UploadIdentityDocumentUseCase uploadIdentityDocumentUseCase;

	@Test
	void givenMultipartFile_WhenUploadProfileImage_ThenSucceed() throws Exception {
		InputStream imageIs = UploadIdentityDocumentControllerTests.class.getClassLoader()
				.getResourceAsStream("Image.jpg");

		MockMultipartFile identityDocument = new MockMultipartFile("document", "image.jpg", "image/jpeg", imageIs);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(MockMvcRequestBuilders.multipart("/users/me/identities").file(identityDocument))
				.andExpect(status().isCreated()).andReturn();

		ArgumentCaptor<BufferedInputStream> isArgumentCaptor = ArgumentCaptor.forClass(BufferedInputStream.class);

		then(uploadIdentityDocumentUseCase).should(times(1)).uploadIdentityDocument(isArgumentCaptor.capture(),
				eq(identityDocument.getOriginalFilename()), eq(TestConstants.DEFAULT_EMAIL));

		assertTrue(IOUtils.contentEquals(identityDocument.getInputStream(), isArgumentCaptor.getValue()));

	}

}
