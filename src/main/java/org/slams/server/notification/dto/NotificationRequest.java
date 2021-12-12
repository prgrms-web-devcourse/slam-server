package org.slams.server.notification.dto;

import lombok.Getter;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
public class NotificationRequest {
    private final Long courtId;
    private final String courtName;
    private final String startDate;
    private final int startTime;
    private final long userId;
    private final Long reservationId;

    public NotificationRequest(Long courtId, String courtName, String startDate, int startTime, long userId, Long reservationId){
        checkArgument(courtId != null, "courtId는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(courtName), "courtName는 빈값을 허용하지 않습니다.");
        checkArgument(isNotEmpty(startDate), "startDate는 빈값을 허용하지 않습니다.");

        checkArgument(0 <= startTime && startTime < 25, "startTime은 0이상 24이하여야 합나다.");
        checkArgument(userId > 0, "userId는 0미만의 값을 허용하지 않습니다.");
        checkArgument(reservationId != null, "reservationId는 null을 허용하지 않습니다.");

        this.courtId = courtId;
        this.courtName = courtName;
        this.startDate = startDate;
        this.startTime = startTime;
        this.userId = userId;
        this.reservationId = reservationId;
    }
}
