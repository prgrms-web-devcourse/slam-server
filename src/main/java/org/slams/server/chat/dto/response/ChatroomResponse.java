package org.slams.server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class ChatroomResponse {
    private final Long courtId;
    private final String courtName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ChatroomResponse(
            Long courtId,
            String courtName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        this.courtId = courtId;
        this.courtName = courtName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
