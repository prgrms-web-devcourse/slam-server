package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courtId;
    private int startTime;

    @Builder
    public LoudSpeakerNotification(Long id, Long courtId, int startTime){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(courtId != null, "courtId는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.id = id;
        this.courtId = courtId;
        this.startTime = startTime;
    }

    public void createLoudSpeakerNotification(Long courtId, int startTime){
        checkArgument(courtId != null, "courtId는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.courtId = courtId;
        this.startTime = startTime;
    }
}
