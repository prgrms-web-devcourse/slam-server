package org.slams.server.reservation.repository;

import org.slams.server.court.entity.NewCourt;
import org.slams.server.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


//    @Query("selelct")
//    Optional<Long> findByUserIdCourtId(Long userId, Long courtId);

}
