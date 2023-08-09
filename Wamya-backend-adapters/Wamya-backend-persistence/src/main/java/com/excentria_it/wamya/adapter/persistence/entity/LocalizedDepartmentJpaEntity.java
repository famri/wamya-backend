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
@Table(name = "localized_department")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
public class LocalizedDepartmentJpaEntity {

	@EmbeddedId
	private LocalizedId localizedId;

	@ManyToOne
	@MapsId("id")
	@JoinColumn(name = "id")
	private DepartmentJpaEntity department;

	private String name;

	public LocalizedId getLocalizedId() {
		return localizedId;
	}

	public void setLocalizedId(LocalizedId localizedId) {
		this.localizedId = localizedId;
	}

	public DepartmentJpaEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentJpaEntity department) {
		this.department = department;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		LocalizedDepartmentJpaEntity other = (LocalizedDepartmentJpaEntity) obj;
		if (localizedId == null) {
			return false;
		} else if (!localizedId.equals(other.localizedId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalizedDepartmentJpaEntity [localizedId=" + localizedId + ", department=" + department + ", name="
				+ name + "]";
	}

}
