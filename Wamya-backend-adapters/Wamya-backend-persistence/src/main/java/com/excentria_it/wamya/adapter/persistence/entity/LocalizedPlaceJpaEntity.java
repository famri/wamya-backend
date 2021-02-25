package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "localized_place")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
public class LocalizedPlaceJpaEntity {
	@EmbeddedId
	private LocalizedPlaceId localizedPlaceId;

	private String name;

	@ManyToOne
	@MapsId("placeId")
	@JoinColumns(value = { @JoinColumn(name = "id"), @JoinColumn(name = "type") })
	private PlaceJpaEntity place;

	public LocalizedPlaceId getLocalizedPlaceId() {
		return localizedPlaceId;
	}

	public void setLocalizedPlaceId(LocalizedPlaceId localizedPlaceId) {
		this.localizedPlaceId = localizedPlaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlaceJpaEntity getPlace() {
		return place;
	}

	public void setPlace(PlaceJpaEntity place) {
		this.place = place;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localizedPlaceId == null) ? 0 : localizedPlaceId.hashCode());
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
		LocalizedPlaceJpaEntity other = (LocalizedPlaceJpaEntity) obj;
		if (localizedPlaceId == null) {
			return false;
		} else if (!localizedPlaceId.equals(other.localizedPlaceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalizedPlaceJpaEntity [localizedPlaceId=" + localizedPlaceId + ", name=" + name + ", place=" + place
				+ "]";
	}

}
