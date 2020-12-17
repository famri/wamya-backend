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
@Table(name = "engine_type")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = EngineTypeJpaEntity.ENGINE_TYPE_SEQ, initialValue = 1, allocationSize = 5)
public class EngineTypeJpaEntity {

	public static final String ENGINE_TYPE_SEQ = "engine_type_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENGINE_TYPE_SEQ)
	private Long id;

	@Column
	private String name;
}
