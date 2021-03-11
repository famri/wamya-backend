package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.excentria_it.wamya.common.annotation.Generated;

import lombok.NoArgsConstructor;

@Generated
@Entity
@Table(name = "time_zone")
@NoArgsConstructor
@SequenceGenerator(name = TimeZoneJpaEntity.TIME_ZONE_SEQ)
public class TimeZoneJpaEntity {
	public static final String TIME_ZONE_SEQ = "time_zone_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TIME_ZONE_SEQ)
	private Long id;

	private String name;

	private String gmtOffset;

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

	public String getGmtOffset() {
		return gmtOffset;
	}

	public void setGmtOffset(String gmtOffset) {
		this.gmtOffset = gmtOffset;
	}

	public TimeZoneJpaEntity(String name, String gmtOffset) {
		super();
		this.name = name;
		this.gmtOffset = gmtOffset;
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
		TimeZoneJpaEntity other = (TimeZoneJpaEntity) obj;
		if (id == null) {

			return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TimeZoneJpaEntity [id=" + id + ", name=" + name + ", gmtOffset=" + gmtOffset + "]";
	}

}
