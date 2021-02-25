package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "dl2dp_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "delegation_id",
		"department_id" }))
@NoArgsConstructor
@SequenceGenerator(name = DelegationToDepartmentTravelInfoJpaEntity.DL2DPTI_SEQ)
public class DelegationToDepartmentTravelInfoJpaEntity {

	public static final String DL2DPTI_SEQ = "dl2dpti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DL2DPTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "delegation_id")
	private DelegationJpaEntity delegation;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public DelegationToDepartmentTravelInfoJpaEntity(DelegationJpaEntity delegation, DepartmentJpaEntity department,
			Integer hours, Integer minutes, Integer distance) {

		this.delegation = delegation;
		this.department = department;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public DelegationJpaEntity getDelegation() {
		return delegation;
	}

	public void setDelegation(DelegationJpaEntity delegation) {
		this.delegation = delegation;
	}

	public DepartmentJpaEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentJpaEntity department) {
		this.department = department;
	}

	public Integer getHours() {
		return hours;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Long getId() {
		return id;
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
		DelegationToDepartmentTravelInfoJpaEntity other = (DelegationToDepartmentTravelInfoJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DelegationToDepartmentTravelInfoJpaEntity [id=" + id + ", delegation=" + delegation + ", department="
				+ department + ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
