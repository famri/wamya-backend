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
@Table(name = "vehicule_constructor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = ConstructorJpaEntity.CONSTRUCTOR_SEQ, initialValue = 1, allocationSize = 5)
public class ConstructorJpaEntity {

	public static final String CONSTRUCTOR_SEQ = "vehicule_constructor_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CONSTRUCTOR_SEQ)
	private Long id;

	@Column
	private String name;
}
