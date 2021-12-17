package org.slams.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/17.
 */

@Getter
public class ConversationInfo {

    private final String content;
    private final Long userId;

    @Builder
    public ConversationInfo(String content, Long userId){
        this.content = content;
        this.userId = userId;
    }

}
