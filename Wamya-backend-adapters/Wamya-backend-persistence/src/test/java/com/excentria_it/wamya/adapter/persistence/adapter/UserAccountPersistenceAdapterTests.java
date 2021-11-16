package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.ClientMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.TransporterMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DocumentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GenderRepository;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.adapter.persistence.utils.DefaultIds;
import com.excentria_it.wamya.common.exception.GenderNotFoundException;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCodeException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.domain.UserAccount.UserAccountBuilder;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@ExtendWith(MockitoExtension.class)
public class UserAccountPersistenceAdapterTests {

	@Mock
	private ClientRepository clientRepository;
	@Mock
	private TransporterRepository transporterRepository;
	@Mock
	private UserAccountRepository userAccountRepository;
	@Mock
	private InternationalCallingCodeRepository iccRepository;
	@Mock
	private GenderRepository genderRepository;
	@Mock
	private UserAccountMapper userAccountMapper;
	@Mock
	private TransporterMapper transporterMapper;
	@Mock
	private ClientMapper clientMapper;
	@Mock
	private DocumentRepository documentRepository;

	@InjectMocks
	private UserAccountPersistenceAdapter userAccountPersistenceAdapter;

	// Test createUserAccount

	@Test
	void givenNotFoundIcc_whenCreateUserAccount_thenThrowUnsupportedInternationalCallingCodeException() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.build();

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(Optional.empty());

