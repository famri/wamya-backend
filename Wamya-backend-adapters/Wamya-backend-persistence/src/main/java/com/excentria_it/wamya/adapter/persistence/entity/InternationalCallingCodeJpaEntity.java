package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "international_calling_code", uniqueConstraints = @UniqueConstraint(columnNames = { "id", "value",
		"countryName", "flagPath" }))
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SequenceGenerator(name = "icc_seq", initialValue = 3, allocationSize = 1)
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
