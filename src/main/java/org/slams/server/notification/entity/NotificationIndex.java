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
    private String messageId;

    @Column(nullable = false)
    private Long userId;

    private NotificationIndex(String messageId, Long userId){
        checkArgument(messageId != null, "messageId는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.messageId = messageId;
        this.userId = userId;
    }

    @Builder
    public NotificationIndex(Long id, String messageId, Long userId){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(messageId != null, "messageId는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId는 null을 허용하지 않습니다.");
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
    }

    public static NotificationIndex of(String messageId, Long userId){
        return new NotificationIndex(messageId, userId);
    }
}
