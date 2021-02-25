package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "geo2l_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "geo_place_id",
		"locality_id" }))
@NoArgsConstructor
@SequenceGenerator(name = GeoPlaceToLocalityTravelInfoJpaEntity.GEO2LTI_SEQ)
public class GeoPlaceToLocalityTravelInfoJpaEntity {
	public static final String GEO2LTI_SEQ = "geo2lti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEO2LTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "geo_place_id")
	private GeoPlaceJpaEntity geoPlace;

	@ManyToOne
	@JoinColumn(name = "locality_id")
	private LocalityJpaEntity locality;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public GeoPlaceToLocalityTravelInfoJpaEntity(GeoPlaceJpaEntity geoPlace, LocalityJpaEntity locality, Integer hours,
			Integer minutes, Integer distance) {

		this.geoPlace = geoPlace;
		this.locality = locality;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public GeoPlaceJpaEntity getGeoPlace() {
		return geoPlace;
	}

	public void setGeoPlace(GeoPlaceJpaEntity geoPlace) {
		this.geoPlace = geoPlace;
	}

	public LocalityJpaEntity getLocality() {
		return locality;
	}

	public void setLocality(LocalityJpaEntity locality) {
		this.locality = locality;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
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
		GeoPlaceToLocalityTravelInfoJpaEntity other = (GeoPlaceToLocalityTravelInfoJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeoPlaceToDepartmentTravelInfoJpaEntity [id=" + id + ", geoPlace=" + geoPlace + ", locality=" + locality
				+ ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
