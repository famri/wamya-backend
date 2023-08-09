package com.excentria_it.wamya.springcloud.authorisationserver.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthUserAccount;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
	
	@Mappings({ @Mapping(target = "password", ignore = true) })
	OAuthUserAccount entityToApi(UserEntity entity);

	@Mappings({ @Mapping(target = "version", ignore = true) })
	UserEntity apiToEntity(OAuthUserAccount api);

}
