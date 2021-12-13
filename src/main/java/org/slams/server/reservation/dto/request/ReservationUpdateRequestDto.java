package org.slams.server.reservation.dto.request;

import lombok.Builder;
import lombok.Getter;
import org.slams.server.reservation.entity.Reservation;

@Getter
@Builder
public class ReservationUpdateRequestDto {

    private Long reservationId;
    private String startTime;
    private String endTime;
    private Boolean hasBall;

    // requestDto -> Entity
    public Reservation updateRequestDtoToEntity(ReservationUpdateRequestDto requestDto) {
        return Reservation.builder()
                .id(requestDto.getReservationId())
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .hasBall(requestDto.getHasBall())
                .build();
    }


}
