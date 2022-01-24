package org.slams.server.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by yunyun on 2021/12/15.
 */
public interface FollowNotificationRepository extends JpaRepository<FollowNotification, Long> {


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
    @Query("DELETE FROM FollowNotification n WHERE n.checkCreatorId=:userId AND n.userId=:receiverId")
    void deleteByReceiverIdAndUserId(
            @Param("receiverId") Long receiverId,
            @Param("userId") Long userId
    );

}
