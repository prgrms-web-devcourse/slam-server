package org.slams.server.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class ReservationInsertRequestDto {


    private Long courtId;
    private String startTime;
    private String endTime;
    private Boolean hasBall;

//    public ReservationInsertRequestDto(ReservationInsertRequestDto requestDto, User user) {
//        courtId= requestDto.getCourtId();
//        startTime=requestDto.getStartTime();
//        endTime= requestDto.getEndTime();
//        hasBall=requestDto.getHasBall();
//    }

    // requestDto -> Entity
    public Reservation insertRequestDtoToEntity(ReservationInsertRequestDto requestDto) {
        return Reservation.builder()
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .hasBall(requestDto.getHasBall())
                .build();
    }
}
