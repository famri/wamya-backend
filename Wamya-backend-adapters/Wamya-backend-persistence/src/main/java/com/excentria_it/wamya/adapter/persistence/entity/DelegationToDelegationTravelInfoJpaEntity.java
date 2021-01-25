package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "dl2dl_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "delegation_one_id",
		"delegation_two_id" }))
@NoArgsConstructor
@SequenceGenerator(name = DelegationToDelegationTravelInfoJpaEntity.DL2DLTI_SEQ)
public class DelegationToDelegationTravelInfoJpaEntity {
	public static final String DL2DLTI_SEQ = "dl2dlti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DL2DLTI_SEQ)
	private Long id;

	@OneToOne
	@JoinColumn(name = "delegation_one_id")
	private DelegationJpaEntity delegationOne;
	@OneToOne
	@JoinColumn(name = "delegation_two_id")
	private DelegationJpaEntity delegationTwo;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public DelegationToDelegationTravelInfoJpaEntity(DelegationJpaEntity delegationOne,
			DelegationJpaEntity delegationTwo, Integer hours, Integer minutes, Integer distance) {

		this.delegationOne = delegationOne;
		this.delegationTwo = delegationTwo;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public DelegationJpaEntity getDelegationOne() {
		return delegationOne;
	}

	public void setDelegationOne(DelegationJpaEntity delegationOne) {
		this.delegationOne = delegationOne;
	}

	public DelegationJpaEntity getDelegationTwo() {
		return delegationTwo;
	}

	public void setDelegationTwo(DelegationJpaEntity delegationTwo) {
		this.delegationTwo = delegationTwo;
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
		DelegationToDelegationTravelInfoJpaEntity other = (DelegationToDelegationTravelInfoJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DelegationToDelegationTravelInfoJpaEntity [id=" + id + ", delegationOne=" + delegationOne
				+ ", delegationTwo=" + delegationTwo + ", hours=" + hours + ", minutes=" + minutes + ", distance="
				+ distance + "]";
	}

}
