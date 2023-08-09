package com.excentria_it.wamya.application.aspects;

import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.port.out.CheckDocumentEntitlementsPort;
import com.excentria_it.wamya.common.exception.ForbiddenAccessException;

import lombok.RequiredArgsConstructor;

@Aspect
@RequiredArgsConstructor
@Component
public class DocumentAccessAspect {

	private final CheckDocumentEntitlementsPort checkDocumentEntitlementsPort;



	@Before("com.excentria_it.wamya.application.aspects.JoinPointConfiguration.getDocumentExecution(documentId, documentHash)")
	public void beforeGetDocument(Long documentId, String documentHash) throws ForbiddenAccessException {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		Boolean checkEntitlementsResult = checkDocumentEntitlementsPort.checkReadEntitlements(documentId, documentHash,
				authentication.getName(),
				authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));

		if (!checkEntitlementsResult) {

			throw new ForbiddenAccessException("You are unauthorized to access this document.");
		}

	}
}