package org.slams.server.court.repository;

import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewCourtRepository extends JpaRepository<NewCourt, Long> {
}
