package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.Gender;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "user_account")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor

@SequenceGenerator(name = UserAccountJpaEntity.USER_SEQ, initialValue = 1, allocationSize = 5)
public abstract class UserAccountJpaEntity {

	public static final String USER_SEQ = "user_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQ)
	protected Long id;

	protected Long oauthId;

	@Enumerated(EnumType.STRING)
	protected Gender gender;

	protected String firstname;

	protected String lastname;

	protected LocalDate dateOfBirth;

	protected String email;

	protected String emailValidationCode;

	protected Boolean isValidatedEmail;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "icc_id")
	protected InternationalCallingCodeJpaEntity icc;

	protected String mobileNumber;

	protected String mobileNumberValidationCode;

	protected Boolean isValidatedMobileNumber;

	protected Boolean receiveNewsletter;

	protected Instant creationDateTime;

	protected String photoUrl;

	@OneToMany(mappedBy = "userAccount", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "userPreferenceId.key")
	protected Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();

	public UserAccountJpaEntity(Long id, Long oauthId, Gender gender, String firstname, String lastname,
			LocalDate dateOfBirth, String email, String emailValidationCode, Boolean isValidatedEmail,
			InternationalCallingCodeJpaEntity icc, String mobileNumber, String mobileNumberValidationCode,
			Boolean isValidatedMobileNumber, Boolean receiveNewsletter, Instant creationDateTime, String photoUrl,
			Map<String, UserPreferenceJpaEntity> preferences) {
		super();
		this.id = id;
		this.oauthId = oauthId;
		this.gender = gender;
		this.firstname = firstname;
		this.lastname = lastname;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.emailValidationCode = emailValidationCode;
		this.isValidatedEmail = isValidatedEmail;
		this.icc = icc;
		this.mobileNumber = mobileNumber;
		this.mobileNumberValidationCode = mobileNumberValidationCode;
		this.isValidatedMobileNumber = isValidatedMobileNumber;
		this.receiveNewsletter = receiveNewsletter;
		this.creationDateTime = creationDateTime;
		this.photoUrl = photoUrl;
		if (preferences != null) {
			preferences.forEach((k, v) -> v.setUserAccount(this));
		}

		this.preferences = preferences;
	}

	public String getPreferenceValue(String key) {
		return this.preferences.get(key).getValue();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOauthId() {
		return oauthId;
	}

	public void setOauthId(Long oauthId) {
		this.oauthId = oauthId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailValidationCode() {
		return emailValidationCode;
	}

	public void setEmailValidationCode(String emailValidationCode) {
		this.emailValidationCode = emailValidationCode;
	}

	public Boolean getIsValidatedEmail() {
		return isValidatedEmail;
	}

	public void setIsValidatedEmail(Boolean isValidatedEmail) {
		this.isValidatedEmail = isValidatedEmail;
	}

	public InternationalCallingCodeJpaEntity getIcc() {
		return icc;
	}

	public void setIcc(InternationalCallingCodeJpaEntity icc) {
		this.icc = icc;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMobileNumberValidationCode() {
		return mobileNumberValidationCode;
	}

	public void setMobileNumberValidationCode(String mobileNumberValidationCode) {
		this.mobileNumberValidationCode = mobileNumberValidationCode;
	}

	public Boolean getIsValidatedMobileNumber() {
		return isValidatedMobileNumber;
	}

	public void setIsValidatedMobileNumber(Boolean isValidatedMobileNumber) {
		this.isValidatedMobileNumber = isValidatedMobileNumber;
	}

	public Boolean getReceiveNewsletter() {
		return receiveNewsletter;
	}

	public void setReceiveNewsletter(Boolean receiveNewsletter) {
		this.receiveNewsletter = receiveNewsletter;
	}

	public Instant getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Instant creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Map<String, UserPreferenceJpaEntity> getPreferences() {
		return preferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccountJpaEntity other = (UserAccountJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserAccountJpaEntity [id=" + id + ", oauthId=" + oauthId + ", gender=" + gender + ", firstname="
				+ firstname + ", lastname=" + lastname + ", dateOfBirth=" + dateOfBirth + ", email=" + email
				+ ", emailValidationCode=" + emailValidationCode + ", isValidatedEmail=" + isValidatedEmail + ", icc="
				+ icc + ", mobileNumber=" + mobileNumber + ", mobileNumberValidationCode=" + mobileNumberValidationCode
				+ ", isValidatedMobileNumber=" + isValidatedMobileNumber + ", receiveNewsletter=" + receiveNewsletter
				+ ", creationDateTime=" + creationDateTime + ", photoUrl=" + photoUrl + "]";
	}

}
