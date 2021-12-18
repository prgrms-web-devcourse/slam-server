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


    @Query("SELECT r FROM Reservation r WHERE (r.startTime between :startLocalDateTime and :endLocalDateTime) and r.court.id=:courtId")
    List<Reservation> findAllByCourtAndDate(
            @Param("courtId")Long courtId,
            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
            @Param("endLocalDateTime") LocalDateTime endLocalDateTime
           );

    @Query("SELECT r FROM Reservation r WHERE r.user.id=:userId AND r.startTime>:localDateTime")
    List<Reservation> findByUserByNow(
            @Param("userId") Long userId,
            @Param ("localDateTime") LocalDateTime localDateTime);
    @Query("SELECT r.user.id FROM Reservation r WHERE r.court.id=:courtId")
    List<Long> findBeakerIdByCourtId(
            @Param("courtId") Long courtId
    );


//    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.court.id=:courtId")
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.court.id=:courtId AND ((r.startTime BETWEEN :startLocalDateTime AND :endLocalDateTime) OR (r.endTime BETWEEN :startLocalDateTime AND :endLocalDateTime))")
    Long findByDate(
            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
            @Param("endLocalDateTime") LocalDateTime endLocalDateTime,
            @Param("courtId") Long courtId
    );


}
