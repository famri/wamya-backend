package com.excentria_it.wamya.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public  class OAuthUserAccount {

	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private boolean enabled;
	private boolean emailVerified;
	
	private List<Credentials> credentials;
	private Map<String, String> attributes;
	private Collection<String> realmRoles;
	private Collection<String> requiredActions;

	@AllArgsConstructor
	@NoArgsConstructor
	@Builder
	@Data
	public static class Credentials {
		private String type;
		private String value;
		private boolean temporary;
	}
	

}

