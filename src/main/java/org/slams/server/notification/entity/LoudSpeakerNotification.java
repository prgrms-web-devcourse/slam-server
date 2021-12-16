package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "loudspeaker_notification")
public class LoudSpeakerNotification extends BaseEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "court_id", nullable = false, referencedColumnName = "id")
    private Court court;

    @Column
    private int startTime;

    @Column
    private Long userId;

    @Column(columnDefinition = "boolean default false")
    private boolean isRead;

    @Column(columnDefinition = "boolean default false")
    private boolean isClicked;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private LoudSpeakerNotification(Court court, int startTime, Long userId, NotificationType notificationType){
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.id = UUID.randomUUID().toString();
        this.court = court;
        this.startTime = startTime;
        this.userId = userId;
        this.notificationType = notificationType;
    }


    @Builder
    public LoudSpeakerNotification(String id, Court court, int startTime, Long userId,
                                   boolean isRead, boolean isClicked, NotificationType notificationType){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(notificationType != null, "notificationType 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.id = id;
        this.court = court;
        this.startTime = startTime;
        this.userId = userId;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.notificationType = notificationType;
    }

    public static LoudSpeakerNotification of(Court court, int startTime, Long userId, NotificationType notificationType){
        return new LoudSpeakerNotification(court, startTime, userId, notificationType);
    }

    public void updateIsClicked(boolean isClicked){
        this.isClicked = isClicked;
    }

    public void updateIsRead(boolean isRead){
        this.isRead = isRead;
    }

}
