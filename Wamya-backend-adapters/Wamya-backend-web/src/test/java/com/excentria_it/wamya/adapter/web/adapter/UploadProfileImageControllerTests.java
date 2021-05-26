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
import com.excentria_it.wamya.application.port.in.UploadProfileImageUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UploadProfileImageController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UploadProfileImageController.class)
public class UploadProfileImageControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UploadProfileImageUseCase uploadProfileImageUseCase;

	@Test
	void givenMultipartFile_WhenUploadProfileImage_ThenSucceed() throws Exception {
		InputStream imageIs = UploadProfileImageController.class.getResourceAsStream("/Image.jpg");

		MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", imageIs);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(MockMvcRequestBuilders.multipart("/profiles/me/avatars").file(image))
				.andExpect(status().isCreated()).andReturn();

		ArgumentCaptor<BufferedInputStream> isArgumentCaptor = ArgumentCaptor.forClass(BufferedInputStream.class);

		then(uploadProfileImageUseCase).should(times(1)).uploadProfileImage(isArgumentCaptor.capture(),
				eq(image.getOriginalFilename()), eq(TestConstants.DEFAULT_EMAIL));

		assertTrue(IOUtils.contentEquals(image.getInputStream(), isArgumentCaptor.getValue()));

	}

}
