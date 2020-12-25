package com.excentria_it.wamya.adapter.persistence.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "vehicule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = VehiculeJpaEntity.VEHICULE_SEQ, initialValue = 1, allocationSize = 5)
public class VehiculeJpaEntity {

	public static final String VEHICULE_SEQ = "vehicule_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = VEHICULE_SEQ)
	private Long id;

	@ManyToOne
	private EngineTypeJpaEntity type;

	@ManyToOne
	private ModelJpaEntity model;

	private LocalDate circulationDate;

	private String registration;

	public EngineTypeJpaEntity getType() {
		return type;
	}

	public void setType(EngineTypeJpaEntity type) {
		this.type = type;
	}

	public ModelJpaEntity getModel() {
		return model;
	}

	public void setModel(ModelJpaEntity model) {
		this.model = model;
	}

	public LocalDate getCirculationDate() {
		return circulationDate;
	}

	public void setCirculationDate(LocalDate circulationDate) {
		this.circulationDate = circulationDate;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
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
		VehiculeJpaEntity other = (VehiculeJpaEntity) obj;
		if (id == null) {
			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VehiculeJpaEntity [id=" + id + ", type=" + type + ", model=" + model + ", circulationDate="
				+ circulationDate + ", registration=" + registration + "]";
	}

}
