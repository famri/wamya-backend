package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.UserAccount;

import java.util.Optional;

public interface LoadUserAccountPort {

    Optional<UserAccount> loadUserAccountBySubject(String subject);

    Boolean existsByOauthId(String userOauthId);

    String loadProfileImageLocation(Long userId);

    Boolean hasDefaultProfileImage(Long userId);

    boolean hasNoIdentityImage(Long userId);

    String loadIdentityDocumentLocation(Long userId);

    boolean existsBySubject(String username);
}
