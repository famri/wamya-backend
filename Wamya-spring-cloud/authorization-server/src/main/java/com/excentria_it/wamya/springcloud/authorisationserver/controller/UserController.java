package com.excentria_it.wamya.springcloud.authorisationserver.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PostMapping(path = "/users/{id}/do-reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void createUser(@NotEmpty @RequestParam(name = "password") String newPassword,
			@PathVariable(name = "id") Long oauthId) {

		userService.resetPassword(oauthId, newPassword);

	}
}
