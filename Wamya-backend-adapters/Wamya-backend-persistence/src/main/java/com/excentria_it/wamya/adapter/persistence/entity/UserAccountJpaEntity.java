package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "user_account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = UserAccountJpaEntity.USER_SEQ, initialValue = 1, allocationSize = 5)
public class UserAccountJpaEntity {

	public static final String USER_SEQ = "user_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQ)
	private Long id;

	private Long oauthId;

	private Boolean isTransporter;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String firstname;

	private String lastname;

	private LocalDate dateOfBirth;

	private String email;

	private String emailValidationCode;

	private Boolean isValidatedEmail;

	@ManyToOne
	private InternationalCallingCodeJpaEntity icc;

	private String mobileNumber;

	private String mobileNumberValidationCode;

	private Boolean isValidatedMobileNumber;

	private Boolean receiveNewsletter;

	private LocalDateTime creationDateTime;

	private String photoUrl;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private Set<VehiculeJpaEntity> vehicules;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "transporter_id")
	private Set<RatingJpaEntity> ratings;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "transporter_id")
	private Set<CommentJpaEntity> comments;

	public Long getOauthId() {
		return oauthId;
	}

	public void setOauthId(Long oauthId) {
		this.oauthId = oauthId;
	}

	public Boolean getIsTransporter() {
		return isTransporter;
	}

	public void setIsTransporter(Boolean isTransporter) {
		this.isTransporter = isTransporter;
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

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Set<VehiculeJpaEntity> getVehicules() {
		return vehicules;
	}

	public void setVehicules(Set<VehiculeJpaEntity> vehicules) {
		this.vehicules = vehicules;
	}

	public Set<RatingJpaEntity> getRatings() {
		return ratings;
	}

	public void setRatings(Set<RatingJpaEntity> ratings) {
		this.ratings = ratings;
	}

	public Set<CommentJpaEntity> getComments() {
		return comments;
	}

	public void setComments(Set<CommentJpaEntity> comments) {
		this.comments = comments;
	}

	public Long getId() {
		return id;
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
		return "UserAccountJpaEntity [id=" + id + ", oauthId=" + oauthId + ", isTransporter=" + isTransporter
				+ ", gender=" + gender + ", firstname=" + firstname + ", lastname=" + lastname + ", dateOfBirth="
				+ dateOfBirth + ", email=" + email + ", emailValidationCode=" + emailValidationCode
				+ ", isValidatedEmail=" + isValidatedEmail + ", icc=" + icc + ", mobileNumber=" + mobileNumber
				+ ", mobileNumberValidationCode=" + mobileNumberValidationCode + ", isValidatedMobileNumber="
				+ isValidatedMobileNumber + ", receiveNewsletter=" + receiveNewsletter + ", creationDateTime="
				+ creationDateTime + ", photoUrl=" + photoUrl + "]";
	}

}
