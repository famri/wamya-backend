package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DocumentRepository;
import com.excentria_it.wamya.domain.DocumentInfo;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class DocumentPersistenceAdapterTests {

	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private DocumentPersistenceAdapter documentPersistenceAdapter;

	@Test
	void givenInexistentDocumentById_whenCheckReadEntitlements_thenReturnFalse() {
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, "someHash",
				TestConstants.DEFAULT_EMAIL, Collections.emptySet());
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenBadDocumentHash_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, "someHash",
				TestConstants.DEFAULT_EMAIL, Collections.emptySet());
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenDefaultDocument_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Collections.emptySet());
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentAndAdminAuthority_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Set.of("ADMIN"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentAndSupportAuthorityAndNoEntitlements_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithNullEntitlementsDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Set.of("SUPPORT"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentAndSupportAuthorityAndEmptyEntitlements_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithEmptyEntitlementsDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Set.of("SUPPORT"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentAndSupportAuthorityAndNoSupportEntitlement_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithoutSupportEntitlementDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Set.of("SUPPORT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentAndSupportAuthorityAndSupportReadEntitlementFalse_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithSupportEntitlementReadFalseDocumentJpaEntity();
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Set.of("SUPPORT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithoutOwner_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageWithoutOwnerDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				TestConstants.DEFAULT_EMAIL, Collections.emptySet());
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOwnerUserEmailAccessAndOwnerReadEntitlementTrue_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				document.getOwner().getEmail(), Set.of("CLIENT"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOwnerUserMobileAccessAndOwnerReadEntitlementTrue_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				document.getOwner().getIcc().getValue() + "_" + document.getOwner().getMobileNumber(),
				Set.of("CLIENT"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOwnerUserAccessAndOwnerReadEntitlementFalse_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithOwnerReadEntitlementFalseDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				document.getOwner().getEmail(), Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOwnerUserAccessAndNoOwnerEntitlement_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithoutOwnerEntitlementDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				document.getOwner().getEmail(), Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOwnerUserAccessAndNoEntitlements_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithNullEntitlementsDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				document.getOwner().getEmail(), Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOthersUserAccessAndNoEntitlements_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithNullEntitlementsDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				"others@gmail.com", Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOthersUserAccessAndOthersReadEntitlementTrue_whenCheckReadEntitlements_thenReturnTrue() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithOthersReadEntitlementTrueDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				"others@gmail.com", Set.of("CLIENT"));
		// then
		assertTrue(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOthersUserAccessAndOthersReadEntitlementFalse_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithOthersReadEntitlementFalseDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				"others@gmail.com", Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenNonDefaultDocumentWithOwnerAndOthersUserAccessAndNoOthersEntitlement_whenCheckReadEntitlements_thenReturnFalse() {

		DocumentJpaEntity document = DocumentJpaTestData
				.nonDefaultManProfileImageWithNoOthersEntitlementDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		Boolean canReadDocument = documentPersistenceAdapter.checkReadEntitlements(1L, document.getHash(),
				"others@gmail.com", Set.of("CLIENT"));
		// then
		assertFalse(canReadDocument);
	}

	@Test
	void givenInexistentDocumentById_whenGetDocumentInfo_thenReturnNull() {
		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		DocumentInfo docInfo = documentPersistenceAdapter.getDocumentInfo(1L);
		// then
		assertNull(docInfo);
	}

	@Test
	void givenExistentDocumentById_whenGetDocumentInfo_thenReturnDocumentInfo() {

		DocumentJpaEntity document = DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity();

		// given
		given(documentRepository.findById(any(Long.class))).willReturn(Optional.of(document));
		// when
		DocumentInfo docInfo = documentPersistenceAdapter.getDocumentInfo(1L);
		// then
		assertEquals(document.getType(), docInfo.getType());
		assertEquals(document.getLocation(), docInfo.getLocation());
	}

}
