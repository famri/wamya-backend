package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadDiscussionsOutput {
    private Long id;

    private Boolean active;

    private Instant dateTime;

    private MessageOutput latestMessage;

    private InterlocutorOutput client;

    private InterlocutorOutput transporter;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class MessageOutput {
        private Long id;
        private String authorId;
        private String content;
        private Instant dateTime;
        private Boolean read;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class InterlocutorOutput {
        private String id;
        private String name;
        private String email;
        private String mobileNumber;
        private String photoUrl;

    }
}
