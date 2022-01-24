package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_index")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "follow_noti_id", referencedColumnName = "id")
    private FollowNotification followNotification;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loudspeaker_noti_id", referencedColumnName = "id")
    private LoudSpeaker loudSpeakerNotification;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(columnDefinition = "boolean default false")
    private boolean isClicked;

    @Column
    private Long checkCreatorId ;

    private Notification(Long userId, FollowNotification followNotification, Long checkCreatorId){
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.userId = userId;
        this.notificationType = NotificationType.FOLLOWING;
        this.followNotification = followNotification;
        this.checkCreatorId = checkCreatorId;
    }

    private Notification(Long userId, LoudSpeaker loudSpeakerNotification){
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.userId = userId;
        this.notificationType = NotificationType.LOUDSPEAKER;
        this.loudSpeakerNotification = loudSpeakerNotification;
    }

    public Notification(Long id, Long userId, FollowNotification followNotification,
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

    public Notification(Long id, Long userId, LoudSpeaker loudSpeakerNotification,
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

    public static Notification createLoudSpeakerNoti(Long userId, LoudSpeaker loudSpeakerNotification){
        return new Notification(userId, loudSpeakerNotification);

    }

    public static Notification createFollowNoti(Long userId, FollowNotification followNotification, Long checkCreatorId){
        return new Notification(userId, followNotification, checkCreatorId);
    }


}
