package org.slams.server.chat.dto.response.subDto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/17.
 */

@Getter
public class LoudSpeaker {
    private final int startTime;

    @Builder
    public LoudSpeaker(int startTime){
        this.startTime = startTime;
    }
}
