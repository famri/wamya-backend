package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.ProfileInfoDto;

public interface LoadProfileInfoPort {

	ProfileInfoDto loadInfo(String username, String locale);

}
