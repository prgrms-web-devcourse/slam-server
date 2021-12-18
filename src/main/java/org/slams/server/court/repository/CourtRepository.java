package org.slams.server.court.repository;

import org.slams.server.court.dto.response.CourtReservationResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.favorite.entity.Favorite;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CourtRepository extends JpaRepository<Court, Long> {


//    @Query("SELECT c FROM Court c " +
//            "INNER JOIN Reservation r ON c.id=r.court.id " +
//            "WHERE (c.latitude BETWEEN :startLatitude AND :endLatitude) " +
//            "AND (c.longitude BETWEEN :startLongitude AND :endLongitude) " +
//            "AND (r.startTime BETWEEN :startLocalDateTime AND :endLocalDateTime) " +
//            "OR (r.endTime BETWEEN :startLocalDateTime AND :endLocalDateTime) " +
//            "GROUP BY c.id")
//    List<Court> findAllByDateByBoundary(
//            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
//            @Param("endLocalDateTime") LocalDateTime endLocalDateTime,
//            @Param("startLatitude") double startLatitude,
//            @Param("endLatitude") double endLatitude,
//            @Param("startLongitude") double startLongitude,
//            @Param("endLongitude") double endLongitude
//    );


    @Query("SELECT c FROM Court c " +
            "WHERE (c.latitude BETWEEN :startLatitude AND :endLatitude) " +
            "AND (c.longitude BETWEEN :startLongitude AND :endLongitude)")
    List<Court> findByBoundary(
            @Param("startLatitude") double startLatitude,
            @Param("endLatitude") double endLatitude,
            @Param("startLongitude") double startLongitude,
            @Param("endLongitude") double endLongitude
    );





//    @Query("SELECT c FROM Court c " +
//            "LEFT JOIN Reservation r ON c.id=r.court.id " +
//            "WHERE (c.latitude BETWEEN :startLatitude AND :endLatitude) " +
//            "AND (c.longitude BETWEEN :startLongitude AND :endLongitude) " +
//            // Having
//            "GROUP BY c.id" +
//            "HAVING r. BETWEEN :startLocalDateTime AND :endLocalDateTime)  +
//            "            \"OR (r.endTime BETWEEN :startLocalDateTime AND :endLocalDateTime) \" +" +
//            "" +
//            "")
//    List<Court> findAllByDateByBoundary(
//            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
//            @Param("endLocalDateTime") LocalDateTime endLocalDateTime,
//            @Param("startLatitude") double startLatitude,
//            @Param("endLatitude") double endLatitude,
//            @Param("startLongitude") double startLongitude,
//            @Param("endLongitude") double endLongitude
//    );


}
