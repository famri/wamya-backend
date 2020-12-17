package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Column;
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
@Table(name = "user_rating")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = RatingJpaEntity.RATING_SEQ, initialValue = 1, allocationSize = 5)
public class RatingJpaEntity {

	public static final String RATING_SEQ = "rating_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RATING_SEQ)
	private Long id;

	@ManyToOne
	private RatingTypeJpaEntity type;

	@Column
	private Double value;

}
