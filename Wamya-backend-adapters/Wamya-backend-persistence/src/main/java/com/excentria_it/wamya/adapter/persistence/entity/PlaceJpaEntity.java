package com.excentria_it.wamya.adapter.persistence.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "place")
@NoArgsConstructor
public class PlaceJpaEntity {

	@EmbeddedId
	private PlaceId placeId;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	@OneToMany(mappedBy = "place", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true)
	@MapKey(name = "localizedPlaceId.locale")
	private Map<String, LocalizedPlaceJpaEntity> localizations = new HashMap<>();

	@Column(scale = 6, precision = 8)
	private BigDecimal latitude;
	
	@Column(scale = 6, precision = 9)
	private BigDecimal longitude;

	public PlaceJpaEntity(DepartmentJpaEntity department, Map<String, LocalizedPlaceJpaEntity> localizations,
			BigDecimal latitude, BigDecimal longitude) {
		super();
		this.department = department;
		this.localizations = localizations;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public PlaceId getPlaceId() {
		return placeId;
	}

	public void setPlaceId(PlaceId placeId) {
		this.placeId = placeId;
	}

	public DepartmentJpaEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentJpaEntity department) {
		this.department = department;
	}

	public Map<String, LocalizedPlaceJpaEntity> getLocalizations() {
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

	public String getName(String locale) {
		if (localizations.containsKey(locale)) {
			return localizations.get(locale).getName();
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((placeId == null) ? 0 : placeId.hashCode());
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
		PlaceJpaEntity other = (PlaceJpaEntity) obj;
		if (placeId == null) {

			return false;
		} else if (!placeId.equals(other.placeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlaceJpaEntity [placeId=" + placeId + ", department=" + department + ", latitude=" + latitude
				+ ", longitude=" + longitude + "]";
	}

}
