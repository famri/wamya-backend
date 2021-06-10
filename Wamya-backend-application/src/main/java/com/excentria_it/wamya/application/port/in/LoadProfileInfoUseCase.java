package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.ProfileInfoDto;

public interface LoadProfileInfoUseCase {


	ProfileInfoDto loadProfileInfo(String username, String string);

}
