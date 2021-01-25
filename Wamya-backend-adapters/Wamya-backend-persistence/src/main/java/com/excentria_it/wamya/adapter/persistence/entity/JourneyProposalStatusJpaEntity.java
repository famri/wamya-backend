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
@Table(name = "journey_proposal_status")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = JourneyProposalStatusJpaEntity.JOURNEY_PROPOSAL_STATUS_SEQ)
public class JourneyProposalStatusJpaEntity {

	public static final String JOURNEY_PROPOSAL_STATUS_SEQ = "journey_proposal_status_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = JOURNEY_PROPOSAL_STATUS_SEQ)
	private Long id;

	@Enumerated(EnumType.STRING)
	private JourneyProposalStatusCode code;

	private String description;

	@Builder.Default
	@OneToMany(mappedBy = "status", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedJourneyProposalStatusJpaEntity> localizations = new HashMap<>();

	public String getValue(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getValue();
		}
		return null;
	}

	public JourneyProposalStatusCode getCode() {
		return code;
	}

	public void setCode(JourneyProposalStatusCode code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public Map<String, LocalizedJourneyProposalStatusJpaEntity> getLocalizations() {
		return localizations;
	}

	public enum JourneyProposalStatusCode {
		SUBMITTED, CANCELED, REJECTED, ACCEPTED;
	}

}
