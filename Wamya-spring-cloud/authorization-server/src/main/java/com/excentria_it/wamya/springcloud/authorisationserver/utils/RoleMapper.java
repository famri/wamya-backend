package com.excentria_it.wamya.springcloud.authorisationserver.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthRole;
import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {

	OAuthRole entityToApi(RoleEntity entity);

	@Mappings({ @Mapping(target = "id", ignore = true) })
	RoleEntity apiToEntity(OAuthRole api);
}
