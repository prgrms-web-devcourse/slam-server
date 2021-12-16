package org.slams.server.chat.repository;

import org.slams.server.chat.entity.UserChatroomMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */
public interface UserChatRoomMappingRepository extends JpaRepository<UserChatroomMapping, Long> {

    @Query("SELECT u FROM UserChatroomMapping u WHERE u.user.id = :userId AND u.id >= :lastId ORDER BY u.createdAt ASC")
    List<UserChatroomMapping> findAllByUserIdMoreThenLastIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT u FROM UserChatroomMapping u WHERE u.user.id = :userId ORDER BY u.createdAt ASC")
    List<UserChatroomMapping> findAllByUserIdByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
