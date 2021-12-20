package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_index")
public class NotificationIndex extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_noti_id", referencedColumnName = "id")
    private FollowNotification followNotification;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loudspeaker_noti_id", referencedColumnName = "id")
    private LoudSpeakerNotification loudSpeakerNotification;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(columnDefinition = "boolean default false")
    private boolean isClicked;

    @Column
    private Long checkCreatorId ;

    private NotificationIndex(Long userId, FollowNotification followNotification, Long checkCreatorId){
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.userId = userId;
        this.notificationType = NotificationType.FOLLOWING;
        this.followNotification = followNotification;
        this.checkCreatorId = checkCreatorId;
    }

    private NotificationIndex(Long userId, LoudSpeakerNotification loudSpeakerNotification){
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.userId = userId;
        this.notificationType = NotificationType.LOUDSPEAKER;
        this.loudSpeakerNotification = loudSpeakerNotification;
    }

    public NotificationIndex(Long id, Long userId, FollowNotification followNotification,
                             NotificationType notificationType, boolean isRead, boolean isClicked, Long checkCreatorId){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        this.id = id;
        this.userId = userId;
        this.followNotification = followNotification;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.checkCreatorId = checkCreatorId;
    }

    public NotificationIndex(Long id, Long userId, LoudSpeakerNotification loudSpeakerNotification,
                             NotificationType notificationType, boolean isRead, boolean isClicked){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        this.id = id;
        this.userId = userId;
        this.loudSpeakerNotification = loudSpeakerNotification;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.isClicked = isClicked;
    }

    public static NotificationIndex createLoudSpeakerNoti(Long userId, LoudSpeakerNotification loudSpeakerNotification){
        return new NotificationIndex(userId, loudSpeakerNotification);

    }

    public static NotificationIndex createFollowNoti(Long userId, FollowNotification followNotification, Long checkCreatorId){
        return new NotificationIndex(userId, followNotification, checkCreatorId);
    }


}
