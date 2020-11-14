package com.excentria_it.wamya.springcloud.authorisationserver.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@NoArgsConstructor
@Data
@SequenceGenerator(name = RoleEntity.ROLE_SEQ, initialValue = 4, allocationSize = 5 )
public class RoleEntity {

	public static final String ROLE_SEQ = "role_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ROLE_SEQ)
	private Long id;

	private String name;

}
