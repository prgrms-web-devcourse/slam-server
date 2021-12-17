package org.slams.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by yunyun on 2021/12/17.
 */

@Getter
public class LoudSpeakerInfo {
    private final int startTime;

    @Builder
    public LoudSpeakerInfo(int startTime){
        this.startTime = startTime;
    }
}
