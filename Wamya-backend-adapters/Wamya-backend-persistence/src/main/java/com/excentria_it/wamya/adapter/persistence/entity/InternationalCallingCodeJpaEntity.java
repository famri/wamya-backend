package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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
@SequenceGenerator(name = "icc_seq", initialValue = 3, allocationSize = 5)
public class InternationalCallingCodeJpaEntity {
	public static final String ICC_SEQ = "icc_seq";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ICC_SEQ)
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
