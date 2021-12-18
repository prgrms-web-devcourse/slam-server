package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.user.entity.User;

import javax.persistence.*;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow_notification")
public class FollowNotification extends BaseEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id", nullable = false, referencedColumnName = "id")
    private User creator;

    @Column
    private Long userId;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(columnDefinition = "boolean default false")
    private boolean isClicked;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private FollowNotification(User creator, Long userId, NotificationType notificationType){
        checkArgument(creator != null, "creator 정보는 null을 허용하지 않습니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.id = UUID.randomUUID().toString();
        this.creator = creator;
        this.userId = userId;
        this.notificationType = notificationType;
    }

    @Builder
    public FollowNotification(String id, User creator, Long userId, boolean isRead,
                              boolean isClicked, NotificationType notificationType){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(creator != null, "creator 정보는 null을 허용하지 않습니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.id = id;
        this.creator = creator;
        this.userId = userId;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.notificationType = notificationType;
    }

    public static FollowNotification of(User creator, Long userId, NotificationType notificationType){
        return new FollowNotification(creator, userId, notificationType);
    }

    public void updateIsClicked(boolean isClicked){
        this.isClicked = isClicked;
    }

    public void updateIsRead(boolean isRead){
        this.isRead = isRead;
    }
}
