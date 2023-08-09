package com.excentria_it.wamya.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Locale;

public interface RequestPasswordResetUseCase {

    void requestPasswordReset(String userLogin, Locale locale);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    class RequestPasswordResetCommand {

        @NotEmpty
        String userLogin;

    }
}
