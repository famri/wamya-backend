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
@Table(name = "l2dp_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "locality_id",
		"department_id" }))
@NoArgsConstructor
@SequenceGenerator(name = LocalityToDepartmentTravelInfoJpaEntity.L2DPTI_SEQ)
public class LocalityToDepartmentTravelInfoJpaEntity {
	
	public static final String L2DPTI_SEQ = "l2dpti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = L2DPTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "locality_id")
	private LocalityJpaEntity locality;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public LocalityToDepartmentTravelInfoJpaEntity(LocalityJpaEntity locality, DepartmentJpaEntity department,
			Integer hours, Integer minutes, Integer distance) {
		super();
		this.locality = locality;
		this.department = department;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public LocalityJpaEntity getLocality() {
		return locality;
	}

	public void setLocality(LocalityJpaEntity locality) {
		this.locality = locality;
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
		LocalityToDepartmentTravelInfoJpaEntity other = (LocalityToDepartmentTravelInfoJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalityToDepartmentTavelInfoJpaEntity [id=" + id + ", locality=" + locality + ", department="
				+ department + ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
