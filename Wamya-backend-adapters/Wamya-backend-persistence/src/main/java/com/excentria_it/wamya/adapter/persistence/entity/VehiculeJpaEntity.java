package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;

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
@Table(name = "vehicule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = VehiculeJpaEntity.VEHICULE_SEQ, initialValue = 1, allocationSize = 5)
public class VehiculeJpaEntity {

	public static final String VEHICULE_SEQ = "vehicule_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = VEHICULE_SEQ)
	private Long id;

	@ManyToOne
	private EngineTypeJpaEntity type;

	@ManyToOne
	private ModelJpaEntity model;

	private LocalDate circulationDate;

	private String registration;

}
