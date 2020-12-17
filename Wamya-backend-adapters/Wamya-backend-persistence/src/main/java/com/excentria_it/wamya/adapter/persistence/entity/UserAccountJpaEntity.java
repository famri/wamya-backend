package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.domain.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = UserAccountJpaEntity.USER_SEQ, initialValue = 1, allocationSize = 5)
public class UserAccountJpaEntity {

	public static final String USER_SEQ = "user_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQ)
	private Long id;

	@Column
	private Long oauthId;

	@Column
	private Boolean isTransporter;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column
	private String firstname;

	@Column
	private String lastname;

	@Column
	private LocalDate dateOfBirth;

	@Column
	private String email;

	@Column
	private String emailValidationCode;

	@Column
	private Boolean isValidatedEmail;

	@ManyToOne
	private InternationalCallingCodeJpaEntity icc;

	@Column
	private String mobileNumber;

	@Column
	private String mobileNumberValidationCode;

	@Column
	private Boolean isValidatedMobileNumber;

	@Column
	private Boolean receiveNewsletter;

	@Column
	private LocalDateTime creationDateTime;

	@Column
	private String photoUrl;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "owner_id")
	private Set<VehiculeJpaEntity> vehicules;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "transporter_id")
	private Set<RatingJpaEntity> ratings;

	@OneToMany(orphanRemoval = true)
	@JoinColumn(name = "transporter_id")
	private Set<CommentJpaEntity> comments;

}
