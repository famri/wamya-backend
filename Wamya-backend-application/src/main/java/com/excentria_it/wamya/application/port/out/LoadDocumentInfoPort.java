package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.DocumentInfo;

public interface LoadDocumentInfoPort {

	DocumentInfo getDocumentInfo(Long documentId);

}
