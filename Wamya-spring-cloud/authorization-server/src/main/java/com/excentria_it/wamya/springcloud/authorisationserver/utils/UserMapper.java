package com.excentria_it.wamya.springcloud.authorisationserver.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.User;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {

	User entityToApi(UserEntity entity);

	@Mappings({ @Mapping(target = "version", ignore = true) })
	UserEntity apiToEntity(User api);

}
