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

//    @Query(value="select DISTINCT c from Court c left join fetch ")

    // court & reservation left join

//    @Query("SELECT p FROM Post p WHERE p.user.id IN(:userIdList) and p.id < :lastId order by p.id desc")
//    List<Court> findByUserIdListAndIdLessThanOrderByIdDesc(
//            @Param("userIdList") List<Long> userIdList,
//            @Param("lastId") Long lastId,
//            Pageable pageable
//    );

    // courtId에 의한 조회
//    <Optional>Court findByCourt(Court court);


//    List<Favorite> findAllByUser(User user);

    @Query("SELECT c FROM Court c " +
            "INNER JOIN Reservation r ON c.id=r.court.id " +
            "WHERE (c.latitude BETWEEN :startLatitude AND :endLatitude) " +
            "AND (c.longitude BETWEEN :startLongitude AND :endLongitude) " +
            "AND (r.startTime BETWEEN :startLocalDateTime AND :endLocalDateTime) " +
            "OR (r.endTime BETWEEN :startLocalDateTime AND :endLocalDateTime) " +
            "GROUP BY c.id")
    List<Court> findAllByDateByBoundary(
            @Param("startLocalDateTime") LocalDateTime startLocalDateTime,
            @Param("endLocalDateTime") LocalDateTime endLocalDateTime,
            @Param("startLatitude") double startLatitude,
            @Param("endLatitude") double endLatitude,
            @Param("startLongitude") double startLongitude,
            @Param("endLongitude") double endLongitude
    );


}
