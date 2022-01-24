package org.slams.server.notification.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.court.entity.Court;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/14.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "loudspeaker_notification")
public class LoudSpeaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "court_id", nullable = false, referencedColumnName = "id")
    private Court court;

    @Column
    private int startTime;

    @Column
    private Long userId;

    private LoudSpeaker(Court court, int startTime, Long userId){
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");

        this.court = court;
        this.startTime = startTime;
        this.userId = userId;
    }

    @Builder
    public LoudSpeaker(Long id, Court court, int startTime, Long userId){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");
        checkArgument(userId != null, "userId 정보는 null을 허용하지 않습니다.");
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.id = id;
        this.court = court;
        this.startTime = startTime;
        this.userId = userId;
    }

    public static LoudSpeaker of(Court court, int startTime, Long userId){
        return new LoudSpeaker(court, startTime, userId);
    }


}
