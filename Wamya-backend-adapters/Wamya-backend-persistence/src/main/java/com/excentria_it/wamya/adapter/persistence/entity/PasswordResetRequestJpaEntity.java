package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "password_reset_request")
@NoArgsConstructor
public class PasswordResetRequestJpaEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID uuid;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private UserAccountJpaEntity account;

	private Instant expiryTimestamp;

	public PasswordResetRequestJpaEntity(UserAccountJpaEntity account, Instant expiryTimestamp) {
		super();
		this.account = account;
		this.expiryTimestamp = expiryTimestamp;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public UserAccountJpaEntity getAccount() {
		return account;
	}

	public void setAccount(UserAccountJpaEntity account) {
		this.account = account;
	}

	public Instant getExpiryTimestamp() {
		return expiryTimestamp;
	}

	public void setExpiryTimestamp(Instant expiryTimestamp) {
		this.expiryTimestamp = expiryTimestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expiryTimestamp == null) ? 0 : expiryTimestamp.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		PasswordResetRequestJpaEntity other = (PasswordResetRequestJpaEntity) obj;
		if (expiryTimestamp == null) {
			if (other.expiryTimestamp != null)
				return false;
		} else if (!expiryTimestamp.equals(other.expiryTimestamp))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PasswordResetRequestJpaEntity [uuid=" + uuid + ", account=" + account + ", expiryTimestamp="
				+ expiryTimestamp + "]";
	}

}
