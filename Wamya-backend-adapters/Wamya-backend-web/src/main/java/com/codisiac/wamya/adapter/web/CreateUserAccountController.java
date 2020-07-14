package com.codisiac.wamya.adapter.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codisiac.wamya.adapter.web.dto.CreateUserAccountDto;
import com.codisiac.wamya.application.port.in.CreateUserAccountUseCase;
import com.codisiac.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.codisiac.wamya.common.annotation.Generated;
import com.codisiac.wamya.common.annotation.WebAdapter;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;
import com.codisiac.wamya.domain.UserAccount.UserPasswordPair;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
class CreateUserAccountController {

	private final CreateUserAccountUseCase createUserAccountUseCase;

	@PostMapping(path = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	void createUserAccount(@RequestBody CreateUserAccountDto createUserAccountDto) {

		CreateUserAccountCommand command = new CreateUserAccountCommand(
				new MobilePhoneNumber(createUserAccountDto.getIcc(), createUserAccountDto.getMobilePhoneNumber()),
				new UserPasswordPair(createUserAccountDto.getPassword(),
						createUserAccountDto.getPasswordConfirmation()));

		createUserAccountUseCase.registerUserAccountCreationDemand(command);
	}

	@Generated
	@GetMapping(path = "/hello")
	ResponseEntity test(@RequestParam String message) {

		return ResponseEntity.ok("hello bonjour" + message + "good");

	}

}
