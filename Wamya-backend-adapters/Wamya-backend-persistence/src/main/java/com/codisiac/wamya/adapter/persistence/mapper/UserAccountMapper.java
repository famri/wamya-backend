package com.codisiac.wamya.adapter.persistence.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.codisiac.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.codisiac.wamya.domain.UserAccount;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;
import com.codisiac.wamya.domain.UserAccount.UserAccountId;
import com.codisiac.wamya.domain.ValidationCode;

@Component
public class UserAccountMapper {

	public UserAccountJpaEntity mapToJpaEntity(UserAccount userAccount) {

		return new UserAccountJpaEntity(userAccount.getId() == null ? null : userAccount.getId().getValue(),
				userAccount.getMobilePhoneNumber().getInternationalCallingCode(),
				userAccount.getMobilePhoneNumber().getMobileNumber(),
				userAccount.getUserPassword().getEncodedPassword(), LocalDateTime.now(),
				userAccount.getValidationCode().getValue(), null, false);
	}

	public UserAccount mapToDomainEntity(UserAccountJpaEntity userAccountJpaEntity) {
		if (userAccountJpaEntity == null)
			return null;

		UserAccountId userAccountId = new UserAccountId(userAccountJpaEntity.getId());

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(userAccountJpaEntity.getInternationalCallingCode(),
				userAccountJpaEntity.getMobileNumber());

		ValidationCode validationCode = new ValidationCode(userAccountJpaEntity.getValidationCode());

		return UserAccount.withId(userAccountId, mobilePhoneNumber, null, validationCode,
				userAccountJpaEntity.isValidated());
	}
}
