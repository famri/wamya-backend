package com.excentria_it.wamya.application.port.out;

import java.util.Set;

public interface CheckDocumentEntitlementsPort {

	Boolean checkReadEntitlements(Long documentId, String documentHash, String username, Set<String> authorities);

}
