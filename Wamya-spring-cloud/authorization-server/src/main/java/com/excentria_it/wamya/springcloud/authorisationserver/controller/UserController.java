package com.excentria_it.wamya.springcloud.authorisationserver.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthUserAccount;
import com.excentria_it.wamya.springcloud.authorisationserver.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/oauth", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	private UserService userService;

	@PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public OAuthUserAccount createUser(@Valid @RequestBody OAuthUserAccount user) {

		OAuthUserAccount newUser = userService.createUser(user);

		return newUser;
	}

}
