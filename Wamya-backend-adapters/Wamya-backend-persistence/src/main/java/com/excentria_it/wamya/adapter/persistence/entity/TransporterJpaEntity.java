package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.Gender;

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
	private Set<VehiculeJpaEntity> vehicules;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "rated_user_id")
	private Set<RatingJpaEntity> ratings;

	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Set<CommentJpaEntity> comments;

	@OneToMany(mappedBy = "transporter", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<JourneyProposalJpaEntity> proposals = new HashSet<>();

	public void addProposal(JourneyProposalJpaEntity proposal) {
		proposals.add(proposal);
		proposal.setTransporter(this);
	}

	public void removeProposal(JourneyProposalJpaEntity proposal) {
		proposals.remove(proposal);
		proposal.setTransporter(null);
	}

	public TransporterJpaEntity(Long id, Long oauthId, Gender gender, String firstname, String lastname,
			LocalDate dateOfBirth, String email, String emailValidationCode, Boolean isValidatedEmail,
			InternationalCallingCodeJpaEntity icc, String mobileNumber, String mobileNumberValidationCode,
			Boolean isValidatedMobileNumber, Boolean receiveNewsletter, LocalDateTime creationDateTime, String photoUrl,
			Double globalRating, Set<VehiculeJpaEntity> vehicules, Set<RatingJpaEntity> ratings,
			Set<CommentJpaEntity> comments, Set<JourneyProposalJpaEntity> proposals) {

		super(id, oauthId, gender, firstname, lastname, dateOfBirth, email, emailValidationCode, isValidatedEmail, icc,
				mobileNumber, mobileNumberValidationCode, isValidatedMobileNumber, receiveNewsletter, creationDateTime,
				photoUrl);

		this.globalRating = globalRating;
		this.vehicules = vehicules;
		this.ratings = ratings;
		this.comments = comments;
		this.proposals = proposals;
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
