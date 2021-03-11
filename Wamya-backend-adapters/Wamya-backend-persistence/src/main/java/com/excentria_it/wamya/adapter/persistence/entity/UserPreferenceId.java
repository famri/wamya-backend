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
public class UserPreferenceId implements Serializable {

	private static final long serialVersionUID = -5158952285995795045L;

	private Long id;

	private String key;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		UserPreferenceId other = (UserPreferenceId) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserPreferenceId [id=" + id + ", key=" + key + "]";
	}

}
