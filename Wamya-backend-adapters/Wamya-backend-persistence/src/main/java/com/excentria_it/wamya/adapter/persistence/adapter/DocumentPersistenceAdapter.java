package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EntitlementJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DocumentRepository;
import com.excentria_it.wamya.application.port.out.CheckDocumentEntitlementsPort;
import com.excentria_it.wamya.application.port.out.LoadDocumentInfoPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.DocumentInfo;
import com.excentria_it.wamya.domain.EntitlementType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class DocumentPersistenceAdapter implements CheckDocumentEntitlementsPort, LoadDocumentInfoPort {

	private static final String ADMIN_ROLE = "ROLE_ADMIN";
	private static final String SUPPORT_ROLE = "ROLE_SUPPORT";

	private final DocumentRepository documentRepository;

	@Override
	public Boolean checkReadEntitlements(Long documentId, String documentHash, String username,
			Set<String> authorities) {

		Optional<DocumentJpaEntity> documentOptional = documentRepository.findById(documentId);

		if (documentOptional.isEmpty() || !documentOptional.get().getHash().equals(documentHash)) {
			return false;
		}

		DocumentJpaEntity document = documentOptional.get();

		if (document.getIsDefault()) {
			return true;
		}

		if (authorities.contains(ADMIN_ROLE)) {
			return true;
		}

		Set<EntitlementJpaEntity> entitlements = document.getEntitlements();

		if (authorities.contains(SUPPORT_ROLE)) {
			if (entitlements == null || entitlements.isEmpty()) {
				return true;
			} else {
				Optional<EntitlementJpaEntity> supportEntitlementOptional = entitlements.stream()
						.filter(e -> e.getType().equals(EntitlementType.SUPPORT)).findFirst();
				return supportEntitlementOptional.isPresent() && supportEntitlementOptional.get().getRead();
			}
		}

		if (document.getOwner() != null) {
			String ownerEmail = document.getOwner().getEmail();
			String ownerMobileNumber = document.getOwner().getIcc().getValue() + "_"
					+ document.getOwner().getMobileNumber();

			if (username.equals(ownerEmail) || username.equals(ownerMobileNumber)) {
				if (entitlements != null) {
					Optional<EntitlementJpaEntity> ownerEntitlementOptional = entitlements.stream()
							.filter(e -> e.getType().equals(EntitlementType.OWNER)).findFirst();
					return ownerEntitlementOptional.isPresent() && ownerEntitlementOptional.get().getRead();
				} else {
					return false;
				}

			} else {
				if (entitlements != null) {
					Optional<EntitlementJpaEntity> othersEntitlementOptional = entitlements.stream()
							.filter(e -> e.getType().equals(EntitlementType.OTHERS)).findFirst();
					return othersEntitlementOptional.isPresent() && othersEntitlementOptional.get().getRead();
				} else {
					return false;
				}
			}
		} else {
			return true;
		}

	}

	@Override
	public DocumentInfo getDocumentInfo(Long documentId) {

		Optional<DocumentJpaEntity> documentOptional = documentRepository.findById(documentId);
		if (documentOptional.isEmpty()) {
			return null;
		}

		return new DocumentInfo(documentOptional.get().getType(), documentOptional.get().getLocation());
	}

}
