package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountJpaEntity {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String internationalCallingCode;

	@Column
	private String mobileNumber;

	@Column(length = 100)
	private String password;

	@Column
	private LocalDateTime creationTimestamp;

	@Column
	private String validationCode;

	@Column
	private LocalDateTime validationTimestamp;

	@Column
	private boolean validated;

}
