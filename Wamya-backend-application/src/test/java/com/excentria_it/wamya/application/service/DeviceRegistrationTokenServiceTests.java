package com.excentria_it.wamya.application.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@ExtendWith(MockitoExtension.class)
public class DeviceRegistrationTokenServiceTests {
	@Mock

	private LoadUserAccountPort loadUserAccountPort;

	@Mock
	private UpdateUserAccountPort updateUserAccountPort;

	@InjectMocks
	private DeviceRegistrationTokenService deviceRegistrationTokenService;

	@Test
	void testUpdateToken() {
		// given

		UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));

		// when
		deviceRegistrationTokenService.updateToken("some-device-registration-token", TestConstants.DEFAULT_EMAIL);
		// then
		then(updateUserAccountPort).should(times(1)).updateDeviceRegistrationToken(userAccount.getId(),
				"some-device-registration-token");
	}

}
