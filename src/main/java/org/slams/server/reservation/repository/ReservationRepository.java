package org.slams.server.reservation.repository;

import org.slams.server.court.entity.NewCourt;
import org.slams.server.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
