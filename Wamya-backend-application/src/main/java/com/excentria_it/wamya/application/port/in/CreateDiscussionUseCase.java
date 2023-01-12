package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public interface CreateDiscussionUseCase {

    LoadDiscussionsDto createDiscussion(CreateDiscussionCommand command, String username);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class CreateDiscussionCommand {

        @NotNull
        private String clientId;
        @NotNull
        private String transporterId;

    }

}
