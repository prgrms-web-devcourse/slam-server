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

/**
 * Created by yunyun on 2021/12/15.
 */
public interface FollowNotificationRepository extends JpaRepository<FollowNotification, String> {

    @Query("select f FROM FollowNotification f WHERE f.id IN :messageIds")
    List<FollowNotification> findAllByNotificationIds(
            @Param("messageIds") List messageIds
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

}
