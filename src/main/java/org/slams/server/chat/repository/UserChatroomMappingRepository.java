package org.slams.server.chat.repository;

import org.slams.server.chat.entity.UserChatroomMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */
public interface UserChatroomMappingRepository extends JpaRepository<UserChatroomMapping, Long> {

    @Query("SELECT u FROM UserChatroomMapping u WHERE u.user.id = :userId AND u.id >= :lastId ORDER BY u.courtChatroomMapping.updateAt ASC")
    List<UserChatroomMapping> findAllByUserIdMoreThenLastIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT u FROM UserChatroomMapping u WHERE u.user.id = :userId ORDER BY u.courtChatroomMapping.updateAt DESC")
    List<UserChatroomMapping> findAllByUserIdByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Transactional
    @Modifying()
    @Query("DELETE FROM UserChatroomMapping u WHERE u.courtId=:courtId AND u.user.id=:userId")
    void deleteUserChatRoomByCourtId(
            @Param("courtId") Long courtId,
            @Param("userId") Long userId
    );

    @Query("SELECT u.id FROM UserChatroomMapping u WHERE u.user.id = :userId AND u.id >= :lastId ORDER BY u.courtChatroomMapping.updateAt ASC")
    List<Long> findIdByUserIdMoreThenLastIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT u.id FROM UserChatroomMapping u WHERE u.user.id = :userId ORDER BY u.courtChatroomMapping.updateAt DESC")
    List<Long> findIdByUserIdByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
