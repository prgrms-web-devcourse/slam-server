package org.slams.server.notification.dto.response;

import lombok.Builder;
import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
public class LoudspeakerInfo {
    private final CourtInfo courtInfo;
    private final int startTime;

    @Builder
    public LoudspeakerInfo(
            CourtInfo courtInfo,
            int startTime) {
        checkArgument(courtInfo != null, "courtInfo는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.courtInfo = courtInfo;
        this.startTime = startTime;
    }
}
