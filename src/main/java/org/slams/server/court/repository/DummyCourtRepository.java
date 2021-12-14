package org.slams.server.court.repository;

import org.slams.server.court.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyCourtRepository extends JpaRepository<Court, Long> {
}
