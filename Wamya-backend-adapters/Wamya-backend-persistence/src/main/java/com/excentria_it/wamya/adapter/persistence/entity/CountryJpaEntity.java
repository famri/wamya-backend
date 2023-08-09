package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "country", uniqueConstraints = @UniqueConstraint(columnNames = { "code", "flagPath" }))
@NoArgsConstructor
@SequenceGenerator(name = CountryJpaEntity.COUNTRY_SEQ)
public class CountryJpaEntity {

	public static final String COUNTRY_SEQ = "country_seq";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = COUNTRY_SEQ)
	private Long id;

	private String code;

	@Column(length = 255)
	private String flagPath;

	@OneToOne
	@JoinColumn(name = "icc_id")
	private InternationalCallingCodeJpaEntity icc;

	@OneToMany
	@JoinColumn(name = "country_id")
	private List<TimeZoneJpaEntity> timeZones;

	@OneToMany(mappedBy = "country", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedId.locale")
	private Map<String, LocalizedCountryJpaEntity> localizations = new HashMap<>();

	@OneToMany(mappedBy = "country")
	private Set<DepartmentJpaEntity> departments = new HashSet<>();

	public CountryJpaEntity(String code, String flagPath, InternationalCallingCodeJpaEntity icc,
			List<TimeZoneJpaEntity> timeZones, Map<String, LocalizedCountryJpaEntity> localizations,
			Set<DepartmentJpaEntity> departments) {

		this.code = code;
		this.flagPath = flagPath;
		this.icc = icc;
		this.timeZones = timeZones;
		this.localizations = localizations;
		departments.forEach(d -> this.addDepartment(d));
	}

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

	public Set<DepartmentJpaEntity> getDepartments() {
		return Collections.unmodifiableSet(departments);
	}

	public void addDepartment(DepartmentJpaEntity department) {
		if (department == null)
			return;
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

	public String getFlagPath() {
		return flagPath;
	}

	public void setFlagPath(String flagPath) {
		this.flagPath = flagPath;
	}

	public InternationalCallingCodeJpaEntity getIcc() {
		return icc;
	}

	public void setIcc(InternationalCallingCodeJpaEntity icc) {
		this.icc = icc;
	}

	public List<TimeZoneJpaEntity> getTimeZones() {
		return timeZones;
	}

	public void setTimeZones(List<TimeZoneJpaEntity> timeZones) {
		this.timeZones = timeZones;
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
