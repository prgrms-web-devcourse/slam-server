package org.slams.server.reservation.dto.response;

import lombok.EqualsAndHashCode;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.entity.Reservation;


@EqualsAndHashCode
public class ReservationInsertResponseDto extends BaseResponse {

    private Long reservationId;
    private Long userId;
    private Long courtId;
    private String startTime;
    private String endTime;
    private Boolean hasBall;

    // requestDto -> Entity
    public ReservationInsertResponseDto(Reservation reservation) {
        super(reservation.getCreatedAt(), reservation.getUpdateAt());
        reservationId=reservation.getId();
        userId=reservation.getUser().getId();
        courtId=reservation.getCourt().getId();
        startTime=reservation.getStartTime();
        endTime=reservation.getEndTime();
        hasBall=reservation.isHasBall();
    }


}
