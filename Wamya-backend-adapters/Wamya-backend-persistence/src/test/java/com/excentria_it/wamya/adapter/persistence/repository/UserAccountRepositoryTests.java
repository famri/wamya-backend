package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
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
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.defaultNewTransporterJpaEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles(profiles = {"persistence-local"})
@AutoConfigureTestDatabase(replace = NONE)
public class UserAccountRepositoryTests {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private InternationalCallingCodeRepository iccRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @BeforeEach
    public void cleanDatabase() {

        userAccountRepository.deleteAll();
        iccRepository.deleteAll();
        genderRepository.deleteAll();
        documentRepository.deleteAll();
        userPreferenceRepository.deleteAll();
    }

    @Test
    public void givenAUserAccountWithOauthId_WhenFindByOauthId_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findByOauthId(user.getOauthId());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getOauthId(), entityOptional.get().getOauthId());

    }

    @Test
    public void givenAUserAccountWithEmail_WhenFindByEmail_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findByEmail(user.getEmail());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getEmail(), entityOptional.get().getEmail());

    }

    @Test
    public void givenAUserAccountWithMobilePhone_WhenFindByMobilePhoneNumber_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findByMobilePhoneNumber(
                user.getIcc().getValue(),
                user.getMobileNumber());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        assertEquals(user.getMobileNumber(), entityOptional.get().getMobileNumber());

    }


    @Test
    public void givenAUserAccountWithOAuthIdAndEmptyPreferences_WhenFindByOAuthIdWithUserPreferences_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByOauthIdWithUserPreferences(user.getOauthId());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getOauthId(), entityOptional.get().getOauthId());

    }

    @Test
    public void givenAUserAccountWithEmailAndEmptyPreferences_WhenFindByEmailWithUserPreferences_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByEmailWithUserPreferences(user.getEmail());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getEmail(), entityOptional.get().getEmail());

    }

    @Test
    public void givenAUserAccountWithMobilePhoneAndEmptyPreferences_WhenFindByMobilePhoneNumberWithUserPreferences_ThenReturnUserAccount() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByMobilePhoneNumberWithUserPreferences(user.getIcc().getValue(), user.getMobileNumber());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());

    }


    @Test
    public void givenAUserAccountWithOAuthIdAndPreferences_WhenFindByOAuthIdWithUserPreferences_ThenReturnUserAccountWithPreferences() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithExistentPreferences();


        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByOauthIdWithUserPreferences(user.getOauthId());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getOauthId(), entityOptional.get().getOauthId());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    @Test
    public void givenAUserAccountWithEmailAndEmptyPreferences_WhenFindByEmailWithUserPreferences_ThenReturnUserAccountWithPreferences() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByEmailWithUserPreferences(user.getEmail());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getEmail(), entityOptional.get().getEmail());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    @Test
    public void givenAUserAccountWithMobilePhoneAndEmptyPreferences_WhenFindByMobilePhoneNumberWithUserPreferences_ThenReturnUserAccountWithPreferences() {

        // Given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // When
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
                .findByMobilePhoneNumberWithUserPreferences(user.getIcc().getValue(), user.getMobileNumber());
        // Then
        assertTrue(entityOptional.isPresent());
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    @Test
    void givenNullSubject_WhenFindBySubject_ThenReturnEmpty() {
        // given
        givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubject(null);

        // then
        assertTrue(entityOptional.isEmpty());
    }

    @Test
    void givenEmailSubject_WhenFindBySubject_ThenReturnUserAccountEntity() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubject(user.getEmail());

        // then
        assertEquals(user.getEmail(), entityOptional.get().getEmail());
    }


    @Test
    void givenMobileNumberSubject_WhenFindBySubject_ThenReturnUserAccountEntity() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubject(user.getIcc().getValue() + "_" + user.getMobileNumber());

        // then
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        assertEquals(user.getMobileNumber(), entityOptional.get().getMobileNumber());
    }

    @Test
    void givenFrenchMobileNumberWithoutLeadingZeroSubject_WhenFindBySubject_ThenReturnUserAccountEntity() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberWithoutLeadingZeroAndOauthIdWithExistentPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubject(user.getIcc().getValue() + "_0" + user.getMobileNumber());

        // then
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        assertEquals(user.getMobileNumber(), entityOptional.get().getMobileNumber());

    }

    @Test
    void givenOauthIdSubject_WhenFindBySubject_ThenReturnUserAccountEntity() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubject(user.getOauthId());
        // then
        assertEquals(user.getOauthId(), entityOptional.get().getOauthId());
    }


    @Test
    void givennullSubject_WhenFindBySubjectWithUserPreferences_ThenReturnEmpty() {
        // given
        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubjectWithUserPreferences(null);

        // then
        assertTrue(entityOptional.isEmpty());
    }

    @Test
    void givenEmailSubject_WhenFindBySubjectWithUserPreferences_ThenReturnUserAccountEntityWithPreferences() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithExistentPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubjectWithUserPreferences(user.getEmail());

        // then
        assertEquals(user.getEmail(), entityOptional.get().getEmail());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }


    @Test
    void givenMobileNumberSubject_WhenFindBySubjectWithUserPreferences_ThenReturnUserAccountEntityWithPreferences() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithExistentPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubjectWithUserPreferences(user.getIcc().getValue() + "_" + user.getMobileNumber());

        // then
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        assertEquals(user.getMobileNumber(), entityOptional.get().getMobileNumber());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    @Test
    void givenFrenchMobileNumberWithoutLeadingZeroSubject_WhenFindBySubjectWithUserPreferences_ThenReturnUserAccountEntityWithPreferences() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberWithoutLeadingZeroAndOauthIdWithExistentPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubjectWithUserPreferences(user.getIcc().getValue() + "_0" + user.getMobileNumber());

        // then
        assertEquals(user.getIcc().getValue(), entityOptional.get().getIcc().getValue());
        assertEquals(user.getMobileNumber(), entityOptional.get().getMobileNumber());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    @Test
    void givenOauthIdSubject_WhenFindBySubjectWithUserPreferences_ThenReturnUserAccountEntityWithPreferences() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithExistentPreferences();

        // when
        Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findBySubjectWithUserPreferences(user.getOauthId());
        // then
        assertEquals(user.getOauthId(), entityOptional.get().getOauthId());
        user.getPreferences().values().forEach(pref -> assertTrue(entityOptional.get().getPreferences().get(pref.getUserPreferenceId().getKey()).getValue().equals(pref.getValue())));

    }

    private UserAccountJpaEntity givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences() {
        InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
                .defaultNewInternationalCallingCodeJpaEntity();

        iccEntity = iccRepository.save(iccEntity);

        GenderJpaEntity genderEntity = GenderJpaTestData.manGenderJpaEntity();
        genderEntity = genderRepository.save(genderEntity);

        DocumentJpaEntity defaultManProfileImage = DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity();
        defaultManProfileImage = documentRepository.save(defaultManProfileImage);

        UserAccountJpaEntity userAccountEntity = defaultNewTransporterJpaEntity();
        userAccountEntity.setIcc(iccEntity);
        userAccountEntity.setGender(genderEntity);
        userAccountEntity.setProfileImage(defaultManProfileImage);

        userAccountRepository.save(userAccountEntity);

        return userAccountEntity;

    }

    private UserAccountJpaEntity givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithExistentPreferences() {
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

        UserAccountJpaEntity savedUserAccountEntity = userAccountRepository.save(userAccountEntity);

        savedUserAccountEntity.getPreferences().forEach((k, v) -> {
            v.setUserAccount(savedUserAccountEntity);
            userPreferenceRepository.save(v);
        });

        return savedUserAccountEntity;

    }

    private UserAccountJpaEntity givenAUserAccountWithEmailAndMobileNumberWithoutLeadingZeroAndOauthIdWithExistentPreferences() {
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
        // strip leading zero from mobile number
        userAccountEntity.setMobileNumber(userAccountEntity.getMobileNumber().substring(1));

        UserAccountJpaEntity savedUserAccountEntity = userAccountRepository.save(userAccountEntity);

        savedUserAccountEntity.getPreferences().forEach((k, v) -> {
            v.setUserAccount(savedUserAccountEntity);
            userPreferenceRepository.save(v);
        });

        return savedUserAccountEntity;

    }

    @Test
    void testExistsByEmail() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsByEmail(userAccount.getEmail());

        // then
        assertTrue(userExists);
    }

    @Test
    void testExistsByOauthId() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsByOauthId(userAccount.getOauthId());

        // then
        assertTrue(userExists);
    }

    @Test
    void testExistsByIcc_ValueAndMobileNumber() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsByIcc_ValueAndMobileNumber(userAccount.getIcc().getValue(), userAccount.getMobileNumber());

        // then
        assertTrue(userExists);
    }

    @Test
    void givenUserWithFrenchMobileNumberWithoutLeadingZero_WhenExistsBySubjectIccAndMobileNumber_ThenReturnTrue() {
        // given
        UserAccountJpaEntity user = givenAUserAccountWithEmailAndMobileNumberWithoutLeadingZeroAndOauthIdWithExistentPreferences();

        // when
        boolean userExists = userAccountRepository.existsBySubject(user.getIcc().getValue() + "_0" + user.getMobileNumber());

        // then
        assertTrue(userExists);

    }

    @Test
    void testExistsBySubjectEmail() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsBySubject(userAccount.getEmail());

        // then
        assertTrue(userExists);
    }

    @Test
    void testExistsBySubjectIccAndMobileNumber() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsBySubject(userAccount.getIcc().getValue() + "_" + userAccount.getMobileNumber());

        // then
        assertTrue(userExists);
    }

    @Test
    void testExistsBySubjectOauthId() {
        // given
        final UserAccountJpaEntity userAccount = givenAUserAccountWithEmailAndMobileNumberAndOauthIdWithEmptyPreferences();

        // when
        boolean userExists = userAccountRepository.existsBySubject(userAccount.getOauthId());

        // then
        assertTrue(userExists);
    }
}
