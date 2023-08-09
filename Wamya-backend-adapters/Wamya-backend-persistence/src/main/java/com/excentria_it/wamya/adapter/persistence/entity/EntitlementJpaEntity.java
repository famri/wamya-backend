package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;
import com.excentria_it.wamya.domain.EntitlementType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "entitlement")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = EntitlementJpaEntity.ENTITLEMENT_SEQ, initialValue = 1, allocationSize = 5)
public class EntitlementJpaEntity {

	public static final String ENTITLEMENT_SEQ = "entitlement_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ENTITLEMENT_SEQ)
	private Long id;

	@Enumerated(EnumType.STRING)
	private EntitlementType type;

	private Boolean read;

	private Boolean write;

	public EntitlementJpaEntity(EntitlementType type, Boolean read, Boolean write) {
		super();
		this.type = type;
		this.read = read;
		this.write = write;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntitlementType getType() {
		return type;
	}

	public void setType(EntitlementType type) {
		this.type = type;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Boolean getWrite() {
		return write;
	}

	public void setWrite(Boolean write) {
		this.write = write;
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
		EntitlementJpaEntity other = (EntitlementJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntitlementJpaEntity [id=" + id + ", type=" + type + ", read=" + read + ", write=" + write + "]";
	}

}
