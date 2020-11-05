package com.excentria_it.wamya.domain;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class OAuthUserAccount {

	private String firstname;
	private String lastname;
	private String email;
	private String phoneNumber;
	private String password;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
	private Collection<OAuthRole> roles;

}
