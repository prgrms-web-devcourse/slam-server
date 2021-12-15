package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.user.entity.User;

import javax.persistence.*;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "follow_notification")
public class FollowNotification {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "follower_id", nullable = false, referencedColumnName = "id")
    private User follower;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", nullable = false, referencedColumnName = "id")
    private Notification notification;

    private FollowNotification(User follower, Notification notification){
        checkArgument(follower != null, "follower 정보는 null을 허용하지 않습니다.");
        this.id = UUID.randomUUID().toString();
        this.follower = follower;
        this.notification = notification;
    }

    @Builder
    public FollowNotification(String id, User follower, Notification notification){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(follower != null, "follower 정보는 null을 허용하지 않습니다.");

        this.id = id;
        this.follower = follower;
        this.notification = notification;
    }

    public static FollowNotification of(User follower, Notification notification){
        checkArgument(follower != null, "follower 정보는 null을 허용하지 않습니다.");
        return new FollowNotification(follower, notification);
    }
}
