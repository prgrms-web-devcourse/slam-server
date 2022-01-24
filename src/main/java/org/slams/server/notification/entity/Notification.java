package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.follow.entity.Follow;
import org.slams.server.notification.common.ValidationMessage;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "follow_id", referencedColumnName = "id")
    private Follow follow;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loudspeaker_id", referencedColumnName = "id")
    private LoudSpeaker loudSpeaker;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(columnDefinition = "boolean default false")
    private boolean isClicked;

    public Notification(Long id,
                        Long userId,
                        Follow follow,
                        LoudSpeaker loudSpeaker,
                        NotificationType type,
                        boolean isRead,
                        boolean isClicked){
        checkArgument(id != null, ValidationMessage.NOTNULL_ID);
        checkArgument(userId != null, ValidationMessage.NOTNULL_USERID);
        checkArgument(type != null, ValidationMessage.NOTNULL_NOTIFICATION_TYPE);
        this.id = id;
        this.userId = userId;
        this.follow = follow;
        this.loudSpeaker = loudSpeaker;
        this.isRead = isRead;
        this.isClicked = isClicked;
    }

    private Notification(
            Long userId,
            Follow follow,
            LoudSpeaker loudSpeaker,
            NotificationType type
    ){
        checkArgument(id != null, ValidationMessage.NOTNULL_ID);
        checkArgument(userId != null, ValidationMessage.NOTNULL_USERID);
        checkArgument(type != null, ValidationMessage.NOTNULL_NOTIFICATION_TYPE);
        this.userId = userId;
        this.follow = follow;
        this.loudSpeaker = loudSpeaker;
        this.type = type;
    }


    public static Notification createLoudSpeaker(Long userId, LoudSpeaker loudSpeaker){
        return new Notification(userId, null, loudSpeaker, NotificationType.LOUDSPEAKER);

    }

    public static Notification createFollow(Long userId,  Follow follow){
        return new Notification(userId, follow, null, NotificationType.FOLLOW);
    }


}