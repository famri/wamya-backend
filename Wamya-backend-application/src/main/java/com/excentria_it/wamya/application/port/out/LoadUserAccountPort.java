package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.UserAccount;

import java.util.Optional;

public interface LoadUserAccountPort {

    Optional<UserAccount> loadUserAccountByUsername(String username);

    Boolean existsByOauthId(String userOauthId);

    String loadProfileImageLocation(Long userId);

    Boolean hasDefaultProfileImage(Long userId);

    boolean hasNoIdentityImage(Long userId);

    String loadIdentityDocumentLocation(Long userId);

}
