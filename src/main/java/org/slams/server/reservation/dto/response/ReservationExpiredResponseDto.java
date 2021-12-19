package org.slams.server.reservation.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.court.entity.Court;
import org.slams.server.follow.dto.FollowerResponse;
import org.slams.server.reservation.entity.Reservation;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationExpiredResponseDto {

    private Long reservationId;
    private Long courtId;
    private double latitude;
    private double longitude;
    private String courtName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long numberOfReservations;

    private ReservationExpiredResponseDto(Long reservationId, Long courtId, double latitude, double longitude,
                             String courtName, LocalDateTime startTime, LocalDateTime endTime,
                             LocalDateTime createdAt, LocalDateTime updatedAt, Long numOfReservations) {
        this.reservationId = reservationId;
        this.courtId = courtId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.courtName=courtName;
        this.startTime=startTime;
        this.endTime=endTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.numberOfReservations=numOfReservations;
    }

    public static ReservationExpiredResponseDto toResponse(Reservation reservation, Court court, LocalDateTime createdAt, LocalDateTime updatedAt, Long numOfReservations) {
        return new ReservationExpiredResponseDto(reservation.getId(), court.getId(), court.getLatitude(), court.getLongitude(),
                court.getName(),reservation.getStartTime(), reservation.getEndTime(),
                createdAt, updatedAt, numOfReservations);
    }
}
