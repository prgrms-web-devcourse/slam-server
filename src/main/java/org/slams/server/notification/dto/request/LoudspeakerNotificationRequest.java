package org.slams.server.notification.dto.request;

import lombok.Getter;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
public class LoudspeakerNotificationRequest {
    private final Long courtId;
    private final int startTime;
    private final Long reservationId;

    public LoudspeakerNotificationRequest(Long courtId, int startTime, Long reservationId){
        /** websocket으로 runtimeException을 낼 경우, 세션 끊어지는가?
         *  혹시 모르니까, 객체에 값을 넣기 전에 유효성 검사를 하자.
         */
        this.courtId = courtId;
        this.startTime = startTime;
        this.reservationId = reservationId;
    }
}
