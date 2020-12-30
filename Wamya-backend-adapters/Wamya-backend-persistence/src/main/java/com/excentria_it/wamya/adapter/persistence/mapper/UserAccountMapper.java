package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

@Component
public class UserAccountMapper {

	public UserAccountJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc) {
		if (userAccount == null)
			return null;
		if (userAccount.getIsTransporter()) {

			return new TransporterJpaEntity(userAccount.getId(), userAccount.getOauthId(), userAccount.getGender(),
					userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
					userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
					icc, userAccount.getMobilePhoneNumber().getMobileNumber(),
					userAccount.getMobileNumberValidationCode(), userAccount.getIsValidatedMobileNumber(),
					userAccount.getReceiveNewsletter(),
					userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime()
							: LocalDateTime.now(ZoneOffset.UTC),
					userAccount.getPhotoUrl(), null, null, null, null, null);

		} else {
			return new ClientJpaEntity(userAccount.getId(), userAccount.getOauthId(), userAccount.getGender(),
					userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
					userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
					icc, userAccount.getMobilePhoneNumber().getMobileNumber(),
					userAccount.getMobileNumberValidationCode(), userAccount.getIsValidatedMobileNumber(),
					userAccount.getReceiveNewsletter(),
					userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime()
							: LocalDateTime.now(ZoneOffset.UTC),
					userAccount.getPhotoUrl(), null);
		}

	}

	public UserAccount mapToDomainEntity(UserAccountJpaEntity userAccountJpaEntity) {
		if (userAccountJpaEntity == null)
			return null;

		return UserAccount.builder().id(userAccountJpaEntity.getId()).gender(userAccountJpaEntity.getGender())
				.isTransporter(userAccountJpaEntity instanceof TransporterJpaEntity)
				.firstname(userAccountJpaEntity.getFirstname()).lastname(userAccountJpaEntity.getLastname())
				.dateOfBirth(userAccountJpaEntity.getDateOfBirth()).email(userAccountJpaEntity.getEmail())
				.emailValidationCode(userAccountJpaEntity.getEmailValidationCode())
				.isValidatedEmail(userAccountJpaEntity.getIsValidatedEmail())
				.mobilePhoneNumber(new MobilePhoneNumber(userAccountJpaEntity.getIcc().getValue(),
						userAccountJpaEntity.getMobileNumber()))
				.mobileNumberValidationCode(userAccountJpaEntity.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccountJpaEntity.getIsValidatedMobileNumber())
				.receiveNewsletter(userAccountJpaEntity.getReceiveNewsletter())
				.creationDateTime(userAccountJpaEntity.getCreationDateTime())
				.photoUrl(userAccountJpaEntity.getPhotoUrl()).build();
	}
}
