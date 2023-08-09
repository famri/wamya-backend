package com.excentria_it.wamya.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoadDiscussionsDto {
    private Long id;

    private Boolean active;

    private LocalDateTime dateTime;

    private MessageDto latestMessage;

    private InterlocutorDto client;

    private InterlocutorDto transporter;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class MessageDto {

        private Long id;
        private String authorId;
        private String content;
        private LocalDateTime dateTime;
        private Boolean read;
        private Boolean sent;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class InterlocutorDto {
        private String id;
        private String name;
        private String mobileNumber;
        private String photoUrl;

    }
}
