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
@Table(name = "geo2geo_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "geo_place_one_id",
		"geo_place_two_id" }))
@NoArgsConstructor
@SequenceGenerator(name = GeoPlaceToGeoPlaceTravelInfoJpaEntity.GEO2GEOTI_SEQ)
public class GeoPlaceToGeoPlaceTravelInfoJpaEntity {
	public static final String GEO2GEOTI_SEQ = "geo2geoti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEO2GEOTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "geo_place_one_id")
	private GeoPlaceJpaEntity geoPlaceOne;

	@ManyToOne
	@JoinColumn(name = "geo_place_two_id")
	private GeoPlaceJpaEntity geoPlaceTwo;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public GeoPlaceToGeoPlaceTravelInfoJpaEntity(GeoPlaceJpaEntity geoPlaceOne, GeoPlaceJpaEntity geoPlaceTwo,
			Integer hours, Integer minutes, Integer distance) {

		this.geoPlaceOne = geoPlaceOne;
		this.geoPlaceTwo = geoPlaceTwo;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public GeoPlaceJpaEntity getGeoPlaceOne() {
		return geoPlaceOne;
	}

	public void setGeoPlaceOne(GeoPlaceJpaEntity geoPlaceOne) {
		this.geoPlaceOne = geoPlaceOne;
	}

	public GeoPlaceJpaEntity getGeoPlaceTwo() {
		return geoPlaceTwo;
	}

	public void setGeoPlaceTwo(GeoPlaceJpaEntity geoPlaceTwo) {
		this.geoPlaceTwo = geoPlaceTwo;
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
		GeoPlaceToGeoPlaceTravelInfoJpaEntity other = (GeoPlaceToGeoPlaceTravelInfoJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeoPlaceToGeoPlaceTravelInfoJpaEntity [id=" + id + ", geoPlaceOne=" + geoPlaceOne + ", geoPlaceTwo="
				+ geoPlaceTwo + ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
