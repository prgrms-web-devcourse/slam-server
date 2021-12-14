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

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode
@Getter
@Builder
public class ReservationInsertRequestDto {

    @NotNull(message = "코트 id는 필수 값입니다.")
    private Long courtId;
    @Future(message = "예약은 현재보다 미래만 가능합니다.")
    private LocalDateTime startTime;
    @Future(message = "예약은 현재보다 미래만 가능합니다.")
    private LocalDateTime endTime;
    @NotNull(message = "농구공 유무는 필수 값입니다.")
    private Boolean hasBall;

    // requestDto -> Entity
    public Reservation insertRequestDtoToEntity(ReservationInsertRequestDto requestDto) {
        return Reservation.builder()
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .hasBall(requestDto.getHasBall())
                .build();
    }
}
