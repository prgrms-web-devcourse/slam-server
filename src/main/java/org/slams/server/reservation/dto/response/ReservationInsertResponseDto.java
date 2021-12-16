package org.slams.server.reservation.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@EqualsAndHashCode
public class ReservationInsertResponseDto extends BaseResponse {

    private Long reservationId;
    private Long userId;
    private Long courtId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean hasBall;

    // Entity -> Response
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
