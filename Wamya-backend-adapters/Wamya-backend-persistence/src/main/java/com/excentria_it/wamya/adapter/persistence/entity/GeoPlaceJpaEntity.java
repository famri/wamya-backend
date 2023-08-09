package com.excentria_it.wamya.adapter.persistence.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "geo_place")
@NoArgsConstructor
@SequenceGenerator(name = GeoPlaceJpaEntity.GEO_PLACE_SEQ, initialValue = 1, allocationSize = 5)
public class GeoPlaceJpaEntity {
	public static final String GEO_PLACE_SEQ = "geo_place_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = GEO_PLACE_SEQ)
	private Long id;

	private String name;

	@Column(scale = 6, precision = 8)
	private BigDecimal latitude;

	@Column(scale = 6, precision = 9)
	private BigDecimal longitude;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private DepartmentJpaEntity department;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private ClientJpaEntity client;

	public GeoPlaceJpaEntity(String name, BigDecimal latitude, BigDecimal longitude, DepartmentJpaEntity department,
			ClientJpaEntity client) {

		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.department = department;
		this.client = client;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public DepartmentJpaEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentJpaEntity department) {
		this.department = department;
	}

	public ClientJpaEntity getClient() {
		return client;
	}

	public void setClient(ClientJpaEntity client) {
		this.client = client;
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
		GeoPlaceJpaEntity other = (GeoPlaceJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GeoPlaceJpaEntity [id=" + id + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", department=" + department + ", client=" + client + "]";
	}

}
