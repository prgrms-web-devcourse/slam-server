package org.slams.server.notification.entity;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column
    private Long userId;

    @Column(columnDefinition = "default false")
    private boolean isRead;

    @Column(columnDefinition = "default false")
    private boolean isClicked;

//    private String messageId;

    private Notification(NotificationType notificationType, Long userId){
        this.notificationType = notificationType;
        this.userId = userId;
    }

    @Builder
    public Notification(Long id, NotificationType notificationType, Long userId, boolean isClicked, boolean isRead){
        this.id = id;
        this.notificationType = notificationType;
        this.userId = userId;
        this.isClicked = isClicked;
        this.isRead = isRead;
    }

    public static Notification of(NotificationType notificationType, Long userId){
        return new Notification(notificationType, userId);
    }
}
