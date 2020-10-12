package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.excentria_it.wamya.domain.Gender;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "user_account")
@Builder
@Data
public class UserAccountJpaEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private Boolean isTransporter;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column
	private String firstName;

	@Column
	private String lastName;

	@Column
	private Date dateOfBirth;

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

	@Column(length = 100)
	private String password;

	@Column
	private Boolean receiveNewsletter;

	@Column
	private LocalDateTime creationTimestamp;

}
