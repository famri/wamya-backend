package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "journey_proposal")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = JourneyProposalJpaEntity.JOURNEY_PROPOSAL_SEQ)
public class JourneyProposalJpaEntity {

	public static final String JOURNEY_PROPOSAL_SEQ = "journey_proposal_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = JOURNEY_PROPOSAL_SEQ)
	private Long id;

	private Integer price;

	private LocalDateTime creationDateTime;

	@ManyToOne
	private UserAccountJpaEntity transporter;

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public UserAccountJpaEntity getTransporter() {
		return transporter;
	}

	public void setTransporter(UserAccountJpaEntity transporter) {
		this.transporter = transporter;
	}

	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JourneyProposalJpaEntity other = (JourneyProposalJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JourneyProposalJpaEntity [id=" + id + ", price=" + price + ", creationDateTime=" + creationDateTime
				+ ", transporter id=" + transporter.getId() + "]";
	}

}
