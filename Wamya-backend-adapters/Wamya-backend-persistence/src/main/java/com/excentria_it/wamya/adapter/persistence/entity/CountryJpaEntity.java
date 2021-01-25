package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "country", uniqueConstraints = @UniqueConstraint(columnNames = { "code" }))
@NoArgsConstructor
@SequenceGenerator(name = CountryJpaEntity.COUNTRY_SEQ)
public class CountryJpaEntity {

	public static final String COUNTRY_SEQ = "country_seq";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = COUNTRY_SEQ)
	private Long id;

	@OneToMany(mappedBy = "country", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedCountryJpaEntity> localizations = new HashMap<>();

	private String code;

	public CountryJpaEntity(Map<String, LocalizedCountryJpaEntity> localizations, String name, String code,
			Set<DepartmentJpaEntity> departments) {

		this.localizations = localizations;
		this.code = code;
		this.departments = departments;
	}

	@OneToMany(mappedBy = "country")
	private Set<DepartmentJpaEntity> departments = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

	public Set<DepartmentJpaEntity> getDepartments() {
		return Collections.unmodifiableSet(departments);
	}

	public void addDepartment(DepartmentJpaEntity department) {
		this.departments.add(department);
		department.setCountry(this);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, LocalizedCountryJpaEntity> getLocalizations() {
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
		CountryJpaEntity other = (CountryJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CountryJpaEntity [id=" + id + ", code=" + code + "]";
	}

}
