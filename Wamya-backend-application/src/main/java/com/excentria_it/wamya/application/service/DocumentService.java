package com.excentria_it.wamya.application.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.springframework.core.io.FileSystemResource;

import com.excentria_it.wamya.application.port.in.DownloadDocumentUseCase;
import com.excentria_it.wamya.application.port.out.LoadDocumentInfoPort;
import com.excentria_it.wamya.application.port.out.LoadFilePort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DocumentAccessException;
import com.excentria_it.wamya.domain.Document;
import com.excentria_it.wamya.domain.DocumentInfo;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class DocumentService implements DownloadDocumentUseCase {

	private final LoadDocumentInfoPort loadDocumentInfoPort;

	private final LoadFilePort loadFilePort;

	@Override
	public Document getDocument(Long documentId, String documentHash) {
		
		DocumentInfo documentInfo = loadDocumentInfoPort.getDocumentInfo(documentId);
		if (documentInfo == null) {
			return null;
		}
		FileSystemResource fileResource;
		try {
			fileResource = loadFilePort.loadFile(documentInfo.getLocation());
			return new Document(documentInfo.getType(), fileResource);
		} catch (IOException e) {
			throw new DocumentAccessException(e.getMessage());
		}

	}

}
