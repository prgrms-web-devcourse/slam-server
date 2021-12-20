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
public class FollowNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false, referencedColumnName = "id")
    private User creator;

    @Column
    private Long userId;

    @Column
    private Long checkCreatorId ;

    private FollowNotification(User creator, Long userId, Long checkCreatorId){
        checkArgument(creator != null, "creator 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.creator = creator;
        this.userId = userId;
        this.checkCreatorId = checkCreatorId;
    }

    @Builder
    public FollowNotification(Long id, User creator, Long userId, Long checkCreatorId){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(creator != null, "creator 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.id = id;
        this.creator = creator;
        this.userId = userId;
        this.checkCreatorId = checkCreatorId;
    }

    public static FollowNotification of(User creator, Long userId, Long checkCreatorId){
        return new FollowNotification(creator, userId, checkCreatorId);
    }

}
