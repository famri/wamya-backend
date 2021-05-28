package com.excentria_it.wamya.application.aspects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.application.port.out.CheckDocumentEntitlementsPort;
import com.excentria_it.wamya.application.service.DocumentService;
import com.excentria_it.wamya.common.exception.ForbiddenAccessException;

@SpringBootTest(classes = { DocumentService.class, DocumentAccessAspectTestsConfiguration.class })
@ActiveProfiles("app-local")
public class DocumentAccessAspectTests {

	@SpyBean
	private DocumentAccessAspect documentAccessAspect;

	@Autowired
	private CheckDocumentEntitlementsPort checkDocumentEntitlementsPort;

	@Autowired
	@InjectMocks
	private DocumentService documentService;

	@Test
	void whenDocumentService_getDocument_ThenCall_DocumentAccessAspect_beforeGetDocument() {

		// given

		doNothing().when(documentAccessAspect).beforeGetDocument(any(Long.class), any(String.class));

		// when
		documentService.getDocument(1L, "someHash");

		// then
		assertTrue(AopUtils.isAopProxy(documentService));
		assertTrue(AopUtils.isCglibProxy(documentService));

		verify(documentAccessAspect, times(1)).beforeGetDocument(1L, "someHash");

	}

	@Test
	void givenCheckReadEntitlementsReturnsTrue_whenBeforeGetDocumentThenReturnVoid() {

		// given
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("anonymousUser");

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		given(checkDocumentEntitlementsPort.checkReadEntitlements(any(Long.class), any(String.class), any(String.class),
				any(Set.class))).willReturn(true);

		// when
		documentAccessAspect.beforeGetDocument(1L, "someHash");
		// then

	}

	@Test
	void givenCheckReadEntitlementsReturnsFalse_whenBeforeGetDocumentThenThrowForbiddenAccessException() {

		// given
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("anonymousUser");

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		given(checkDocumentEntitlementsPort.checkReadEntitlements(any(Long.class), any(String.class), any(String.class),
				any(Set.class))).willReturn(false);

		// when //then
		assertThrows(ForbiddenAccessException.class, () -> documentAccessAspect.beforeGetDocument(1L, "someHash"));

	}
}
