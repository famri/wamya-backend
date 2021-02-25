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
@Table(name = "l2l_travel_info", uniqueConstraints = @UniqueConstraint(columnNames = { "locality_one_id",
		"locality_two_id" }))
@NoArgsConstructor
@SequenceGenerator(name = LocalityToLocalityTravelInfoJpaEntity.L2LTI_SEQ)
public class LocalityToLocalityTravelInfoJpaEntity {
	public static final String L2LTI_SEQ = "l2lti_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = L2LTI_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "locality_one_id")
	private LocalityJpaEntity localityOne;

	@ManyToOne
	@JoinColumn(name = "locality_two_id")
	private LocalityJpaEntity localityTwo;

	private Integer hours;

	private Integer minutes;

	private Integer distance;

	public LocalityToLocalityTravelInfoJpaEntity(LocalityJpaEntity localityOne, LocalityJpaEntity localityTwo,
			Integer hours, Integer minutes, Integer distance) {

		this.localityOne = localityOne;
		this.localityTwo = localityTwo;
		this.hours = hours;
		this.minutes = minutes;
		this.distance = distance;
	}

	public LocalityJpaEntity getLocalityOne() {
		return localityOne;
	}

	public void setLocalityOne(LocalityJpaEntity localityOne) {
		this.localityOne = localityOne;
	}

	public LocalityJpaEntity getLocalityTwo() {
		return localityTwo;
	}

	public void setLocalityTwo(LocalityJpaEntity localityTwo) {
		this.localityTwo = localityTwo;
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
		LocalityToLocalityTravelInfoJpaEntity other = (LocalityToLocalityTravelInfoJpaEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocalityToLocalityTravelInfoJpaEntity [id=" + id + ", localityOne=" + localityOne + ", localityTwo="
				+ localityTwo + ", hours=" + hours + ", minutes=" + minutes + ", distance=" + distance + "]";
	}

}
