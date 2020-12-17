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
@Table(name = "rating_type")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = RatingTypeJpaEntity.RATING_TYPE_SEQ, initialValue = 1, allocationSize = 5)
public class RatingTypeJpaEntity {

	public static final String RATING_TYPE_SEQ = "rating_type_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RATING_TYPE_SEQ)
	private Long id;

	@Column
	private String name;

}
