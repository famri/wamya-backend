package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicule_model")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = ModelJpaEntity.MODEL_SEQ, initialValue = 1, allocationSize = 5)
public class ModelJpaEntity {

	public static final String MODEL_SEQ = "vehicule_model_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MODEL_SEQ)
	private Long id;

	@ManyToOne
	private ConstructorJpaEntity Constructor;

	private String name;
}
