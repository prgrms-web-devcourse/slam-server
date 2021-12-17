package org.slams.server.court.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.Texture;
import org.slams.server.favorite.entity.Favorite;
import org.slams.server.reservation.entity.Reservation;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class CourtReservationResponseDto {

    Long reservationId;
    Long userId;
    String avatarImgSrc;
    Long courtId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Boolean hasBall;


    public CourtReservationResponseDto(Reservation reservation) {
        reservationId=reservation.getId();
        userId=reservation.getUser().getId();
        avatarImgSrc=reservation.getUser().getProfileImage();
        courtId=reservation.getCourt().getId();
        startTime=reservation.getStartTime();
        endTime=reservation.getEndTime();
        hasBall=reservation.isHasBall();
    }


}
