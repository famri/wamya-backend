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
@Table(name = "user_preference")

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
public class UserPreferenceJpaEntity {

	@EmbeddedId
	private UserPreferenceId userPreferenceId;

	private String value;

	@ManyToOne
	@MapsId("id")
	@JoinColumn(name = "id")
	private UserAccountJpaEntity userAccount;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public UserPreferenceId getUserPreferenceId() {
		return userPreferenceId;
	}

	public void setUserPreferenceId(UserPreferenceId userPreferenceId) {
		this.userPreferenceId = userPreferenceId;
	}

	public UserAccountJpaEntity getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccountJpaEntity userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userPreferenceId == null) ? 0 : userPreferenceId.hashCode());
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
		if (userPreferenceId == null) {
			return false;
		} else if (!userPreferenceId.equals(other.userPreferenceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserPreferenceJpaEntity [userPreferenceId=" + userPreferenceId + ", value=" + value + "]";
	}

}
