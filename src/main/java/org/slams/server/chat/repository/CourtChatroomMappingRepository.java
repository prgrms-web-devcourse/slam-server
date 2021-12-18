package org.slams.server.chat.repository;

import org.slams.server.chat.entity.CourtChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yunyun on 2021/12/17.
 */
public interface CourtChatroomMappingRepository extends JpaRepository<CourtChatroomMapping, Long> {

    @Transactional
    @Modifying()
    @Query("UPDATE CourtChatroomMapping c SET c.updateAt = current_timestamp WHERE c.court.id =:courtId")
    void updateUpdatedAtByCourtId(
      @Param("courtId") Long courtId
    );

    @Query("SELECT c FROM CourtChatroomMapping c WHERE c.court.id=:courtId")
    CourtChatroomMapping findByCourtId(
            @Param("courtId") Long courtId
    );
}
