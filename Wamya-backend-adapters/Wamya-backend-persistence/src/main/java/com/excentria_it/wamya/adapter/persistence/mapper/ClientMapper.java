package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

@Component
public class ClientMapper {

	public ClientJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc) {
		if (userAccount == null || userAccount.getIsTransporter())
			return null;

		return new ClientJpaEntity(userAccount.getId(), userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(), icc,
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime()
						: LocalDateTime.now(ZoneOffset.UTC),
				userAccount.getPhotoUrl(), null);

	}
}
