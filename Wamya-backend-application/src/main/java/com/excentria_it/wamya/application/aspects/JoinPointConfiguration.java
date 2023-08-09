package com.excentria_it.wamya.application.aspects;

import org.aspectj.lang.annotation.Pointcut;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public class JoinPointConfiguration {

	private JoinPointConfiguration() {

	}

	@Pointcut("execution(* com.excentria_it.wamya.application.service.DocumentService.getDocument(Long, String)) && args(documentId, documentHash)")
	public void getDocumentExecution(Long documentId, String documentHash) {
	}

}
