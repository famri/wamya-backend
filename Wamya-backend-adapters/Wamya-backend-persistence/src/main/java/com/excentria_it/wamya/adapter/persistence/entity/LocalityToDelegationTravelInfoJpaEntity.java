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
@Table(name = "l2dl_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "locality_id",
		"delegation_id" }))
@NoArgsConstructor
@SequenceGenerator(name = LocalityToDelegationTravelInfoJpaEntity.L2DLTI_SEQ)
public class LocalityToDelegationTravelInfoJpaEntity {

	public static final String L2DLTI_SEQ = "l2dlti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = L2DLTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "locality_id")
	private LocalityJpaEntity locality;

	@ManyToOne
	@JoinColumn(name = "delegation_id")
	private DelegationJpaEntity delegation;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public LocalityToDelegationTravelInfoJpaEntity(LocalityJpaEntity locality, DelegationJpaEntity delegation,
			Integer hours, Integer minutes, Integer distance) {
		super();
		this.locality = locality;
		this.delegation = delegation;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public LocalityJpaEntity getLocality() {
		return locality;
	}

	public void setLocality(LocalityJpaEntity locality) {
		this.locality = locality;
	}

	public DelegationJpaEntity getDelegation() {
		return delegation;
	}

	public void setDelegation(DelegationJpaEntity delegation) {
		this.delegation = delegation;
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
		LocalityToDelegationTravelInfoJpaEntity other = (LocalityToDelegationTravelInfoJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalityToDelegationTavelInfoJpaEntity [id=" + id + ", locality=" + locality + ", delegation="
				+ delegation + ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
