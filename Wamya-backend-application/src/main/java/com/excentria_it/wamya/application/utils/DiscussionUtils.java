package com.excentria_it.wamya.application.utils;

import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.InterlocutorDto;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.InterlocutorOutput;
import com.excentria_it.wamya.domain.LoadDiscussionsOutput.MessageOutput;

import java.time.ZoneId;

public class DiscussionUtils {

    private DiscussionUtils() {

    }

    public static LoadDiscussionsDto mapToLoadDiscussionsDto(DateTimeHelper dateTimeHelper, LoadDiscussionsOutput ldo,
                                                             ZoneId userZoneId) {
        return LoadDiscussionsDto.builder().id(ldo.getId()).active(ldo.getActive())
                .dateTime(dateTimeHelper.systemToUserLocalDateTime(ldo.getDateTime(), userZoneId))
                .latestMessage(mapToMessageDto(dateTimeHelper, ldo.getLatestMessage(), userZoneId))
                .client(mapToInterlocutorDto(ldo.getClient())).transporter(mapToInterlocutorDto(ldo.getTransporter()))
                .build();
    }

    public static MessageDto mapToMessageDto(DateTimeHelper dateTimeHelper, MessageOutput mo, ZoneId userZoneId) {
        if (mo == null)
            return null;
        return MessageDto.builder().id(mo.getId()).authorId(mo.getAuthorId()).content(mo.getContent())
                .dateTime(dateTimeHelper.systemToUserLocalDateTime(mo.getDateTime(), userZoneId)).read(mo.getRead())
                .build();
    }

    public static InterlocutorDto mapToInterlocutorDto(InterlocutorOutput io) {
        if (io == null)
            return null;
        return InterlocutorDto.builder().id(io.getId()).name(io.getName()).mobileNumber(io.getMobileNumber())
                .photoUrl(io.getPhotoUrl()).build();
    }
}
