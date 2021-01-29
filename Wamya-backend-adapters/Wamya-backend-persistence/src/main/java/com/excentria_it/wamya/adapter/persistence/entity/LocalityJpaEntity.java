package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "locality")
@NoArgsConstructor
@SequenceGenerator(name = LocalityJpaEntity.LOCALITY_SEQ)
public class LocalityJpaEntity {
	public static final String LOCALITY_SEQ = "locality_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = LOCALITY_SEQ)
	private Long id;

	@Column(length = 4096)
	private String possibleNames;

	@OneToMany(mappedBy = "locality", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedLocalityJpaEntity> localizations = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "delegation_id")
	private DelegationJpaEntity delegation;

	public LocalityJpaEntity(String possibleNames, Map<String, LocalizedLocalityJpaEntity> localizations,
			DelegationJpaEntity delegation) {
		super();
		this.possibleNames = possibleNames;
		this.localizations = localizations;
		this.delegation = delegation;
	}

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

	public String getPossibleNames() {
		return possibleNames;
	}

	public void setPossibleNames(String possibleNames) {
		this.possibleNames = possibleNames;
	}

	public Map<String, LocalizedLocalityJpaEntity> getLocalizations() {
		return localizations;
	}

	public void setLocalizations(Map<String, LocalizedLocalityJpaEntity> localizations) {
		this.localizations = localizations;
	}

	public DelegationJpaEntity getDelegation() {
		return delegation;
	}

	public void setDelegation(DelegationJpaEntity delegation) {
		this.delegation = delegation;
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
		LocalityJpaEntity other = (LocalityJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalityJpaEntity [id=" + id + ", delegation=" + delegation + "]";
	}

}
