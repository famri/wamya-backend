package com.excentria_it.wamya.springcloud.authorisationserver.dto;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OAuthUserAccount {

	private Long oauthId;

	@NotNull
	@NotEmpty
	private String firstname;

	@NotNull
	@NotEmpty
	private String lastname;

	@NotNull
	@NotEmpty
	private String email;

	@NotNull
	@NotEmpty
	private String phoneNumber;

	@NotNull
	@NotEmpty
	private String password;

	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;

	@NotNull
	@NotEmpty
	private Collection<OAuthRole> roles;

}