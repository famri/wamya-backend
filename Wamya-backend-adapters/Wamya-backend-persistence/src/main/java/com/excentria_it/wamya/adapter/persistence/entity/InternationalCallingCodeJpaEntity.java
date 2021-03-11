package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "international_calling_code", uniqueConstraints = @UniqueConstraint(columnNames = { "id", "value" }))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "icc_seq", initialValue = 3, allocationSize = 1)
public class InternationalCallingCodeJpaEntity {
	public static final String ICC_SEQ = "icc_seq";
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ICC_SEQ)
	private Long id;

	private String value;

	private Boolean enabled;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
		InternationalCallingCodeJpaEntity other = (InternationalCallingCodeJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InternationalCallingCodeJpaEntity [id=" + id + ", value=" + value + ", enabled=" + enabled + "]";
	}

}
