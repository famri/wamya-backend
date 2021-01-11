package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "journey_request")
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

	private Double distance;

	private Instant dateTime;

	private Instant endDateTime;

	private Integer workers;

	@Column(length = 500)
	private String description;

	private Instant creationDateTime;

	@ManyToOne
	private JourneyRequestStatusJpaEntity status;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private ClientJpaEntity client;

	@OneToMany(mappedBy = "journeyRequest", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<JourneyProposalJpaEntity> proposals = new HashSet<>();

	public void addProposal(JourneyProposalJpaEntity proposal) {
		proposals.add(proposal);
		proposal.setJourneyRequest(this);
	}

	public void removeProposal(JourneyProposalJpaEntity proposal) {
		proposals.remove(proposal);
		proposal.setJourneyRequest(null);
	}

	public Long getId() {
		return id;
	}

	public PlaceJpaEntity getDeparturePlace() {
		return departurePlace;
	}

	public void setDeparturePlace(PlaceJpaEntity departurePlace) {
		this.departurePlace = departurePlace;
	}

	public PlaceJpaEntity getArrivalPlace() {
		return arrivalPlace;
	}

	public void setArrivalPlace(PlaceJpaEntity arrivalPlace) {
		this.arrivalPlace = arrivalPlace;
	}

	public EngineTypeJpaEntity getEngineType() {
		return engineType;
	}

	public void setEngineType(EngineTypeJpaEntity engineType) {
		this.engineType = engineType;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Instant getDateTime() {
		return dateTime;
	}

	public void setDateTime(Instant dateTime) {
		this.dateTime = dateTime;
	}

	public Instant getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Instant endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Integer getWorkers() {
		return workers;
	}

	public void setWorkers(Integer workers) {
		this.workers = workers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Instant getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Instant creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public ClientJpaEntity getClient() {
		return client;
	}

	public void setClient(ClientJpaEntity client) {
		this.client = client;
	}

	public Set<JourneyProposalJpaEntity> getProposals() {
		return Collections.unmodifiableSet(proposals);
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
		JourneyRequestJpaEntity other = (JourneyRequestJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JourneyRequestJpaEntity [id=" + id + ", departurePlace=" + departurePlace + ", arrivalPlace="
				+ arrivalPlace + ", engineType=" + engineType + ", distance=" + distance + ", dateTime=" + dateTime
				+ ", endDateTime=" + endDateTime + ", workers=" + workers + ", description=" + description
				+ ", creationDateTime=" + creationDateTime + ", client id=" + client.getId() + "]";
	}

	public JourneyRequestStatusJpaEntity getStatus() {
		return status;
	}

	public void setStatus(JourneyRequestStatusJpaEntity status) {
		this.status = status;
	}
}
