package com.excentria_it.wamya.springcloud.authorisationserver.dto;

import java.util.Collection;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class User {

	private UUID oauthId;

	@NotNull
	private String firstname;
	@NotNull
	private String lastname;
	@NotNull
	private String email;
	@NotNull
	private String phoneNumber;
	@NotNull
	private String password;

	private boolean isAccountNonExpired;

	private boolean isAccountNonLocked;

	private boolean isCredentialsNonExpired;

	private boolean isEnabled;
	@NotNull
	@NotEmpty
	private Collection<Role> roles;
}
