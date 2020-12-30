package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "user_rating")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = RatingJpaEntity.RATING_SEQ, initialValue = 1, allocationSize = 5)
public class RatingJpaEntity {

	public static final String RATING_SEQ = "rating_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = RATING_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "rating_type_id")
	private RatingTypeJpaEntity type;

	private Double value;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private UserAccountJpaEntity user;

	public RatingTypeJpaEntity getType() {
		return type;
	}

	public void setType(RatingTypeJpaEntity type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
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
		RatingJpaEntity other = (RatingJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RatingJpaEntity [id=" + id + ", type=" + type + ", value=" + value + "]";
	}

}
