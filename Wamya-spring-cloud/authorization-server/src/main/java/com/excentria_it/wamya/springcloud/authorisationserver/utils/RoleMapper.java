package com.excentria_it.wamya.springcloud.authorisationserver.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.Role;
import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	Role entityToApi(RoleEntity entity);

	@Mappings({ @Mapping(target = "id", ignore = true), @Mapping(target = "privileges", ignore = true) })
	RoleEntity apiToEntity(Role api);
}
