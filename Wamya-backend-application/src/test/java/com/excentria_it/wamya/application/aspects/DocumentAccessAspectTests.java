package com.excentria_it.wamya.application.aspects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.excentria_it.wamya.application.port.out.CheckDocumentEntitlementsPort;
import com.excentria_it.wamya.application.port.out.LoadDocumentInfoPort;
import com.excentria_it.wamya.application.port.out.LoadFilePort;
import com.excentria_it.wamya.common.exception.ForbiddenAccessException;

@ExtendWith(MockitoExtension.class)
public class DocumentAccessAspectTests {

	@Mock
	private LoadDocumentInfoPort loadDocumentInfoPort;
	@Mock
	private LoadFilePort loadFilePort;
	@Mock
	private CheckDocumentEntitlementsPort checkDocumentEntitlementsPort;

	@InjectMocks
	private DocumentAccessAspect documentAccessAspect;

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
