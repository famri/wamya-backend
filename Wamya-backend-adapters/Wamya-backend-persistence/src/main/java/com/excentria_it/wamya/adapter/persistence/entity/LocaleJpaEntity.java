package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "user_locale")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = LocaleJpaEntity.USER_LOCALE_SEQ, initialValue = 1, allocationSize = 5)
public class LocaleJpaEntity {

	public static final String USER_LOCALE_SEQ = "user_locale_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_LOCALE_SEQ)
	private Long id;

	private String name;

	private String countryCode;

	private String languageCode;

	public LocaleJpaEntity(String name, String languageCode, String countryCode) {
		super();
		this.name = name;
		this.languageCode = languageCode;
		this.countryCode = countryCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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
		LocaleJpaEntity other = (LocaleJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserLocaleJpaEntity [id=" + id + ", name=" + name + ", languageCode=" + languageCode + ", countryCode="
				+ countryCode + "]";
	}

}
