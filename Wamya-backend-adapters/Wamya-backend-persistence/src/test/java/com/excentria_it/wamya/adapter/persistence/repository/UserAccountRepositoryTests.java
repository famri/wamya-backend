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

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
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

	@BeforeEach
	public void cleanDatabase() {
		userAccountRepository.deleteAll();
		iccRepository.deleteAll();
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

	private void givenAUserAccountWithEmailAndMobilePhoneAndPassword() {
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultNewInternationalCallingCodeJpaEntity();

		iccEntity = iccRepository.save(iccEntity);

		UserAccountJpaEntity userAccountEntity = defaultNewTransporterJpaEntity();
		userAccountEntity.setIcc(iccEntity);

		userAccountRepository.save(userAccountEntity);

	}

}
