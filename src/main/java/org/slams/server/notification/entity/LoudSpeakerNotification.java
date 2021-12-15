package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.court.entity.Court;

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
@Table(name = "loudspeaker_notification")
public class LoudSpeakerNotification {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "court_id", nullable = false, referencedColumnName = "id")
    private Court court;

    @Column
    private int startTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", nullable = false, referencedColumnName = "id")
    private Notification notification;


    private LoudSpeakerNotification(Court court, int startTime, Notification notification){
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");
        this.id = UUID.randomUUID().toString();
        this.court = court;
        this.startTime = startTime;
        this.notification = notification;
    }


    @Builder
    public LoudSpeakerNotification(String id, Court court, int startTime, Notification notification){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.id = id;
        this.court = court;
        this.startTime = startTime;
        this.notification = notification;
    }

    public static LoudSpeakerNotification of(Court court, int startTime, Notification notification){
        return new LoudSpeakerNotification(court, startTime, notification);
    }

}
