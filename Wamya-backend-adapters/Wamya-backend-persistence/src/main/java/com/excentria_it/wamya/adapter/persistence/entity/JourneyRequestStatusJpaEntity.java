package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "journey_request_status")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = JourneyRequestStatusJpaEntity.JOURNEY_REQUEST_STATUS_SEQ)
public class JourneyRequestStatusJpaEntity {

	public static final String JOURNEY_REQUEST_STATUS_SEQ = "journey_request_status_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = JOURNEY_REQUEST_STATUS_SEQ)
	private Long id;

	@Enumerated(EnumType.STRING)
	private JourneyRequestStatusCode code;

	private String description;

	@OneToMany(mappedBy = "status", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedJourneyRequestStatusJpaEntity> localizations = new HashMap<>();

	public String getValue(String locale) {
		return localizations.get(locale).getValue();
	}

	public Long getId() {
		return id;
	}

	public Map<String, LocalizedJourneyRequestStatusJpaEntity> getLocalizations() {
		return localizations;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

		JourneyRequestStatusJpaEntity other = (JourneyRequestStatusJpaEntity) obj;

		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JourneyRequestStatusJpaEntity [id=" + id + ", code=" + code + ", description=" + description + "]";
	}

	public enum JourneyRequestStatusCode {
		OPENED, CANCELED, EXPIRED, FULFILLED;
	}
}
