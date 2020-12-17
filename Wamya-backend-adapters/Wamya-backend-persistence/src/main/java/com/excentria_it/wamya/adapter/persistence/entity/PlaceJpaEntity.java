package com.excentria_it.wamya.adapter.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "place")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class PlaceJpaEntity {

	@Id
	private String id;

	@Column
	private String regionId;
	
	@Column
	private String name;
}
