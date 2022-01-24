package org.slams.server.notification.repository;

import org.slams.server.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

public interface NotificationIndexRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT a.id FROM Notification a WHERE a.userId =:userId AND a.id < :lastId ORDER BY a.createdAt desc")
    List<Long> findIdByUserLessThanAlarmIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT a.id FROM Notification a WHERE a.userId =:userId ORDER BY a.createdAt desc")
    List<Long> findIdByUserByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("SELECT a FROM Notification a WHERE a.userId =:userId AND a.id < :lastId ORDER BY a.createdAt desc")
    List<Notification> findAllByUserLessThanAlarmIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT a FROM Notification a WHERE a.userId =:userId ORDER BY a.createdAt desc")
    List<Notification> findAllByUserByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Transactional
    @Modifying()
    @Query("UPDATE Notification n SET n.isClicked=:status WHERE n.userId=:userId")
    Integer updateIsClicked(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

    @Transactional
    @Modifying()
    @Query("UPDATE Notification n SET n.isRead=:status WHERE n.userId=:userId")
    Integer updateIsRead(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM Notification n WHERE n.checkCreatorId=:userId AND n.userId=:receiverId")
    void deleteByReceiverIdAndUserId(
            @Param("receiverId") Long receiverId,
            @Param("userId") Long userId
    );

    @Query("SELECT n FROM Notification n WHERE n.checkCreatorId=:creatorId AND n.userId=:receiverId")
    Notification findByReceiverIdAndCreatorId(
            @Param("receiverId") Long receiverId,
            @Param("creatorId") Long creatorId
    );

}
