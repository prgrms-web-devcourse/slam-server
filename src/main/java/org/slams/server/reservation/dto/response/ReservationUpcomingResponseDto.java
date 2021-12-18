package org.slams.server.reservation.dto.response;

import lombok.Getter;
import org.slams.server.common.api.BaseResponse;
import org.slams.server.reservation.entity.Reservation;

import java.time.LocalDateTime;

@Getter
public class ReservationUpcomingResponseDto extends BaseResponse {
    private Long reservationId;
    private Long courtId;
    private String courtName;
    private double latitude;
    private double longitude;
    private int basketCount;
    private int numberOfReservations;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public ReservationUpcomingResponseDto(Reservation reservation) {
        super(reservation.getCreatedAt(), reservation.getUpdateAt());
        reservationId=reservation.getId();
        courtId=reservation.getCourt().getId();
        courtName=reservation.getCourt().getName();
        latitude=reservation.getCourt().getLatitude();
        longitude=reservation.getCourt().getLongitude();
        basketCount=reservation.getCourt().getBasketCount();
        numberOfReservations=reservation.getCourt().getReservations().size();
        startTime=reservation.getStartTime();
        endTime=reservation.getEndTime();
    }
}
