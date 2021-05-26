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
import com.excentria_it.wamya.domain.UserAccount;
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
	void givenNotFoundIcc_WhenCreateUserAccount_ThenThrowUnsupportedInternationalCallingCodeException() {

		UserAccountBuilder userAccountBuilder = UserAccountTestData.defaultUserAccountBuilder();
		UserAccount userAccount = userAccountBuilder.build();

		given(iccRepository.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode()))
				.willReturn(Optional.empty());

		assertThrows(UnsupportedInternationalCallingCodeException.class,
				() -> userAccountPersistenceAdapter.createUserAccount(userAccount));
	}

	@Test
	void givenNotFoundGender_WhenCreateUserAccount_ThenThrowGenderNotFoundException() {

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
	void givenFoundIccAndManGender_WhenCreateTransporterUserAccount_ThenSaveTransporterJpaEntity() {

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
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class))).willReturn(transporterJpaEntity);

		given(transporterRepository.save(transporterJpaEntity)).willReturn(transporterJpaEntity);

		DocumentJpaEntity defaultManAvatarDocument = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_MAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultManAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);
	}

	@Test
	void givenFoundIccAndWomanGender_WhenCreateTransporterUserAccount_ThenSaveTransporterJpaEntity() {

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
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class))).willReturn(transporterJpaEntity);

		given(transporterRepository.save(transporterJpaEntity)).willReturn(transporterJpaEntity);

		DocumentJpaEntity defaultWomanAvatarDocument = DocumentJpaTestData.defaultWomanProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_WOMAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultWomanAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(transporterRepository).should(times(1)).save(transporterJpaEntity);
	}

	@Test
	void givenFoundIcc_WhenCreateClientUserAccount_ThenSaveTransporterJpaEntity() {

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
				any(GenderJpaEntity.class), any(DocumentJpaEntity.class))).willReturn(clientJpaEntity);
		given(clientRepository.save(clientJpaEntity)).willReturn(clientJpaEntity);

		DocumentJpaEntity defaultManAvatarDocument = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
		given(documentRepository.findById(DefaultIds.DEFAULT_MAN_AVATAR_DOCUMENT_ID))
				.willReturn(Optional.of(defaultManAvatarDocument));

		userAccountPersistenceAdapter.createUserAccount(userAccount);

		then(clientRepository).should(times(1)).save(clientJpaEntity);
	}

	// Test loadUserAccountByIccAndMobileNumber

	@Test
	void givenExistentUserAccountByIccAndMobileNumber_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

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
	void givenInexistentUserAccountByIccAndMobileNumber_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalEmpty() {

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
	void givenExistentUserAccountByEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnTheExistingUserAccount() {

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
	void givenNullUsername_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalEmpty() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByUsername(null);
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenBadUsername_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalEmpty() {
		Optional<UserAccount> result = userAccountPersistenceAdapter.loadUserAccountByUsername("ThisIsABadUserName");
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void givenInexistentUserAccountByEmail_WhenLoadUserAccountByIccAndMobileNumber_ThenReturnOptionalEmpty() {

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
	void givenUserExists_WhenExistsByOauthId_ThenReturnTrue() {

		given(userAccountRepository.existsByOauthId(any(Long.class))).willReturn(true);

		// when
		Boolean result = userAccountPersistenceAdapter.existsByOauthId(1L);
		// then
		assertTrue(result);
	}

	@Test
	void givenInexistentUser_WhenExistsByOauthId_ThenReturnFalse() {

		given(userAccountRepository.existsByOauthId(any(Long.class))).willReturn(false);

		// when
		Boolean result = userAccountPersistenceAdapter.existsByOauthId(1L);
		// then
		assertFalse(result);
	}

	// TestupdateSMSValidationCode

	@Test
	void givenExistentUserAccountById_WhenUpdateSMSValidationCode_ThenChangeSMSValidationCodeAndValidatedSMSCodeFalse() {
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
	void givenInexistentUserAccountById_WhenUpdateSMSValidationCode_ThenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateSMSValidationCode(1L, "1234"));

	}

	@Test
	void givenExistentUserAccountById_WhenUpdateEmailValidationCode_ThenChangeEmailValidationCodeAndValidatedEmailCodeFalse() {
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
	void givenInexistentUserAccountById_WhenUpdateEmailValidationCode_ThenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateEmailValidationCode(1L, "1234"));

	}

	// Test loadProfileImageLocation

	@Test
	void givenExistentUserAccount_WhenLoadProfileImageLocationThenReturnProfileImageLocation() {
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
	void givenInexistentUserAccount_WhenLoadProfileImageLocationThenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.loadProfileImageLocation(1L));

	}

	@Test
	void givenExistentUserAccountWithNullProfileImage_WhenLoadProfileImageLocationThenReturnNull() {
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
	void givenExistentUserAccount_WhenHasDefaultProfileImageThenReturnTrue() {
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
	void givenInexistentUserAccount_WhenHasDefaultProfileImageThenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.hasDefaultProfileImage(1L));

	}

	@Test
	void givenExistentUserAccountWithNullProfileImage_WhenHasDefaultProfileImageThenReturnFalse() {
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
	void givenExistentUserAccountWithDefaultProfileImage_WhenUpdateUserProfileImageThenSaveProfileImageAndUpdateUserProfileImage() {
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
	void givenExistentUserAccountWithNonDefaultProfileImage_WhenUpdateUserProfileImageThenDeleteOldProfileImageAndSaveNewOneAndUpdateUserProfileImage() {
		// given
		UserAccountJpaEntity clientEntity = UserAccountJpaEntityTestData
				.defaultExistentClientJpaEntityWithNonDefaultProfileImage();

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
	void givenInexistentUserAccount_WhenUpdateUserProfileImageThenThrowUserAccountNotFoundException() {
		// given

		given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		String location = "/Images/1621329097610-some_other_man_avatar.jpg";
		String hash = DigestUtils.sha256Hex(location.getBytes());
		assertThrows(UserAccountNotFoundException.class,
				() -> userAccountPersistenceAdapter.updateUserProfileImage(1L, location, hash));

	}

}
