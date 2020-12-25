package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

@Component
public class UserAccountMapper {

	public UserAccountJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc) {
		if (userAccount == null)
			return null;
		
		return UserAccountJpaEntity.builder().id(userAccount.getId()).oauthId(userAccount.getOauthId())
				.isTransporter(userAccount.getIsTransporter()).gender(userAccount.getGender())
				.firstname(userAccount.getFirstname()).lastname(userAccount.getLastname())
				.dateOfBirth(userAccount.getDateOfBirth()).email(userAccount.getEmail())
				.emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail()).icc(icc)
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationDateTime(userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime()
						: LocalDateTime.now(ZoneOffset.UTC))
				.photoUrl(userAccount.getPhotoUrl()).build();
	}

	public UserAccount mapToDomainEntity(UserAccountJpaEntity userAccountJpaEntity) {
		if (userAccountJpaEntity == null)
			return null;

		return UserAccount.builder().id(userAccountJpaEntity.getId())
				.isTransporter(userAccountJpaEntity.getIsTransporter()).gender(userAccountJpaEntity.getGender())
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
