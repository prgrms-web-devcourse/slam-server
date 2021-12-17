package org.slams.server.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("conversationInfo")
    private final ConversationInfo conversationInfo;

    @JsonProperty("loudSpeakerInfo")
    private final LoudSpeakerInfo loudSpeakerInfo;

    @JsonProperty("chatContentType")
    private final ChatContentType chatContentType;

    @Builder
    public ChatContentsResponse(
            Long courtId,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            ConversationInfo conversationInfo,
            LoudSpeakerInfo loudSpeakerInfo,
            ChatContentType chatContentType
    ){
        this.courtId = courtId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.conversationInfo = conversationInfo;
        this.loudSpeakerInfo = loudSpeakerInfo;
        this.chatContentType = chatContentType;
    }
}
