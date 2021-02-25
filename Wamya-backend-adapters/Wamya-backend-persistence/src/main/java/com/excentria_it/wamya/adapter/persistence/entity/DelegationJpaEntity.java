package com.excentria_it.wamya.adapter.persistence.entity;

import java.math.BigDecimal;
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
@Table(name = "delegation")
@NoArgsConstructor
@SequenceGenerator(name = DelegationJpaEntity.DELEGATION_SEQ)
public class DelegationJpaEntity {
	public static final String DELEGATION_SEQ = "delegation_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DELEGATION_SEQ)
	private Long id;

	@Column(length = 4096)
	private String possibleNames;

	@OneToMany(mappedBy = "delegation", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedDelegationJpaEntity> localizations = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	@OneToMany(mappedBy = "delegation")
	private Set<LocalityJpaEntity> localities = new HashSet<>();
	
	@Column(scale = 6, precision = 8)
	private BigDecimal latitude;
	@Column(scale = 6, precision = 9)
	private BigDecimal longitude;

	public DelegationJpaEntity(String possibleNames, Map<String, LocalizedDelegationJpaEntity> localizations,
			DepartmentJpaEntity department, Set<LocalityJpaEntity> localities, BigDecimal latitude,
			BigDecimal longitude) {

		this.possibleNames = possibleNames;
		this.localizations = localizations;
		this.department = department;
		this.latitude = latitude;
		this.longitude = longitude;

		localities.forEach(l -> this.addLocality(l));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void addLocality(LocalityJpaEntity locality) {
		if (locality == null)
			return;
		this.localities.add(locality);
		locality.setDelegation(this);
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

	public DepartmentJpaEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentJpaEntity department) {
		this.department = department;
	}

	public Map<String, LocalizedDelegationJpaEntity> getLocalizations() {
		return localizations;
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
		DelegationJpaEntity other = (DelegationJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DelegationJpaEntity [id=" + id + ", department=" + department + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

}
