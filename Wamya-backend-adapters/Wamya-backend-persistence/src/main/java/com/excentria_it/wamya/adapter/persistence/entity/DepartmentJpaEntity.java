package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
@Table(name = "department")
@NoArgsConstructor
@SequenceGenerator(name = DepartmentJpaEntity.DEPARTMENT_SEQ)
public class DepartmentJpaEntity {

	public static final String DEPARTMENT_SEQ = "department_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DEPARTMENT_SEQ)
	private Long id;

	@Column(length = 4096)
	private String possibleNames;

	@OneToMany(mappedBy = "department", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedDepartmentJpaEntity> localizations = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "country_id")
	private CountryJpaEntity country;

	public DepartmentJpaEntity(String possibleNames, Map<String, LocalizedDepartmentJpaEntity> localizations,
			CountryJpaEntity country, Set<DelegationJpaEntity> delegations) {
		this.possibleNames = possibleNames;
		this.localizations = localizations;
		this.country = country;
		delegations.forEach(d -> this.addDelegation(d));
	}

	public String getPossibleNames() {
		return possibleNames;
	}

	public void setPossibleNames(String possibleNames) {
		this.possibleNames = possibleNames;
	}

	@OneToMany(mappedBy = "department")
	private Set<DelegationJpaEntity> delegations = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

	public CountryJpaEntity getCountry() {
		return country;
	}

	public void setCountry(CountryJpaEntity country) {
		this.country = country;
	}

	public Map<String, LocalizedDepartmentJpaEntity> getLocalizations() {
		return localizations;
	}

	public Set<DelegationJpaEntity> getDelegations() {
		return Collections.unmodifiableSet(delegations);
	}

	public void addDelegation(DelegationJpaEntity delegation) {
		if (delegation == null)
			return;
		this.delegations.add(delegation);
		delegation.setDepartment(this);
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
		DepartmentJpaEntity other = (DepartmentJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DepartmentJpaEntity [id=" + id + ", country=" + country + "]";
	}

}
