package com.excentria_it.wamya.adapter.persistence.repository;

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
import com.excentria_it.wamya.test.data.common.InternationalCallingCodeJpaEntityTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class InternationalCallingCodeRepositoryTests {
	@Autowired
	private InternationalCallingCodeRepository iccRepository;

	@BeforeEach
	public void cleanDatabase() {
		iccRepository.deleteAll();
	}

	@Test
	public void givenAnInternationalCallingCode_WhenFindByValue_ThenReturnInternationalCallingCode() {

		// Given
		givenAnInternationalCallingCode();

		// When
		Optional<InternationalCallingCodeJpaEntity> entityOptional = iccRepository
				.findByValue(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE);
		// Then
		assertTrue(entityOptional.isPresent());
		assertEquals(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, entityOptional.get().getValue());

	}

	private void givenAnInternationalCallingCode() {
		InternationalCallingCodeJpaEntity iccEntity = InternationalCallingCodeJpaEntityTestData
				.defaultNewInternationalCallingCodeJpaEntity();

		iccEntity = iccRepository.save(iccEntity);

	}
}
