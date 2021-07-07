package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

public interface LoadMobileValidationCodePort {

	Optional<String> loadMobileValidationCode(String username);

}
