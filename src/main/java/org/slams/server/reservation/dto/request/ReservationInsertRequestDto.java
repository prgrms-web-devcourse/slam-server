package org.slams.server.reservation.dto.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.entity.Status;
import org.slams.server.court.entity.Texture;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.user.entity.User;

@EqualsAndHashCode
@Getter
public class ReservationInsertRequestDto {

    private User user;

    private Long courtId;
    private String startTime;
    private String endTime;
    private Boolean hasBall;

    // requestDto -> Entity
    public Reservation insertRequestDtoToEntity(ReservationInsertRequestDto requestDto, User user) {
        return Reservation.builder()
                .user(user)
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .hasBall(requestDto.getHasBall())
                .build();
    }
}
