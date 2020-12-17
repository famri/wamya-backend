package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;

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
@Table(name = "journey_proposal")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@SequenceGenerator(name = JourneyProposalJpaEntity.JOURNEY_PROPOSAL_SEQ)
public class JourneyProposalJpaEntity {

	public static final String JOURNEY_PROPOSAL_SEQ = "journey_proposal_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = JOURNEY_PROPOSAL_SEQ)
	private Long id;

	@Column
	private Integer price;

	private LocalDateTime creationDateTime;

	@ManyToOne
	private UserAccountJpaEntity transporter;
}
