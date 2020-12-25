package com.excentria_it.wamya.adapter.persistence.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
@Embeddable
public class LocalizedId implements Serializable {

	private static final long serialVersionUID = -5289594055575768374L;

	private Long id;

	private String locale;

	public LocalizedId(String locale) {
		this.locale = locale;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		LocalizedId other = (LocalizedId) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalizedId [id=" + id + ", locale=" + locale + "]";
	}

}
