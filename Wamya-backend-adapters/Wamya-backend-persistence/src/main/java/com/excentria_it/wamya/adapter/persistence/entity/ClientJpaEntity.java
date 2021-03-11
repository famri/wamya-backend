package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
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
	private Set<JourneyRequestJpaEntity> journeyRequests = new HashSet<>();

	public void addJourneyRequest(JourneyRequestJpaEntity journeyRequest) {
		journeyRequests.add(journeyRequest);
		journeyRequest.setClient(this);
	}

	public void removeJourneyRequest(JourneyRequestJpaEntity journeyRequest) {
		journeyRequests.remove(journeyRequest);
		journeyRequest.setClient(null);
	}

	public ClientJpaEntity(Long id, Long oauthId, Gender gender, String firstname, String lastname,
			LocalDate dateOfBirth, String email, String emailValidationCode, Boolean isValidatedEmail,
			InternationalCallingCodeJpaEntity icc, String mobileNumber, String mobileNumberValidationCode,
			Boolean isValidatedMobileNumber, Boolean receiveNewsletter, Instant creationDateTime, String photoUrl,
			Map<String, UserPreferenceJpaEntity> preferences) {

		super(id, oauthId, gender, firstname, lastname, dateOfBirth, email, emailValidationCode, isValidatedEmail, icc,
				mobileNumber, mobileNumberValidationCode, isValidatedMobileNumber, receiveNewsletter, creationDateTime,
				photoUrl, preferences);

	}

	public Set<JourneyRequestJpaEntity> getJourneyRequests() {
		return Collections.unmodifiableSet(journeyRequests);
	}

	@Override
	public int hashCode() {

		int result = super.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "ClientJpaEntity [" + super.toString() + "journeyRequests=" + journeyRequests + "]";
	}

}
