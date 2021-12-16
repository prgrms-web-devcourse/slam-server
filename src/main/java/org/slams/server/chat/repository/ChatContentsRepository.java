package org.slams.server.chat.repository;

import org.slams.server.chat.entity.ChatContents;
import org.slams.server.chat.entity.UserChatroomMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */
public interface ChatContentsRepository extends JpaRepository<ChatContents, Long> {
    @Query("SELECT c FROM ChatContents c WHERE c.courtId = :courtId AND c.id >= :lastId ORDER BY c.createdAt ASC")
    List<ChatContents> findAllByUserIdMoreThenLastIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT c FROM ChatContents c WHERE c.courtId = :courtId ORDER BY c.createdAt ASC")
    List<ChatContents> findAllByUserIdByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
