package org.slams.server.notification.repository;

import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Native;
import java.util.List;
import java.util.Optional;

/**
 * Created by yunyun on 2021/12/15.
 */
public interface FollowNotificationRepository extends JpaRepository<FollowNotification, String> {

    @Query("SELECT f FROM FollowNotification f WHERE f.id IN :messageIds")
    List<FollowNotification> findAllByNotificationIds(
            @Param("messageIds") List messageIds
    );

    @Query("SELECT f FROM FollowNotification f WHERE f.id=:messageId")
    Optional<FollowNotification> findOneById(
            @Param("messageId") String messageId
    );

    @Transactional
    @Modifying()
    @Query("UPDATE FollowNotification n SET n.isClicked=:status WHERE n.userId=:userId")
    void updateIsClicked(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

    @Transactional
    @Modifying()
    @Query("UPDATE FollowNotification n SET n.isRead=:status WHERE n.userId=:userId")
    void updateIsRead(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

    @Query("SELECT f.id FROM FollowNotification f WHERE f.creator.id=:userId AND f.userId=:receiverId")
    List<String> findByReceiverIdAndUserId(
            @Param("receiverId") Long receiverId,
            @Param("userId") Long userId
    );

    @Query("SELECT f FROM FollowNotification f WHERE f.creator.id=:userId AND f.userId=:receiverId")
    Optional<FollowNotification> findOneByReceiverIdAndUserId(
            @Param("receiverId") Long receiverId,
            @Param("userId") Long userId
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM FollowNotification f WHERE f.creator.id=:userId AND f.userId=:receiverId")
    void deleteByReceiverIdAndUserId(
            @Param("receiverId") Long receiverId,
            @Param("userId") Long userId
    );

//    @Transactional
//    @Modifying()
//    @Query("UPDATE FollowNotification n SET n.isDeleted=:status WHERE n.userId=:receiverId")
//    void updateIsDeleted(
//            @Param("receiverId") Long receiverId,
//            @Param("status") boolean status
//    );
}
