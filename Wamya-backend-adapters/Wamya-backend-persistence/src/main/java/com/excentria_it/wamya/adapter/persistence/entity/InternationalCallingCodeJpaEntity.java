package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "international_calling_code")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InternationalCallingCodeJpaEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String value;

	@Column
	private String countryName;

	@Column(length = 255)
	private String flagPath;

	@Column
	private boolean enabled;
}
