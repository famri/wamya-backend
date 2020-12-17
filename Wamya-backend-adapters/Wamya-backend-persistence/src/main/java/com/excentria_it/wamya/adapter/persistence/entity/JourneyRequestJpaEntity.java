package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "journey_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = JourneyRequestJpaEntity.JOURNEY_REQUEST_SEQ, initialValue = 1, allocationSize = 5)
public class JourneyRequestJpaEntity {

	public static final String JOURNEY_REQUEST_SEQ = "journey_request_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = JOURNEY_REQUEST_SEQ)
	private Long id;

	@ManyToOne
	private PlaceJpaEntity departurePlace;

	@ManyToOne
	private PlaceJpaEntity arrivalPlace;

	@ManyToOne
	private EngineTypeJpaEntity engineType;

	@Column
	private Integer distance;

	private LocalDateTime dateTime;

	private LocalDateTime endDateTime;

	@Column
	private Integer workers;

	@Column(length = 500)
	private String description;

	@ManyToOne
	private UserAccountJpaEntity client;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "journey_request_id")
	private Set<JourneyProposalJpaEntity> proposals;

}
