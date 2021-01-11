package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "temporary_model")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = TemporaryModelJpaEntity.TEMPORARY_MODEL_SEQ)
public class TemporaryModelJpaEntity {

	public static final String TEMPORARY_MODEL_SEQ = "temporary_model_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TEMPORARY_MODEL_SEQ)
	private Long id;

	private String constructorName;

	private String modelName;

	public String getConstructorName() {
		return constructorName;
	}

	public void setConstructorName(String constructorName) {
		this.constructorName = constructorName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
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
		TemporaryModelJpaEntity other = (TemporaryModelJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TemporaryModelJpaEntity [id=" + id + ", constructorName=" + constructorName + ", modelName=" + modelName
				+ "]";
	}

}
