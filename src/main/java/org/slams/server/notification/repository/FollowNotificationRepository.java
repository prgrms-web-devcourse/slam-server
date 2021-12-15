package org.slams.server.notification.repository;

import org.slams.server.notification.entity.FollowNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/12/15.
 */
public interface FollowNotificationRepository extends JpaRepository<FollowNotification, Long> {

    @Query("select f FROM FollowNotification f WHERE f.id IN :messageIds")
    List<FollowNotification> findAllByNotificationIds(
            @Param("messageIds") List messageIds
    );
}
