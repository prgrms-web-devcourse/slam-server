package org.slams.server.reservation.dto.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.reservation.entity.Reservation;
@Getter
@EqualsAndHashCode
public class ReservationDeleteResponseDto {

    private Long reservationId;

    public ReservationDeleteResponseDto(Reservation reservation) {
        reservationId=reservation.getId();
    }

}
