package org.slams.server.reservation.repository;

import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.follow.entity.Follow;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.user.entity.User;
import org.springframework.data.domain.Pageable;
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

//    @Query("SELECT r FROM Reservation r WHERE r.user.id=:userId AND r.startTime>:localDateTime")
//    List<Reservation> findByUserByNow(
//            @Param("userId") Long userId,
//            @Param ("localDateTime") LocalDateTime localDateTime);
    // 수정
    @Query("SELECT r FROM Reservation r WHERE r.user.id=:userId AND r.startTime>:localDateTime")
    List<Reservation> findByUserByNow(
            @Param("userId") Long userId,
            @Param ("localDateTime") LocalDateTime localDateTime);

    @Query("SELECT r.user.id FROM Reservation r WHERE r.court.id=:courtId")
    List<Long> findBeakerIdByCourtId(
            @Param("courtId") Long courtId
    );


//    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.court.id=:courtId")
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.court.id=:courtId AND ((r.startTime BETWEEN :startLocalDateTime AND :endLocalDateTime) OR (r.endTime BETWEEN :startLocalDateTime AND :endLocalDateTime))GROUP BY r.user.id")
    Long findByDate(
            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
            @Param("endLocalDateTime") LocalDateTime endLocalDateTime,
            @Param("courtId") Long courtId
    );



    @Query("SELECT r FROM Reservation r WHERE r.court.id=:courtId AND (:sTime BETWEEN r.startTime AND r.endTime) OR (:eTime BETWEEN r.startTime AND r.endTime)")
    List<Reservation> findByReservation(
            @Param("courtId")Long courtId,
            @Param("sTime") LocalDateTime sTime,
            @Param("eTime") LocalDateTime eTime);

    // 유저의 지난 지난 예약 조회 (무한 스크롤 - 최초)
//    @Query("SELECT r FROM Reservation r WHERE r.user.id=:userId AND r.endTime<:localDateTime ORDER BY r.id desc")
//    List<Reservation> findByUserByExpiredOrderByDesc(
//            @Param("userId") Long userId,
//            @Param ("localDateTime") LocalDateTime localDateTime, Pageable pageable);

    // 수정
    @Query("SELECT r FROM Reservation r WHERE r.user.id=:userId AND r.endTime<:localDateTime ORDER BY r.id desc")
    List<Reservation> findByUserByExpiredOrderByDesc(
            @Param("userId") Long userId,
            @Param ("localDateTime") LocalDateTime localDateTime, Pageable pageable);


    // 유저의 지난 예약 목록 (무한 스크롤)
    @Query("SELECT r FROM Reservation r WHERE r.id < :lastId order by r.id desc")
    List<Reservation> findByUserByAndIdLessThanExpiredOrderByDesc(
            @Param("lastId") Long lastId, Pageable pageable);





}
