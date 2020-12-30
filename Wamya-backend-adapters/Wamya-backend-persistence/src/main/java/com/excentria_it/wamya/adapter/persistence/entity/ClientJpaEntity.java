package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.Gender;

import lombok.NoArgsConstructor;
@Generated
@Table(name = "client")
@Entity
@NoArgsConstructor
public class ClientJpaEntity extends UserAccountJpaEntity {

	@OneToMany(mappedBy = "client", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<JourneyRequestJpaEntity> journeyRequests;

	public ClientJpaEntity(Long id, Long oauthId, Gender gender, String firstname, String lastname,
			LocalDate dateOfBirth, String email, String emailValidationCode, Boolean isValidatedEmail,
			InternationalCallingCodeJpaEntity icc, String mobileNumber, String mobileNumberValidationCode,
			Boolean isValidatedMobileNumber, Boolean receiveNewsletter, LocalDateTime creationDateTime, String photoUrl,
			Set<JourneyRequestJpaEntity> journeyRequests) {

		super(id, oauthId, gender, firstname, lastname, dateOfBirth, email, emailValidationCode, isValidatedEmail, icc,
				mobileNumber, mobileNumberValidationCode, isValidatedMobileNumber, receiveNewsletter, creationDateTime,
				photoUrl);

		this.journeyRequests = journeyRequests;

	}
}
