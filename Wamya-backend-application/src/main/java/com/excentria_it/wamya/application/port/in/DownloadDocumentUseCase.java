package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.Document;

public interface DownloadDocumentUseCase {

	Document getDocument(Long documentId, String documentHash);

}