		assertThrows(UnsupportedInternationalCallingCodeException.class,
				() -> userAccountPersistenceAdapter.createUserAccount(userAccount));
	}

	@Test
	void givenNotFoundGender_whenCreateUserAccount_thenThrowGenderNotFoundException() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);
		given(genderRepository.findById(userAccount.getGenderId())).willReturn(Optional.empty());

		assertThrows(GenderNotFoundException.class, () -> userAccountPersistenceAdapter.createUserAccount(userAccount));
	}

	@Test
	void givenFoundIccAndManGender_whenCreateTransporterUserAccount_thenSaveTransporterJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.isTransporter(true).build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		TransporterJpaEntity transporterJpaEntity = UserAccountJpaEntityTestData.defaultNewTransporterJpaEntity();

		GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
		Optional<GenderJpaEntity> genderEntityOptional = Optional.of(genderEntity);

		given(genderRepository.findById(any(Long.class))).willReturn(genderEntityOptional);

		given(transporterMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class),
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class), eq(null))).willReturn(transporterJpaEntity);

		given(transporterRepository.save(transporterJpaEntity)).willReturn(transporterJpaEntity);

		DocumentJpaEntity defaultManAvatarDocument = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_MAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultManAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);
	}

	@Test
	void givenFoundIccAndWomanGender_whenCreateTransporterUserAccount_thenSaveTransporterJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.isTransporter(true).build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		TransporterJpaEntity transporterJpaEntity = UserAccountJpaEntityTestData.defaultNewTransporterJpaEntity();

		GenderJpaEntity genderEntity = GenderJpaTestData.womanGenderJpaEntity();
		Optional<GenderJpaEntity> genderEntityOptional = Optional.of(genderEntity);

		given(genderRepository.findById(any(Long.class))).willReturn(genderEntityOptional);

		given(transporterMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class),
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class), eq(null))).willReturn(transporterJpaEntity);

		given(transporterRepository.save(transporterJpaEntity)).willReturn(transporterJpaEntity);

		DocumentJpaEntity defaultWomanAvatarDocument = DocumentJpaTestData.defaultWomanProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_WOMAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultWomanAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);
	}

	@Test
	void givenFoundIcc_whenCreateClientUserAccount_thenSaveTransporterJpaEntity() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.isTransporter(false).build();

		Optional<InternationalCallingCodeJpaEntity> iccEntity = Optional
				.of(InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity());
		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(iccEntity);

		GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
		Optional<GenderJpaEntity> genderEntityOptional = Optional.of(genderEntity);

		given(genderRepository.findById(any(Long.class))).willReturn(genderEntityOptional);

		ClientJpaEntity clientJpaEntity = UserAccountJpaEntityTestData.defaultNewClientJpaEntity();

		given(clientMapper.mapToJpaEntity(any(UserAccount.class), any(InternationalCallingCodeJpaEntity.class),
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class), eq(null))).willReturn(clientJpaEntity);
		given(clientRepository.save(clientJpaEntity)).willReturn(clientJpaEntity);

		DocumentJpaEntity defaultManAvatarDocument = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_MAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultManAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(clientRepository).should(times(1)).save(clientJpaEntity);
	}

	// Test loadUserAccountByIccAndMobileNumber

	@Test
	void givenExistentUserAccountByIccAndMobileNumber_whenLoadUserAccountByIccAndMobileNumber_thenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> userAccountEntityOptional = Optional.ofNullable(userAccountEntity);

		given(userAccountRepository.findByMobilePhoneNumber(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE,
				TestConstants.DEFAULT_MOBILE_NUMBER)).willReturn(userAccountEntityOptional);

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		given(userAccountMapper.mapToDomainEntity(userAccountEntity)).willReturn(userAccount);

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter
				.loadUserAccountByUsername(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		assertEquals(result.get(), userAccount);

	}

	@Test
	void givenInexistentUserAccountByIccAndMobileNumber_whenLoadUserAccountByIccAndMobileNumber_thenReturnOptionalEmpty() {

		// given

		given(userAccountRepository.findByMobilePhoneNumber(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE,
				TestConstants.DEFAULT_MOBILE_NUMBER)).willReturn(Optional.empty());

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter
				.loadUserAccountByUsername(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		assertThat(result.isEmpty()).isTrue();

	}

	// Test loadUserAccountByEmail
	@Test
	void givenExistentUserAccountByEmail_whenLoadUserAccountByIccAndMobileNumber_thenReturnTheExistingUserAccount() {

		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(clientEntityOptional);

		UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();

		given(userAccountMapper.mapToDomainEntity(clientEntity)).willReturn(userAccount);

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByUsername(userAccount.getEmail());
		// then
		assertEquals(result.get(), userAccount);

	}

	@Test
	void givenNullUsername_whenLoadUserAccountByIccAndMobileNumber_thenReturnOptionalEmpty() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByUsername(null);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenBadUsername_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalEmpty() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByUsername("ThisIsABadUserName");
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenInexistentUserAccountByEmail_whenLoadUserAccountByIccAndMobileNumber_thenReturnOptionalEmpty() {

		// given

		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.empty());

		// when
		Optional<UserAccount> result = userAccountPersistenceAdapter
				.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL);
		// then
		assertTrue(result.isEmpty());

	}

	// Test existsByOauthId
	@Test
	void givenUserExists_whenExistsByOauthId_thenReturnTrue() {

		given(userAccountRepository.existsByOauthId(any(Long.class))).willReturn(true);

		// when
		Boolean result = userAccountPersistenceAdapter.existsByOauthId(1L);
		// then
		assertTrue(result);
	}

	@Test
	void givenInexistentUser_whenExistsByOauthId_thenReturnFalse() {

		given(userAccountRepository.existsByOauthId(any(Long.class))).willReturn(false);

		// when
		Boolean result = userAccountPersistenceAdapter.existsByOauthId(1L);
		// then
		assertFalse(result);
	}

	// TestupdateSMSValidationCode

	@Test
	void givenExistentUserAccountById_whenUpdateSMSValidationCode_thenChangeSMSValidationCodeAndValidatedSMSCodeFalse() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		userAccountPersistenceAdapter.updateSMSValidationCode(clientEntity.getId(), "1234");
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());
		assertEquals("1234", captor.getValue().getMobileNumberValidationCode());
		assertEquals(false, captor.getValue().getIsValidatedMobileNumber());
	}

	@Test
	void givenInexistentUserAccountById_whenUpdateSMSValidationCode_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateSMSValidationCode(1L, "1234"));

	}

	@Test
	void givenExistentUserAccountById_whenUpdateEmailValidationCode_thenChangeEmailValidationCodeAndValidatedEmailCodeFalse() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		userAccountPersistenceAdapter.updateEmailValidationCode(clientEntity.getId(), "1234");
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());
		assertEquals("1234", captor.getValue().getEmailValidationCode());
		assertEquals(false, captor.getValue().getIsValidatedEmail());
	}

	@Test
	void givenInexistentUserAccountById_whenUpdateEmailValidationCode_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateEmailValidationCode(1L, "1234"));

	}

	// Test loadProfileImageLocation

	@Test
	void givenExistentUserAccount_whenLoadProfileImageLocation_thenReturnProfileImageLocation() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		String location = userAccountPersistenceAdapter.loadProfileImageLocation(clientEntity.getId());
		// then

		assertEquals(clientEntity.getProfileImage().getLocation(), location);

	}

	@Test
	void givenInexistentUserAccount_whenLoadProfileImageLocation_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.loadProfileImageLocation(1L));

	}

	@Test
	void givenExistentUserAccountWithNullProfileImage_whenLoadProfileImageLocation_thenReturnNull() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNoProfileImage();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		String location = userAccountPersistenceAdapter.loadProfileImageLocation(clientEntity.getId());
		// then

		assertNull(location);

	}
	// Test hasDefaultProfileImage

	@Test
	void givenExistentUserAccount_whenHasDefaultProfileImage_thenReturnTrue() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		boolean hasDefaultProfileImage = userAccountPersistenceAdapter.hasDefaultProfileImage(clientEntity.getId());
		// then

		assertTrue(hasDefaultProfileImage);

	}

	@Test
	void givenInexistentUserAccount_whenHasDefaultProfileImage_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.hasDefaultProfileImage(1L));

	}

	@Test
	void givenExistentUserAccountWithNullProfileImage_whenHasDefaultProfileImage_thenReturnFalse() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNoProfileImage();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when
		boolean hasDefaultProfileImage = userAccountPersistenceAdapter.hasDefaultProfileImage(clientEntity.getId());
		// then

		assertFalse(hasDefaultProfileImage);

	}

	// Test updateUserProfileImage

	@Test
	void givenExistentUserAccountWithDefaultProfileImage_whenUpdateUserProfileImage_thenSaveProfileImageAndUpdateUserProfileImage() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when

		String location = "/Images/1621329097610-some_man_avatar.jpg";
		String hash = DigestUtils.sha256Hex(location.getBytes());
		userAccountPersistenceAdapter.updateUserProfileImage(clientEntity.getId(), location, hash);
		// then

		then(documentRepository).should(never()).delete(any(DocumentJpaEntity.class));

		ArgumentCaptor<DocumentJpaEntity> captor = ArgumentCaptor.forClass(DocumentJpaEntity.class);
		then(documentRepository).should(times(1)).save(captor.capture());

		assertEquals(location, captor.getValue().getLocation());
		assertEquals(hash, captor.getValue().getHash());
		assertEquals(clientEntity, captor.getValue().getOwner());
		assertEquals(DocumentType.IMAGE_JPEG, captor.getValue().getType());
		assertEquals(false, captor.getValue().getIsDefault());

	}

	@Test
	void givenExistentUserAccountWithNonDefaultProfileImage_whenUpdateUserProfileImage_thenDeleteOldProfileImageAndSaveNewOneAndUpdateUserProfileImage() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndNoIdentityDocument();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		// when

		String location = "/Images/1621329097610-some_other_man_avatar.jpg";
		String hash = DigestUtils.sha256Hex(location.getBytes());

		DocumentJpaEntity oldProfileImage = clientEntity.getProfileImage();

		userAccountPersistenceAdapter.updateUserProfileImage(clientEntity.getId(), location, hash);
		// then

		then(documentRepository).should(times(1)).delete(oldProfileImage);

		ArgumentCaptor<DocumentJpaEntity> captor = ArgumentCaptor.forClass(DocumentJpaEntity.class);
		then(documentRepository).should(times(1)).save(captor.capture());

		assertEquals(location, captor.getValue().getLocation());
		assertEquals(hash, captor.getValue().getHash());
		assertEquals(clientEntity, captor.getValue().getOwner());
		assertEquals(DocumentType.IMAGE_JPEG, captor.getValue().getType());
		assertEquals(false, captor.getValue().getIsDefault());

	}

	@Test
	void givenInexistentUserAccount_whenUpdateUserProfileImage_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		String location = "/Images/1621329097610-some_other_man_avatar.jpg";
		String hash = DigestUtils.sha256Hex(location.getBytes());
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateUserProfileImage(1L, location, hash));

	}

	@Test
	void givenInexistentUserAccount_whenUpdateIdentityDocument_thenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		String location = "/Documents/1621329097610-some_other_man_avatar.pdf";
		String hash = DigestUtils.sha256Hex(location.getBytes());
		assertThrows(UserAccountNotFoundException.class, () -> userAccountPersistenceAdapter.updateIdentityDocument(1L,
				location, hash, DocumentType.APPLICATION_PDF));

	}

	@Test
	void givenExistentUserAccountWithExistentIdentityDocument_whenUpdateIdentityDocument_thenDeleteOldProfileImageAndSaveNewOneAndUpdateUserProfileImage() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndIdentityDocument();

		DocumentJpaEntity newIdentityDocument = DocumentJpaTestData.pdfIdentityDocumentJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);
		given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(newIdentityDocument);
		// when

		String location = "/Documents/1621329097610-some_other_identity.pdf";
		String hash = DigestUtils.sha256Hex(location.getBytes());

		DocumentJpaEntity oldIdentityDocument = clientEntity.getIdentityDocument();

		userAccountPersistenceAdapter.updateIdentityDocument(clientEntity.getId(), location, hash,
				DocumentType.APPLICATION_PDF);
		// then

		then(documentRepository).should(times(1)).delete(oldIdentityDocument);

		ArgumentCaptor<DocumentJpaEntity> identityDocumentCaptor = ArgumentCaptor.forClass(DocumentJpaEntity.class);
		then(documentRepository).should(times(1)).save(identityDocumentCaptor.capture());

		assertEquals(location, identityDocumentCaptor.getValue().getLocation());
		assertEquals(hash, identityDocumentCaptor.getValue().getHash());
		assertEquals(clientEntity, identityDocumentCaptor.getValue().getOwner());
		assertEquals(DocumentType.APPLICATION_PDF, identityDocumentCaptor.getValue().getType());
		assertEquals(false, identityDocumentCaptor.getValue().getIsDefault());

		assertEquals(3, identityDocumentCaptor.getValue().getEntitlements().size());
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.OWNER.equals(e.getType()) && e.getRead() && e.getWrite()).count() == 1);
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.SUPPORT.equals(e.getType()) && e.getRead() && e.getWrite()).count() == 1);
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.OTHERS.equals(e.getType()) && !e.getRead() && !e.getWrite()).count() == 1);

		ArgumentCaptor<UserAccountJpaEntity> userCaptor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(userCaptor.capture());
		assertEquals(newIdentityDocument, userCaptor.getValue().getIdentityDocument());

	}

	@Test
	void givenExistentUserAccountWithNoIdentityDocument_whenUpdateIdentityDocument_thenSaveNewOneAndUpdateUserProfileImage() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndNoIdentityDocument();

		DocumentJpaEntity newIdentityDocument = DocumentJpaTestData.pdfIdentityDocumentJpaEntity();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);
		given(documentRepository.save(any(DocumentJpaEntity.class))).willReturn(newIdentityDocument);
		// when

		String location = "/Documents/1621329097610-some_other_identity.pdf";
		String hash = DigestUtils.sha256Hex(location.getBytes());

		userAccountPersistenceAdapter.updateIdentityDocument(clientEntity.getId(), location, hash,
				DocumentType.APPLICATION_PDF);
		// then

		then(documentRepository).should(never()).delete(any(DocumentJpaEntity.class));

		ArgumentCaptor<DocumentJpaEntity> identityDocumentCaptor = ArgumentCaptor.forClass(DocumentJpaEntity.class);
		then(documentRepository).should(times(1)).save(identityDocumentCaptor.capture());

		assertEquals(location, identityDocumentCaptor.getValue().getLocation());
		assertEquals(hash, identityDocumentCaptor.getValue().getHash());
		assertEquals(clientEntity, identityDocumentCaptor.getValue().getOwner());
		assertEquals(DocumentType.APPLICATION_PDF, identityDocumentCaptor.getValue().getType());
		assertEquals(false, identityDocumentCaptor.getValue().getIsDefault());

		assertEquals(3, identityDocumentCaptor.getValue().getEntitlements().size());
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.OWNER.equals(e.getType()) && e.getRead() && e.getWrite()).count() == 1);
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.SUPPORT.equals(e.getType()) && e.getRead() && e.getWrite()).count() == 1);
		assertTrue(identityDocumentCaptor.getValue().getEntitlements().stream()
				.filter(e -> EntitlementType.OTHERS.equals(e.getType()) && !e.getRead() && !e.getWrite()).count() == 1);

		ArgumentCaptor<UserAccountJpaEntity> userCaptor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(userCaptor.capture());
		assertEquals(newIdentityDocument, userCaptor.getValue().getIdentityDocument());

	}

	@Test
	void givenUserAccountWithNoIdentityDocuement_whenHasNoIdentityImage_thenReturnTrue() {
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndNoIdentityDocument();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		assertTrue(userAccountPersistenceAdapter.hasNoIdentityImage(1L));
	}

	@Test
	void givenUserAccountWithIdentityDocuement_whenHasNoIdentityImage_thenReturnFalse() {
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndIdentityDocument();

		Optional<UserAccountJpaEntity> clientEntityOptional = Optional.of(clientEntity);

		given(userAccountRepository.findById(any(Long.class))).willReturn(clientEntityOptional);

		assertFalse(userAccountPersistenceAdapter.hasNoIdentityImage(1L));
	}

	@Test
	void givenUserAccountNotFoundById_whenHasNoIdentityImage_thenThrowUserAccountNotFoundException() {

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());
		assertThrows(UserAccountNotFoundException.class, () -> userAccountPersistenceAdapter.hasNoIdentityImage(1L));
	}

	@Test
	void givenUserAccountNotFoundById_whenLoadIdentityDocumentLocation_thenThrowUserAccountNotFoundException() {
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.loadIdentityDocumentLocation(1L));
	}

	@Test
	void givenExistentUserAccountByIdAndNonNullIdentityDocument_whenLoadIdentityDocumentLocation_thenReturnUserAccountIdentityDocumentLocation() {
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndIdentityDocument();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(clientEntity));

		String location = userAccountPersistenceAdapter.loadIdentityDocumentLocation(1L);

		assertEquals(clientEntity.getIdentityDocument().getLocation(), location);
	}

	@Test
	void givenExistentUserAccountByIdAndNullIdentityDocument_whenLoadIdentityDocumentLocation_thenReturnUserAccountIdentityDocumentLocation() {
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImageAndNoIdentityDocument();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(clientEntity));

		String location = userAccountPersistenceAdapter.loadIdentityDocumentLocation(1L);

		assertNull(location);
	}

	@Test
	void givenInexistentUserAccountById_whenUpdateDeviceRegistrationToken_thenThrowUserAccountNotFoundException() {
		// given
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when // then
		assertThrows(UserAccountNotFoundException.class, () -> userAccountPersistenceAdapter
				.updateDeviceRegistrationToken(1L, "some-device-registration-token"));

	}

	@Test
	void givenExistentUserAccountById_whenUpdateDeviceRegistrationToken_thenUpdateDeviceRegistrationToken() {
		// given
		UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccount));
		// when
		userAccountPersistenceAdapter.updateDeviceRegistrationToken(1L, "some-device-registration-token");
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertEquals("some-device-registration-token", captor.getValue().getDeviceRegistrationToken());
	}

	@Test
	void givenInexistentUserAccountById_whenUpdateIsValidatedMobileNumber_thenThrowUserAccountNotFoundException() {
		// given
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateIsValidatedMobileNumber(1L, true));

	}

	@Test
	void givenExistentUserAccountById_whenUpdateIsValidatedMobileNumberToTrue_thenUpdateIsValidatedMobileNumber() {
		// given
		UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccount));
		// when
		userAccountPersistenceAdapter.updateIsValidatedMobileNumber(1L, true);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertEquals(true, captor.getValue().getIsValidatedMobileNumber());
	}

	@Test
	void givenExistentUserAccountById_whenUpdateIsValidatedMobileNumberToFalse_thenUpdateIsValidatedMobileNumber() {
		// given
		UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccount));
		// when
		userAccountPersistenceAdapter.updateIsValidatedMobileNumber(1L, false);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertEquals(false, captor.getValue().getIsValidatedMobileNumber());
	}
	
	@Test
	void givenExistentUserAccountById_whenUpdateIdentityValidationState_thenUpdateIdentityValidationState() {
		// given
		UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccount));
		// when
		userAccountPersistenceAdapter.updateIdentityValidationState(1L, ValidationState.PENDING);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());

		assertEquals(ValidationState.PENDING, captor.getValue().getIdentityValidationState());
	}

}
