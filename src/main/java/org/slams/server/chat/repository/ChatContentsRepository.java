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

    @Query("SELECT c FROM ChatContents c WHERE c.court.id = :courtId AND c.id >= :lastId ORDER BY c.createdAt DESC")
    List<ChatContents> findAllByCourtIdMoreThenLastIdByCreated(
            @Param("courtId") Long courtId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT c FROM ChatContents c WHERE c.court.id = :courtId ORDER BY c.createdAt DESC")
    List<ChatContents> findAllByCourtIdByCreated(
            @Param("courtId") Long courtId,
            Pageable pageable
    );

    @Query("SELECT c.id FROM ChatContents c WHERE c.court.id = :courtId AND c.id >= :lastId ORDER BY c.createdAt DESC")
    List<Long> findIdByCourtIdMoreThenLastIdByCreated(
            @Param("courtId") Long courtId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT c.id FROM ChatContents c WHERE c.court.id = :courtId ORDER BY c.createdAt DESC")
    List<Long> findIdByCourtIdByCreated(
            @Param("courtId") Long courtId,
            Pageable pageable
    );
}
