package com.excentria_it.wamya.adapter.persistence.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Generated
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedPlaceId implements Serializable {

	private static final long serialVersionUID = 7155166440854997360L;

	private PlaceId placeId;

	private String locale;

	public LocalizedPlaceId(String locale) {
		super();
		this.locale = locale;
	}

	public PlaceId getPlaceId() {
		return placeId;
	}

	public void setPlaceId(PlaceId placeId) {
		this.placeId = placeId;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
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
		LocalizedPlaceId other = (LocalizedPlaceId) obj;
		if (placeId == null) {
			if (other.placeId != null)
				return false;
		} else if (!placeId.equals(other.placeId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalizedPlaceId [placeId=" + placeId + ", locale=" + locale + "]";
	}

}
