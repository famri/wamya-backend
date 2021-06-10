package com.excentria_it.wamya.application.port.out;

import java.time.LocalDate;

public interface UpdateProfileInfoPort {

	void updateAboutSection(String username, Long genderId, String firstname, String lastname, LocalDate dateOfBirth,
			String minibio);

	void updateEmailSection(String username, String email);

	void updateMobileSection(String username, String mobileNumber, Long iccId);

}
