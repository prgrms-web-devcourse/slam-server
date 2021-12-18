package org.slams.server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class ChatroomResponse {
    private final String courtName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final Long userChatRoomId;
    private final Long chatRoomId;

    @Builder
    public ChatroomResponse(
            String courtName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long chatRoomId,
            Long userChatRoomId){
        this.courtName = courtName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.chatRoomId = chatRoomId;
        this.userChatRoomId = userChatRoomId;
    }
}
