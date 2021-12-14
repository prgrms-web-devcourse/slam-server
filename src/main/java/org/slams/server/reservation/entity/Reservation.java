package org.slams.server.reservation.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.ReservationUpdateResponseDto;
import org.slams.server.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by dongsung on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
@Table(name="reservation")
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean hasBall;



    public void addReservation(Court court, User user) {
        this.court = court;
        this.user=user;
    }

    public void removeReservation() {
        this.court.removeReservation(this);
        this.court=null;
    }


    public void update(ReservationUpdateRequestDto request) {
        startTime=request.getStartTime();
        endTime=request.getEndTime();
        hasBall=request.getHasBall();
    }


    public Reservation(ReservationInsertRequestDto requestDto) {
        hasBall=requestDto.getHasBall();
        endTime= requestDto.getEndTime();
        startTime= requestDto.getStartTime();
    }


}
