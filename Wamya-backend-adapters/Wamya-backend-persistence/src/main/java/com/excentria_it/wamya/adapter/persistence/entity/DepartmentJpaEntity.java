package com.excentria_it.wamya.adapter.persistence.entity;

import java.math.BigDecimal;
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
import javax.validation.constraints.Digits;

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
	
	@Column(scale = 6, precision = 8)
	private BigDecimal latitude;
	
	@Column(scale = 6, precision = 9)
	private BigDecimal longitude;

	public DepartmentJpaEntity(String possibleNames, Map<String, LocalizedDepartmentJpaEntity> localizations,
			CountryJpaEntity country, Set<DelegationJpaEntity> delegations, BigDecimal latitude, BigDecimal longitude) {
		this.possibleNames = possibleNames;
		this.localizations = localizations;
		this.country = country;
		delegations.forEach(d -> this.addDelegation(d));
		this.latitude = latitude;
		this.longitude = longitude;
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

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
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
		return "DepartmentJpaEntity [id=" + id + ", country=" + country + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}

}
