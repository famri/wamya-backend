package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface FindDiscussionUseCase {

    LoadDiscussionsDto findDiscussionById(FindDiscussionByIdCommand command);

    LoadDiscussionsDto findDiscussionByClientIdAndTransporterId(
            FindDiscussionByClientIdAndTransporterIdCommand command);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class FindDiscussionByIdCommand {
        @NotEmpty
        private String username;
        @NotNull
        private Long discussionId;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    class FindDiscussionByClientIdAndTransporterIdCommand {
        @NotEmpty
        private String username;
        @NotNull
        private String clientId;
        @NotNull
        private String transporterId;
    }

}
