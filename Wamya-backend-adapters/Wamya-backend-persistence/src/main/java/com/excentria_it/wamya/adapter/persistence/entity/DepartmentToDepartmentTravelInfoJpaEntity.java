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
@Table(name = "dp2dp_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "department_one_id",
		"department_two_id" }))
@NoArgsConstructor
@SequenceGenerator(name = DepartmentToDepartmentTravelInfoJpaEntity.DP2DPTI_SEQ)
public class DepartmentToDepartmentTravelInfoJpaEntity {
	public static final String DP2DPTI_SEQ = "dp2dpti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DP2DPTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "department_one_id")
	private DepartmentJpaEntity departmentOne;

	@ManyToOne
	@JoinColumn(name = "department_two_id")
	private DepartmentJpaEntity departmentTwo;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public DepartmentToDepartmentTravelInfoJpaEntity(DepartmentJpaEntity departmentOne,
			DepartmentJpaEntity departmentTwo, Integer hours, Integer minutes, Integer distance) {

		this.departmentOne = departmentOne;
		this.departmentTwo = departmentTwo;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public DepartmentJpaEntity getDepartmentOne() {
		return departmentOne;
	}

	public void setDepartmentOne(DepartmentJpaEntity departmentOne) {
		this.departmentOne = departmentOne;
	}

	public DepartmentJpaEntity getDepartmentTwo() {
		return departmentTwo;
	}

	public void setDepartmentTwo(DepartmentJpaEntity departmentTwo) {
		this.departmentTwo = departmentTwo;
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
		DepartmentToDepartmentTravelInfoJpaEntity other = (DepartmentToDepartmentTravelInfoJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DepartmentToDepartmentTravelInfoJpaEntity [id=" + id + ", departmentOne=" + departmentOne
				+ ", departmentTwo=" + departmentTwo + ", hours=" + hours + ", minutes=" + minutes + ", distance="
				+ distance + "]";
	}
}
