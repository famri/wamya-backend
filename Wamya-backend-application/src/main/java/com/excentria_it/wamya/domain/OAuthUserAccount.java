package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OAuthUserAccount {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private boolean enabled;
    private boolean emailVerified;

    private List<Credentials> credentials;
    private Map<OAuthUserAccountAttribute, String> attributes;
    private Collection<UserRole> realmRoles;
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

