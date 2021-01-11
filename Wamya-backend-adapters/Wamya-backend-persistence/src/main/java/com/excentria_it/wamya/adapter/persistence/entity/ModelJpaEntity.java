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
@Table(name = "vehicule_model")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = ModelJpaEntity.MODEL_SEQ, initialValue = 1, allocationSize = 5)
public class ModelJpaEntity {

	public static final String MODEL_SEQ = "vehicule_model_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = MODEL_SEQ)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "constructor_id")
	private ConstructorJpaEntity constructor;

	private String name;

	public ConstructorJpaEntity getConstructor() {
		return constructor;
	}

	public void setConstructor(ConstructorJpaEntity constructor) {
		this.constructor = constructor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		ModelJpaEntity other = (ModelJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ModelJpaEntity [id=" + id + ", Constructor=" + constructor + ", name=" + name + "]";
	}

}
