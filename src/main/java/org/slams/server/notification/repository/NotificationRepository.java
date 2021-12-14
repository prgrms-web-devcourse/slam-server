package org.slams.server.notification.repository;

import org.slams.server.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("select a from Notification a where a.userId=:userId order by a.createdAt")
    List<Notification> findAllByUserId(
            @Param("userId") Long userId
    );

    @Query("select a FROM Notification a where a.userId =:userId and a.id >= :lastId order by a.createdAt ASC")
    List<Notification> findAllByUserMoreThenAlarmIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("select a FROM Notification a where a.userId =:userId order by a.createdAt ASC")
    List<Notification> findAllByUserByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );
}
