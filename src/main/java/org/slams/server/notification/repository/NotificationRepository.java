package org.slams.server.notification.repository;

import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 읽음 표시 하기
    // 알림 저장하기 -> loudspeaker, follow
    // userID에 맞게 아이디 메시지 추출하기 -> 무한 스크롤

    @Query("SELECT a FROM Notification a WHERE a.userId =:userId AND a.id >= :lastId ORDER BY a.createdAt ASC")
    List<Long> findAllByUserMoreThenAlarmIdByCreated(
            @Param("userId") Long userId,
            @Param("lastId") Long lastId,
            Pageable pageable
    );

    @Query("SELECT a FROM Notification a WHERE a.userId =:userId ORDER BY a.createdAt ASC")
    List<Long> findAllByUserByCreated(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("UPDATE Notification n SET n.isClicked=:status WHERE n.userId=:userId")
    void updateIsClicked(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

    @Query("UPDATE Notification n SET n.isRead=:status WHERE n.userId=:userId")
    void updateIsRead(
            @Param("userId") Long userId,
            @Param("status") boolean status
    );

//    @Query("select a from Notification a where a.userId=:userId order by a.createdAt")
//    List<Notification> findAllByUserId(
//            @Param("userId") Long userId
//    );
//
//    @Query("select a FROM Notification a where a.userId =:userId and a.id >= :lastId order by a.createdAt ASC")
//    List<Notification> findAllByUserMoreThenAlarmIdByCreated(
//            @Param("userId") Long userId,
//            @Param("lastId") Long lastId,
//            Pageable pageable
//    );
//
//    @Query("select a FROM Notification a where a.userId =:userId order by a.createdAt ASC")
//    List<Notification> findAllByUserByCreated(
//            @Param("userId") Long userId,
//            Pageable pageable
//    );
}
