package org.slams.server.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/16.
 */

@Getter
public class CreateChatContentsRequest {
    private final String content;
    private final Long courtId;

    @Builder
    public CreateChatContentsRequest(String content, Long courtId){
        this.content = content;
        this.courtId = courtId;
    }
}
