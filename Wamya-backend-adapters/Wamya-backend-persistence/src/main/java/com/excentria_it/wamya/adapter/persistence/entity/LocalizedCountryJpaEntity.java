package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "localized_country")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
public class LocalizedCountryJpaEntity {
	@EmbeddedId
	private LocalizedId localizedId;

	private String name;

	@ManyToOne
	@MapsId("id")
	@JoinColumn(name = "id")
	private CountryJpaEntity country;

	public LocalizedId getLocalizedId() {
		return localizedId;
	}

	public void setLocalizedId(LocalizedId localizedId) {
		this.localizedId = localizedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CountryJpaEntity getCountry() {
		return country;
	}

	public void setCountry(CountryJpaEntity country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localizedId == null) ? 0 : localizedId.hashCode());
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
		LocalizedCountryJpaEntity other = (LocalizedCountryJpaEntity) obj;
		if (localizedId == null) {
			return false;
		} else if (!localizedId.equals(other.localizedId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalizedCountryJpaEntity [localizedId=" + localizedId + ", name=" + name + ", country=" + country
				+ "]";
	}

}
