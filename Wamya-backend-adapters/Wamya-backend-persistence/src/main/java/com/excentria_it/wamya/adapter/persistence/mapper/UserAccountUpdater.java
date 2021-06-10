package com.excentria_it.wamya.adapter.persistence.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.GenderRepository;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.common.exception.GenderNotFoundException;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCodeException;
import com.excentria_it.wamya.domain.ValidationState;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAccountUpdater {

	private final GenderRepository genderRepository;
	private final InternationalCallingCodeRepository iccRepository;

	public UserAccountJpaEntity updateAboutSection(UserAccountJpaEntity userAccountJpaEntity, Long genderId,
			String firstname, String lastname, LocalDate dateOfBirth, String minibio) {

		if (userAccountJpaEntity.getGender() == null || !userAccountJpaEntity.getGender().getId().equals(genderId)) {
			GenderJpaEntity gender = genderRepository.findById(genderId).orElseThrow(
					() -> new GenderNotFoundException(String.format("Gender not found by ID: %d", genderId)));

			userAccountJpaEntity.setGender(gender);
		}

		if (userAccountJpaEntity.getFirstname() == null || !userAccountJpaEntity.getFirstname().equals(firstname)) {
			userAccountJpaEntity.setFirstname(firstname);
			userAccountJpaEntity.setIdentityValidationState(ValidationState.NOT_VALIDATED);
		}

		if (userAccountJpaEntity.getLastname() == null || !userAccountJpaEntity.getLastname().equals(lastname)) {
			userAccountJpaEntity.setLastname(lastname);
			userAccountJpaEntity.setIdentityValidationState(ValidationState.NOT_VALIDATED);
		}

		if (userAccountJpaEntity.getDateOfBirth() == null
				|| !userAccountJpaEntity.getDateOfBirth().equals(dateOfBirth)) {
			userAccountJpaEntity.setDateOfBirth(dateOfBirth);
		}

		if (userAccountJpaEntity.getMiniBio() == null || !userAccountJpaEntity.getMiniBio().equals(minibio)) {
			userAccountJpaEntity.setMiniBio(minibio);
		}

		return userAccountJpaEntity;
	}

	public UserAccountJpaEntity updateEmailSection(UserAccountJpaEntity userAccountJpaEntity, String email) {

		if (userAccountJpaEntity.getEmail() == null || !userAccountJpaEntity.getEmail().equals(email)) {
			userAccountJpaEntity.setEmail(email);
			userAccountJpaEntity.setIsValidatedEmail(false);
		}

		return userAccountJpaEntity;
	}

	public UserAccountJpaEntity updateMobileSection(UserAccountJpaEntity userAccountJpaEntity, String mobileNumber,
			Long iccId) {

		if (userAccountJpaEntity.getMobileNumber() == null
				|| !userAccountJpaEntity.getMobileNumber().equals(mobileNumber)) {
			userAccountJpaEntity.setMobileNumber(mobileNumber);
			userAccountJpaEntity.setIsValidatedMobileNumber(false);
		}

		if (userAccountJpaEntity.getIcc() == null || !userAccountJpaEntity.getIcc().getId().equals(iccId)) {

			InternationalCallingCodeJpaEntity icc = iccRepository.findById(iccId)
					.orElseThrow(() -> new UnsupportedInternationalCallingCodeException(
							String.format("Unsupported international calling code ID: %d", iccId)));

			userAccountJpaEntity.setIcc(icc);
		}

		return userAccountJpaEntity;
	}

}
