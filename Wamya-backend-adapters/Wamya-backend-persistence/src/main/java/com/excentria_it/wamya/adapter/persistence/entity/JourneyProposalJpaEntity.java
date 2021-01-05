package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

	private Double price;

	private LocalDateTime creationDateTime;

	@ManyToOne
	@JoinColumn(name = "transporter_id")
	private TransporterJpaEntity transporter;

	@ManyToOne
	@JoinColumn(name = "vehicule_id")
	private VehiculeJpaEntity vehicule;

	@ManyToOne
	@JoinColumn(name = "journey_request_id")
	private JourneyRequestJpaEntity journeyRequest;

	@ManyToOne
	private JourneyProposalStatusJpaEntity status;


	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public LocalDateTime getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(LocalDateTime creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public TransporterJpaEntity getTransporter() {
		return transporter;
	}

	public void setTransporter(TransporterJpaEntity transporter) {
		this.transporter = transporter;
	}

	public Long getId() {
		return id;
	}

	public VehiculeJpaEntity getVehicule() {
		return vehicule;
	}

	public void setVehicule(VehiculeJpaEntity vehicule) {
		this.vehicule = vehicule;
	}

	public JourneyRequestJpaEntity getJourneyRequest() {
		return journeyRequest;
	}

	public void setJourneyRequest(JourneyRequestJpaEntity journeyRequest) {
		this.journeyRequest = journeyRequest;
	}

	public JourneyProposalStatusJpaEntity getStatus() {
		return status;
	}

	public void setStatus(JourneyProposalStatusJpaEntity status) {
		this.status = status;
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
				+ ", transporter firstname=" + transporter.getFirstname() + ", vehicule  =" + vehicule.toString()
				+ ", journeyRequest=" + journeyRequest.toString() + "]";
	}

}
