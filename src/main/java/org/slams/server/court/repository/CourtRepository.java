package org.slams.server.court.repository;

import org.slams.server.court.entity.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourtRepository extends JpaRepository<Court, Long> {

//    @Query(value="select DISTINCT c from Court c left join fetch ")

    // court & reservation left join

}
