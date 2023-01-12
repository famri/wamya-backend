package com.excentria_it.wamya.adapter.persistence.mapper;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.domain.UserAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransporterMapper {

    public TransporterJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc,
                                               GenderJpaEntity gender, DocumentJpaEntity profileImage, DocumentJpaEntity identityDocument) {
        if (userAccount == null || !userAccount.getIsTransporter())
            return null;

        Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();

        if (userAccount.getPreferences() != null) {
            userAccount.getPreferences().forEach((k, v) -> preferences.put(k,
                    new UserPreferenceJpaEntity(new UserPreferenceId(userAccount.getId(), k), v, null)));
        }

/*
		return new TransporterJpaEntity(userAccount.getId(), userAccount.getOauthId(), gender,
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.NOT_VALIDATED, "",
				userAccount.getDateOfBirth(), userAccount.getEmail(), userAccount.getEmailValidationCode(),
				userAccount.getIsValidatedEmail(), icc, userAccount.getMobilePhoneNumber().getMobileNumber(),
				userAccount.getMobileNumberValidationCode(), userAccount.getIsValidatedMobileNumber(),
				userAccount.getReceiveNewsletter(), userAccount.getCreationDateTime().toInstant(), profileImage,
				preferences, identityDocument, userAccount.getDeviceRegistrationToken(), userAccount.getIsWebSocketConnected());
*/
        return null;
    }
}
