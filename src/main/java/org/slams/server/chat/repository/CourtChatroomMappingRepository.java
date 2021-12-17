package org.slams.server.chat.repository;

import org.slams.server.chat.entity.CourtChatroomMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by yunyun on 2021/12/17.
 */
public interface CourtChatroomMappingRepository extends JpaRepository<CourtChatroomMapping, Long> {

    @Query("UPDATE CourtChatroomMapping c SET c.updateAt = current_timestamp WHERE c.court.id =:courtId")
    void updateUpdatedAtByCourtId(
      @Param("courtId") Long courtId
    );
}
