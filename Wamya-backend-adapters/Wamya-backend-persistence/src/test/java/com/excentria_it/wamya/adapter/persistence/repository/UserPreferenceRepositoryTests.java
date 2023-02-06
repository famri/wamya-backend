package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.defaultExistentClientJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles(profiles = {"persistence-local"})
@AutoConfigureTestDatabase(replace = NONE)
public class UserPreferenceRepositoryTests {
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;


    @Autowired
    private InternationalCallingCodeRepository iccRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @BeforeEach
    public void cleanDatabase() {

        userAccountRepository.deleteAll();
        iccRepository.deleteAll();
        genderRepository.deleteAll();
        documentRepository.deleteAll();
        userPreferenceRepository.deleteAll();
    }

    @Test
    void testFindByKeyAndUserAccountEmail() {
        // given
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();

        String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();
        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountEmail(preferenceKey, userJpaEntity.getEmail());

        // then

        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());
    }

    @Test
    public void testFindByKeyAndUserAccountMobileNumber() {
        // given
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();

        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountMobileNumber(preferenceKey, userJpaEntity.getIcc().getValue(), userJpaEntity.getMobileNumber());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void testFindByKeyAndUserAccountId() {
        // given
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();

        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountId(preferenceKey, userJpaEntity.getId());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void testFindByKeyAndUserAccountOauthId() {
        // given
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();

        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountOauthId(preferenceKey, userJpaEntity.getOauthId());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void givenNullSubject_WhenFindByKeyAndUserAccountSubject_ThenReturnEmpty() {
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();
        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, null);

        // then
        assertTrue(userTimezoneOptional.isEmpty());

    }

    @Test
    void givenUserEmailSubject_WhenFindByKeyAndUserAccountSubject_ThenReturnUserPreference() {
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();
        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, userJpaEntity.getEmail());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void givenUserIccAndMobileNumberSubject_WhenFindByKeyAndUserAccountSubject_ThenReturnUserPreference() {
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();
        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, userJpaEntity.getIcc().getValue() + "_" + userJpaEntity.getMobileNumber());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void givenUserFrenchIccAndMobileNumberSubjectWithoutLeadingZero_WhenFindByKeyAndUserAccountSubject_ThenReturnUserPreference() {
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferencesAndWithoutMobileNumberLeadingZero();
        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, userJpaEntity.getIcc().getValue() + "_0" + userJpaEntity.getMobileNumber());

        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    @Test
    void givenUserOauthIdSubject_WhenFindByKeyAndUserAccountSubject_ThenReturnUserPreference() {
        UserAccountJpaEntity userJpaEntity = givenAUserAccountWithUserPreferences();
        final String preferenceKey = userJpaEntity.getPreferences().keySet().stream().findFirst().get();

        // when
        Optional<UserPreference> userTimezoneOptional = userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, userJpaEntity.getOauthId());
        // then
        assertEquals(preferenceKey, userTimezoneOptional.get().getKey());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getValue(), userTimezoneOptional.get().getValue());
        assertEquals(userJpaEntity.getPreferences().get(preferenceKey).getUserPreferenceId().getId(), userTimezoneOptional.get().getId());

    }

    private UserAccountJpaEntity givenAUserAccountWithUserPreferences() {
        InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
                .defaultNewInternationalCallingCodeJpaEntity();

        iccEntity = iccRepository.save(iccEntity);

        GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
        genderEntity = genderRepository.save(genderEntity);

        DocumentJpaEntity defaultManProfileImage = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
        defaultManProfileImage = documentRepository.save(defaultManProfileImage);

        UserAccountJpaEntity userAccountEntity = defaultExistentClientJpaEntity();


        userAccountEntity.setIcc(iccEntity);
        userAccountEntity.setGender(genderEntity);
        userAccountEntity.setProfileImage(defaultManProfileImage);

        userAccountEntity = userAccountRepository.save(userAccountEntity);

        userAccountEntity.getPreferences().forEach((preferenceKey, preferenceJpaEntity) -> {
            userPreferenceRepository.save(preferenceJpaEntity);
        });

        return userAccountEntity;

    }

    private UserAccountJpaEntity givenAUserAccountWithUserPreferencesAndWithoutMobileNumberLeadingZero() {
        InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
                .defaultNewInternationalCallingCodeJpaEntity();

        iccEntity = iccRepository.save(iccEntity);

        GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
        genderEntity = genderRepository.save(genderEntity);

        DocumentJpaEntity defaultManProfileImage = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
        defaultManProfileImage = documentRepository.save(defaultManProfileImage);

        UserAccountJpaEntity userAccountEntity = defaultExistentClientJpaEntity();


        userAccountEntity.setIcc(iccEntity);
        userAccountEntity.setGender(genderEntity);
        userAccountEntity.setProfileImage(defaultManProfileImage);
        userAccountEntity.setMobileNumber(userAccountEntity.getMobileNumber().substring(1));

        UserAccountJpaEntity savedUserAccountEntity = userAccountRepository.save(userAccountEntity);

        userAccountEntity.getPreferences().forEach((preferenceKey, preferenceJpaEntity) -> {
            preferenceJpaEntity.setUserAccount(savedUserAccountEntity);
            userPreferenceRepository.save(preferenceJpaEntity);
        });

        return savedUserAccountEntity;
    }

}
