package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "user_preference")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor

@SequenceGenerator(name = UserPreferenceJpaEntity.PREFERENCE_SEQ, initialValue = 1, allocationSize = 5)
public class UserPreferenceJpaEntity {
	public static final String PREFERENCE_SEQ = "preference_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PREFERENCE_SEQ)
	private Long id;

	private String key;

	private String value;

	public UserPreferenceJpaEntity(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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
		UserPreferenceJpaEntity other = (UserPreferenceJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserPreferenceJpaEntity [id=" + id + ", key=" + key + ", value=" + value + "]";
	}

}
