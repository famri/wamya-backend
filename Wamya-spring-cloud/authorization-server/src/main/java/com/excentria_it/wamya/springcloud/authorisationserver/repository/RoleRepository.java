package com.excentria_it.wamya.springcloud.authorisationserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleType;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(RoleType name);
}
