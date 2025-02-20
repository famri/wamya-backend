package com.excentria_it.wamya.adapter.persistence.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "vehicle_constructor")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = ConstructorJpaEntity.CONSTRUCTOR_SEQ, initialValue = 1, allocationSize = 5)
public class ConstructorJpaEntity {

	public static final String CONSTRUCTOR_SEQ = "vehicle_constructor_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = CONSTRUCTOR_SEQ)
	private Long id;

	private String name;

	@Builder.Default
	@OneToMany(mappedBy = "constructor", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<ModelJpaEntity> models = new HashSet<>();

	public void addModel(ModelJpaEntity model) {
		this.models.add(model);
		model.setConstructor(this);
	}

	public void removeModel(ModelJpaEntity model) {
		this.models.remove(model);
		model.setConstructor(null);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ModelJpaEntity> getModels() {
		return Collections.unmodifiableSet(models);
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
		ConstructorJpaEntity other = (ConstructorJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConstructorJpaEntity [id=" + id + ", name=" + name + "]";
	}

}
