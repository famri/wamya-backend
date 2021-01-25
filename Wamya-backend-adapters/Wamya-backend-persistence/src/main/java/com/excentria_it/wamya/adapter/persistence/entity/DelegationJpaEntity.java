package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
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
@Table(name = "delegation")
@NoArgsConstructor
@SequenceGenerator(name = DelegationJpaEntity.DELEGATION_SEQ)
public class DelegationJpaEntity {
	public static final String DELEGATION_SEQ = "delegation_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DELEGATION_SEQ)
	private Long id;

	private String possibleNames;

	@OneToMany(mappedBy = "delegation", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedDelegationJpaEntity> localizations = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	public DelegationJpaEntity(String possibleNames, Map<String, LocalizedDelegationJpaEntity> localizations,
			DepartmentJpaEntity department) {

		this.possibleNames = possibleNames;
		this.localizations = localizations;
		this.department = department;
	}

	public Long getId() {
		return id;
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
		return "DelegationJpaEntity [id=" + id + ", possibleNames=" + possibleNames + ", department=" + department
				+ "]";
	}

}
