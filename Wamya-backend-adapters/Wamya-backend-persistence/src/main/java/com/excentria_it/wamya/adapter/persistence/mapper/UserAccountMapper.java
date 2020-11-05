package com.excentria_it.wamya.adapter.persistence.mapper;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

@Component
public class UserAccountMapper {

	public UserAccountJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc) {
		return UserAccountJpaEntity.builder().id(userAccount.getId()).oauthUuid(userAccount.getOauthUuid())
				.isTransporter(userAccount.getIsTransporter()).gender(userAccount.getGender())
				.firstName(userAccount.getFirstName()).lastName(userAccount.getLastName())
				.dateOfBirth(userAccount.getDateOfBirth()).email(userAccount.getEmail())
				.emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail()).icc(icc)
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.password(userAccount.getUserPassword()).receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationTimestamp(userAccount.getCreationTimestamp()).build();
	}

	public UserAccount mapToDomainEntity(UserAccountJpaEntity userAccountJpaEntity) {
		if (userAccountJpaEntity == null)
			return null;

		return UserAccount.builder().id(userAccountJpaEntity.getId())
				.isTransporter(userAccountJpaEntity.getIsTransporter()).gender(userAccountJpaEntity.getGender())
				.firstName(userAccountJpaEntity.getFirstName()).lastName(userAccountJpaEntity.getLastName())
				.dateOfBirth(userAccountJpaEntity.getDateOfBirth()).email(userAccountJpaEntity.getEmail())
				.emailValidationCode(userAccountJpaEntity.getEmailValidationCode())
				.isValidatedEmail(userAccountJpaEntity.getIsValidatedEmail())
				.mobilePhoneNumber(new MobilePhoneNumber(userAccountJpaEntity.getIcc().getValue(),
						userAccountJpaEntity.getMobileNumber()))
				.mobileNumberValidationCode(userAccountJpaEntity.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccountJpaEntity.getIsValidatedMobileNumber())
				.userPassword(userAccountJpaEntity.getPassword())
				.receiveNewsletter(userAccountJpaEntity.getReceiveNewsletter())
				.creationTimestamp(userAccountJpaEntity.getCreationTimestamp()).build();
	}
}
