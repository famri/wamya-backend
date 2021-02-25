package com.excentria_it.wamya.adapter.persistence.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Generated
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PlaceId implements Serializable {

	private static final long serialVersionUID = -1706431439302656585L;

	private Long id;

	@Enumerated(EnumType.STRING)
	private PlaceType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlaceType getType() {
		return type;
	}

	public void setType(PlaceType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		PlaceId other = (PlaceId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TypedId [id=" + id + ", type=" + type + "]";
	}

}
