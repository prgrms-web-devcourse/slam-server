package org.slams.server.court.repository;

import org.slams.server.court.entity.Court;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
