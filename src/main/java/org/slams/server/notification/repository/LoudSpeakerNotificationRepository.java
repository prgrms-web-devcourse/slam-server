package org.slams.server.notification.repository;

import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/12/15.
 */
public interface LoudSpeakerNotificationRepository extends JpaRepository<LoudSpeakerNotification, Long> {

    @Query("select l FROM LoudSpeakerNotification l WHERE l.id IN :messageIds")
    List<LoudSpeakerNotification> findAllByNotificationIds(
            @Param("messageIds") List messageIds
    );
}
