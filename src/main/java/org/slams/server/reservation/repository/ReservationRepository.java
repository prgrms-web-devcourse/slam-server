package org.slams.server.reservation.repository;

import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


//    @Query("selelct")
//    Optional<Long> findByUserIdCourtId(Long userId, Long courtId);
    // date 가 2012-11-08
    // r.startTime -> 2012-11-08-09
    // r.end Time -> 2012-11-08-11시
//    @Query("SELECT r FROM Reservation r WHERE r.startTime>=:date and r.endTime<=:date and r.court.id=:courtId")
//    List<Reservation> findAllByCourtAndDate(@Param("date") LocalDateTime date, @Param("courtId")Long courtId);


//    @Query("SELECT r FROM Reservation r WHERE r.court.id=:courtId")
//    List<Reservation> findAllByCourtAndDate(@Param("courtId")Long courtId);

    @Query("SELECT r FROM Reservation r WHERE (r.startTime between :startLocalDateTime and :endLocalDateTime) and r.court.id=:courtId")
    List<Reservation> findAllByCourtAndDate(
            @Param("courtId")Long courtId,
            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
            @Param("endLocalDateTime") LocalDateTime endLocalDateTime
           );

    @Query("SELECT r.user.id FROM Reservation r WHERE r.court.id=:courtId")
    List<Long> findBeakerIdByCourtId(
            @Param("courtId") Long courtId
    );


}
