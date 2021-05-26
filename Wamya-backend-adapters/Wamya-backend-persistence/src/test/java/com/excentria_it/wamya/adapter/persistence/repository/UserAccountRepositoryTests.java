package com.excentria_it.wamya.adapter.persistence.repository;

import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.DocumentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.test.data.common.DocumentJpaTestData;
import com.excentria_it.wamya.test.data.common.GenderJpaTestData;
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
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

	@BeforeEach
	public void cleanDatabase() {
		
		userAccountRepository.deleteAll();
		iccRepository.deleteAll();
		genderRepository.deleteAll();
		documentRepository.deleteAll();
	}

	@Test
	public void givenAUserAccountWithMobilePhone_WhenFindByMobilePhoneNumber_ThenReturnUserAccount() {

		// Given
		givenAUserAccountWithEmailAndMobilePhoneAndPassword();

		// When
		Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findByMobilePhoneNumber(
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity().getValue(),
				TestConstants.DEFAULT_MOBILE_NUMBER);
		// Then
		assertTrue(entityOptional.isPresent());
		assertEquals(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, entityOptional.get().getIcc().getValue());

	}

	@Test
	public void givenAUserAccountWithEmail_WhenFindByEmail_ThenReturnUserAccount() {

		// Given
		givenAUserAccountWithEmailAndMobilePhoneAndPassword();

		// When
		Optional<UserAccountJpaEntity> entityOptional = userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL);
		// Then
		assertTrue(entityOptional.isPresent());
		assertEquals(TestConstants.DEFAULT_EMAIL, entityOptional.get().getEmail());

	}

	@Test
	public void givenAUserAccountWithEmailAndEmptyPreferences_WhenFindByEmailWithUserPreferences_ThenReturnUserAccount() {

		// Given
		givenAUserAccountWithEmailAndMobilePhoneAndPassword();

		// When
		Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
				.findByEmailWithUserPreferences(TestConstants.DEFAULT_EMAIL);
		// Then
		assertTrue(entityOptional.isPresent());
		assertEquals(TestConstants.DEFAULT_EMAIL, entityOptional.get().getEmail());

	}

	@Test
	public void givenAUserAccountWithMobilePhoneAndEmptyPreferences_WhenFindByMobilePhoneNumberWithUserPreferences_ThenReturnUserAccount() {

		// Given
		givenAUserAccountWithEmailAndMobilePhoneAndPassword();

		// When
		Optional<UserAccountJpaEntity> entityOptional = userAccountRepository
				.findByMobilePhoneNumberWithUserPreferences(InternationalCallingCodeJpaEntityTestData
						.defaultExistentInternationalCallingCodeJpaEntity().getValue(),
						TestConstants.DEFAULT_MOBILE_NUMBER);
		// Then
		assertTrue(entityOptional.isPresent());
		assertEquals(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, entityOptional.get().getIcc().getValue());

	}

	private void givenAUserAccountWithEmailAndMobilePhoneAndPassword() {
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

	}

}
