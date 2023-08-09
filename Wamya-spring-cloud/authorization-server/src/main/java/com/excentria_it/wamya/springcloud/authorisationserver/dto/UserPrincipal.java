package com.excentria_it.wamya.springcloud.authorisationserver.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.excentria_it.wamya.springcloud.authorisationserver.model.RoleEntity;
import com.excentria_it.wamya.springcloud.authorisationserver.model.UserEntity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 4590514534761195951L;

	private UserEntity user;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return this.getAuthorities(user.getRoles());
	}

	@Override
	public String getPassword() {

		return user.getPassword();
	}

	@Override
	public String getUsername() {

		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {

		return user.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {

		return user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {

		return user.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {

		return user.isEnabled();
	}

	public Long getOauthId() {
		return user.getOauthId();
	}
	
	private final Collection<? extends GrantedAuthority> getAuthorities(Collection<RoleEntity> roles) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		roles.forEach(r -> {

			authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getName()));
			if (!r.getAuthorities().isEmpty()) {
				String[] scopeAuthorities = r.getAuthorities().split(",");
				for (String scope : scopeAuthorities) {
					authorities.add(new SimpleGrantedAuthority(scope));
				}
			}

		});
		return authorities;
	}
}
