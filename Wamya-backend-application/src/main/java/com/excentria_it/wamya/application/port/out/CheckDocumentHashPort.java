package com.excentria_it.wamya.application.port.out;

public interface CheckDocumentHashPort {

	Boolean checkHash(Long documentId, String documentHash);

}
