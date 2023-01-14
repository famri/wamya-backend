package com.excentria_it.wamya.adapter.persistence.mapper;

import com.excentria_it.wamya.adapter.persistence.entity.*;
import com.excentria_it.wamya.application.utils.DocumentUrlResolver;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.domain.ProfileInfoDto.*;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserAccountMapper {

    private final DocumentUrlResolver documentUrlResolver;

    public UserAccountJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc,
                                               GenderJpaEntity gender, DocumentJpaEntity profileImage, DocumentJpaEntity identityDocument) {
        if (userAccount == null)
            return null;

        Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();

        userAccount.getPreferences().forEach((k, v) -> preferences.put(k,
                new UserPreferenceJpaEntity(new UserPreferenceId(userAccount.getId(), k), v, null)));

        if (userAccount.getIsTransporter()) {

            return new TransporterJpaEntity(userAccount.getId(), userAccount.getOauthId(), gender,
                    userAccount.getFirstname(), userAccount.getLastname(), ValidationState.NOT_VALIDATED, "",
                    userAccount.getDateOfBirth(), userAccount.getEmail(), userAccount.getEmailValidationCode(),
                    userAccount.getIsValidatedEmail(), icc, userAccount.getMobilePhoneNumber().getMobileNumber(),
                    userAccount.getMobileNumberValidationCode(), userAccount.getIsValidatedMobileNumber(),
                    userAccount.getReceiveNewsletter(),
                    userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime().toInstant()
                            : Instant.now(),
                    profileImage, preferences, identityDocument, userAccount.getDeviceRegistrationToken(),
                    userAccount.getIsWebSocketConnected());

        } else {
            return new ClientJpaEntity(userAccount.getId(), userAccount.getOauthId(), gender,
                    userAccount.getFirstname(), userAccount.getLastname(), ValidationState.NOT_VALIDATED, "",
                    userAccount.getDateOfBirth(), userAccount.getEmail(), userAccount.getEmailValidationCode(),
                    userAccount.getIsValidatedEmail(), icc, userAccount.getMobilePhoneNumber().getMobileNumber(),
                    userAccount.getMobileNumberValidationCode(), userAccount.getIsValidatedMobileNumber(),
                    userAccount.getReceiveNewsletter(),
                    userAccount.getCreationDateTime() != null ? userAccount.getCreationDateTime().toInstant()
                            : Instant.now(),
                    profileImage, preferences, identityDocument, userAccount.getDeviceRegistrationToken(),
                    userAccount.getIsWebSocketConnected());
        }


    }

    public UserAccount mapToDomainEntity(UserAccountJpaEntity userAccountJpaEntity) {
        if (userAccountJpaEntity == null)
            return null;

        Map<String, String> preferences = new HashMap<>();

        userAccountJpaEntity.getPreferences().forEach((k, v) -> preferences.put(k, v.getValue()));

        return UserAccount.builder().id(userAccountJpaEntity.getId()).oauthId(userAccountJpaEntity.getOauthId())
                .genderId(userAccountJpaEntity.getGender().getId())
                .isTransporter(userAccountJpaEntity instanceof TransporterJpaEntity)
                .firstname(userAccountJpaEntity.getFirstname()).lastname(userAccountJpaEntity.getLastname())
                .dateOfBirth(userAccountJpaEntity.getDateOfBirth()).email(userAccountJpaEntity.getEmail())
                .emailValidationCode(userAccountJpaEntity.getEmailValidationCode())
                .isValidatedEmail(userAccountJpaEntity.getIsValidatedEmail())
                .mobilePhoneNumber(new UserAccount.MobilePhoneNumber(userAccountJpaEntity.getIcc().getValue(),
                        userAccountJpaEntity.getMobileNumber()))
                .mobileNumberValidationCode(userAccountJpaEntity.getMobileNumberValidationCode())
                .isValidatedMobileNumber(userAccountJpaEntity.getIsValidatedMobileNumber())
                .receiveNewsletter(userAccountJpaEntity.getReceiveNewsletter())
                .creationDateTime(userAccountJpaEntity.getCreationDateTime().atZone(ZoneOffset.UTC))
                .photoUrl(documentUrlResolver.resolveUrl(userAccountJpaEntity.getProfileImage().getId(),
                        userAccountJpaEntity.getProfileImage().getHash()))
                .deviceRegistrationToken(userAccountJpaEntity.getDeviceRegistrationToken()).preferences(preferences)
                .isWebSocketConnected(userAccountJpaEntity.getIsWebSocketConnected())
                .build();
    }

    public ProfileInfoDto mapToProfileInfoDto(UserAccountJpaEntity userAccount, String locale) {
        return new ProfileInfoDto(
                new GenderInfo(userAccount.getGender().getId(), userAccount.getGender().getName(locale)),
                new NameInfo(userAccount.getFirstname(), userAccount.getLastname(),
                        new ValidationInfo(userAccount.getIdentityValidationState().name(),
                                userAccount.getIdentityValidationState().isValidated())),
                userAccount.getClass().equals(TransporterJpaEntity.class),
                documentUrlResolver.resolveUrl(userAccount.getProfileImage().getId(),
                        userAccount.getProfileImage().getHash()),
                userAccount.getDateOfBirth(), userAccount.getMiniBio(),
                new EmailInfo(userAccount.getEmail(), userAccount.getIsValidatedEmail()),
                new MobileInfo(userAccount.getMobileNumber(),
                        new IccInfo(userAccount.getIcc().getId(), userAccount.getIcc().getValue()),
                        userAccount.getIsValidatedMobileNumber()));
    }

}
