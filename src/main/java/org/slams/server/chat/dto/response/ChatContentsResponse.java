package org.slams.server.chat.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class ChatContentsResponse {
    private final Long courtId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final ConversationInfo conversationInfo;
    private final LoudSpeakerInfo loudSeapkerInfo;

    @Builder
    public ChatContentsResponse(
            Long courtId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            ConversationInfo conversationInfo,
            LoudSpeakerInfo loudSeapkerInfo
    ){
        this.courtId = courtId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.conversationInfo = conversationInfo;
        this.loudSeapkerInfo = loudSeapkerInfo;
    }
}
