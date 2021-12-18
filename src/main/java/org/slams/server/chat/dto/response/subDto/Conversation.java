package org.slams.server.chat.dto.response.subDto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/17.
 */

@Getter
public class Conversation {

    private final String content;

    @Builder
    public Conversation(String content){
        this.content = content;
    }

}
