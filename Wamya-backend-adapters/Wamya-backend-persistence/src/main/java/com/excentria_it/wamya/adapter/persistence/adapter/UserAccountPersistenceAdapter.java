package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EntitlementJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
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
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.GenderNotFoundException;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCodeException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.DocumentType;
import com.excentria_it.wamya.domain.EntitlementType;
import com.excentria_it.wamya.domain.Gender;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class UserAccountPersistenceAdapter
		implements CreateUserAccountPort, LoadUserAccountPort, UpdateUserAccountPort {

	private final UserAccountRepository userAccountRepository;

	private final TransporterRepository transporterRepository;

	private final ClientRepository clientRepository;

	private final InternationalCallingCodeRepository iccRepository;

	private final GenderRepository genderRepository;

	private final UserAccountMapper userAccountMapper;

	private final TransporterMapper transporterMapper;

	private final ClientMapper clientMapper;

	private final DocumentRepository documentRepository;

	@Override
	public Long createUserAccount(UserAccount userAccount) throws UnsupportedInternationalCallingCodeException {

		Optional<InternationalCallingCodeJpaEntity> iccEntity = iccRepository
				.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode());

		if (iccEntity.isEmpty())
			throw new UnsupportedInternationalCallingCodeException(
					String.format("Unsupported international calling code %s",
							userAccount.getMobilePhoneNumber().getInternationalCallingCode()));

		Optional<GenderJpaEntity> genderEntity = genderRepository.findById(userAccount.getGenderId());

		if (genderEntity.isEmpty())
			throw new GenderNotFoundException(String.format("Gender ID not found: %s", userAccount.getGenderId()));

		Optional<DocumentJpaEntity> defaultAvatarDocumentOptional = null;
		if (Gender.MAN.equals(genderEntity.get().getGender())) {
			defaultAvatarDocumentOptional = documentRepository.findById(DefaultIds.DEFAULT_MAN_AVATAR_DOCUMENT_ID);
		} else if (Gender.WOMAN.equals(genderEntity.get().getGender())) {
			defaultAvatarDocumentOptional = documentRepository.findById(DefaultIds.DEFAULT_WOMAN_AVATAR_DOCUMENT_ID);
		}

		UserAccountJpaEntity result;
		if (userAccount.getIsTransporter()) {

			result = transporterRepository.save(transporterMapper.mapToJpaEntity(userAccount, iccEntity.get(),
					genderEntity.get(), defaultAvatarDocumentOptional.get(), null));
		} else {
			result = clientRepository.save(clientMapper.mapToJpaEntity(userAccount, iccEntity.get(), genderEntity.get(),
					defaultAvatarDocumentOptional.get(), null));
		}

		return result.getId();
	}

	@Override
	public Optional<UserAccount> loadUserAccountByUsername(String username) {

		if (username == null || (!username.contains("@") && !username.contains("_"))) {
			return Optional.empty();
		}
		if (username.contains("@")) {
			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByEmail(username);
			if (optionalEntity.isEmpty())
				return Optional.empty();
			UserAccountJpaEntity entity = optionalEntity.get();

			UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);
			return Optional.of(userAccount);
		} else {
			String[] mobileNumber = username.split("_");
			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository
					.findByMobilePhoneNumber(mobileNumber[0], mobileNumber[1]);

			if (optionalEntity.isEmpty())
				return Optional.empty();

			UserAccountJpaEntity entity = optionalEntity.get();

			UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

			return Optional.of(userAccount);
		}

	}

	@Override
	public Boolean existsByOauthId(Long userOauthId) {

		return userAccountRepository.existsByOauthId(userOauthId);
	}

	@Override
	public void updateSMSValidationCode(Long userId, String validationCode) {

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();
			userAccountJpaEntity.setMobileNumberValidationCode(validationCode);
			userAccountJpaEntity.setIsValidatedMobileNumber(false);
			userAccountRepository.save(userAccountJpaEntity);
		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));

	}

	@Override
	public void updateEmailValidationCode(Long userId, String validationCode) {

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();
			userAccountJpaEntity.setEmailValidationCode(validationCode);
			userAccountJpaEntity.setIsValidatedEmail(false);
			userAccountRepository.save(userAccountJpaEntity);
		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));

	}

	@Override
	public String loadProfileImageLocation(Long userId) {
		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();
			DocumentJpaEntity profileImage = userAccountJpaEntity.getProfileImage();
			if (profileImage != null) {
				return profileImage.getLocation();
			}
			return null;

		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));

	}

	@Override
	public Boolean hasDefaultProfileImage(Long userId) {
		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();
			DocumentJpaEntity profileImage = userAccountJpaEntity.getProfileImage();
			if (profileImage != null) {
				return profileImage.getIsDefault();
			}
			return false;

		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));

	}

	@Override
	public void updateUserProfileImage(Long userId, String location, String hash) {

		Optional<UserAccountJpaEntity> userAccountEntityOptional = userAccountRepository.findById(userId);
		if (userAccountEntityOptional.isPresent()) {
			UserAccountJpaEntity userAccountEntity = userAccountEntityOptional.get();
			DocumentJpaEntity currentProfileAvatar = userAccountEntity.getProfileImage();
			if (currentProfileAvatar != null && !currentProfileAvatar.getIsDefault()) {
				documentRepository.delete(currentProfileAvatar);
			}
			EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
			EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, true, false);
			EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

			Set<EntitlementJpaEntity> entitlements = Set.of(ownerEntitlement, othersEntitlement, supportEntitlement);

			DocumentJpaEntity newProfileAvatar = new DocumentJpaEntity(userAccountEntity, location,
					DocumentType.IMAGE_JPEG, Instant.now(), entitlements, hash, false);
			newProfileAvatar = documentRepository.save(newProfileAvatar);
			userAccountEntity.setProfileImage(newProfileAvatar);
			userAccountRepository.save(userAccountEntity);

		} else {
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));
		}
	}

	@Override
	public void updateIdentityDocument(Long userId, String location, String hash, DocumentType documentType) {

		Optional<UserAccountJpaEntity> userAccountEntityOptional = userAccountRepository.findById(userId);
		if (userAccountEntityOptional.isPresent()) {
			UserAccountJpaEntity userAccountEntity = userAccountEntityOptional.get();
			DocumentJpaEntity currentIdentityDocument = userAccountEntity.getIdentityDocument();
			if (currentIdentityDocument != null) {
				documentRepository.delete(currentIdentityDocument);
			}
			EntitlementJpaEntity ownerEntitlement = new EntitlementJpaEntity(EntitlementType.OWNER, true, true);
			EntitlementJpaEntity othersEntitlement = new EntitlementJpaEntity(EntitlementType.OTHERS, false, false);
			EntitlementJpaEntity supportEntitlement = new EntitlementJpaEntity(EntitlementType.SUPPORT, true, true);

			Set<EntitlementJpaEntity> entitlements = Set.of(ownerEntitlement, othersEntitlement, supportEntitlement);

			DocumentJpaEntity newIdentityDocument = new DocumentJpaEntity(userAccountEntity, location, documentType,
					Instant.now(), entitlements, hash, false);
			newIdentityDocument = documentRepository.save(newIdentityDocument);
			userAccountEntity.setIdentityDocument(newIdentityDocument);
			userAccountRepository.save(userAccountEntity);

		} else {
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));
		}

	}

	@Override
	public boolean hasNoIdentityImage(Long userId) {
		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();

			return userAccountJpaEntity.getIdentityDocument() == null;

		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));
	}

	@Override
	public String loadIdentityDocumentLocation(Long userId) {
		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userId);

		if (optionalEntity.isPresent()) {

			UserAccountJpaEntity userAccountJpaEntity = optionalEntity.get();
			DocumentJpaEntity identityDocument = userAccountJpaEntity.getIdentityDocument();
			if (identityDocument != null) {
				return identityDocument.getLocation();
			}
			return null;

		} else
			throw new UserAccountNotFoundException(String.format("No account was found by ID %d.", userId));
	}

}
