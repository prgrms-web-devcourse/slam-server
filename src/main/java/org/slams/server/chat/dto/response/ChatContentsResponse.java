package org.slams.server.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.slams.server.chat.dto.response.subDto.Conversation;
import org.slams.server.chat.dto.response.subDto.Court;
import org.slams.server.chat.dto.response.subDto.Creator;
import org.slams.server.chat.dto.response.subDto.LoudSpeaker;

import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class ChatContentsResponse {
    private final Court court;

    @JsonProperty("conversation")
    private final Conversation conversation;

    @JsonProperty("loudSpeaker")
    private final LoudSpeaker loudSpeaker;

    private final Creator creator;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    @Builder
    public ChatContentsResponse(
            Court court,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Conversation conversation,
            LoudSpeaker loudSpeaker,
            Creator creator
    ){
        this.court = court;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.conversation = conversation;
        this.loudSpeaker = loudSpeaker;
        this.creator = creator;
    }
}
