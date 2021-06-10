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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.ValidationState;

import lombok.NoArgsConstructor;

@Generated
@Table(name = "transporter")
@Entity
@NoArgsConstructor
public class TransporterJpaEntity extends UserAccountJpaEntity {

	private Double globalRating;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private Set<VehiculeJpaEntity> vehicules = new HashSet<>();

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "rated_user_id")
	private Set<RatingJpaEntity> ratings = new HashSet<>();

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Set<CommentJpaEntity> comments = new HashSet<>();

	@OneToMany(mappedBy = "transporter", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<JourneyProposalJpaEntity> proposals = new HashSet<>();

	public void addProposal(JourneyProposalJpaEntity proposal) {
		proposals.add(proposal);
		proposal.setTransporter(this);
	}

	public void removeVehicule(VehiculeJpaEntity vehicule) {
		vehicules.remove(vehicule);
	}

	public void addVehicule(VehiculeJpaEntity vehicule) {
		vehicules.add(vehicule);
	}

	public void removeProposal(JourneyProposalJpaEntity proposal) {
		proposals.remove(proposal);
		proposal.setTransporter(null);
	}

	public TransporterJpaEntity(Long id, Long oauthId, GenderJpaEntity gender, String firstname, String lastname,
			ValidationState identityValidationState, String minibio, LocalDate dateOfBirth, String email,
			String emailValidationCode, Boolean isValidatedEmail, InternationalCallingCodeJpaEntity icc,
			String mobileNumber, String mobileNumberValidationCode, Boolean isValidatedMobileNumber,
			Boolean receiveNewsletter, Instant creationDateTime, DocumentJpaEntity profileImage,
			Map<String, UserPreferenceJpaEntity> preferences, DocumentJpaEntity identityDocument) {

		super(id, oauthId, gender, firstname, lastname, identityValidationState, minibio, dateOfBirth, email, emailValidationCode, isValidatedEmail, icc,
				mobileNumber, mobileNumberValidationCode, isValidatedMobileNumber, receiveNewsletter, creationDateTime,
				profileImage, preferences, identityDocument);

	}

	public Set<VehiculeJpaEntity> getVehicules() {
		return Collections.unmodifiableSet(vehicules);

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

	public Double getGlobalRating() {
		return globalRating;
	}

	public void setGlobalRating(Double globalRating) {
		this.globalRating = globalRating;
	}

	public Set<JourneyProposalJpaEntity> getProposals() {
		return Collections.unmodifiableSet(proposals);
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
		return "TransporterJpaEntity [" + super.toString() + ", globalRating=" + globalRating + "]";
	}

}
