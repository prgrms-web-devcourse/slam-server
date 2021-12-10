package org.slams.server.alarm.entity;

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
@Table(name = "alarm")
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private Long courtId;

    private Long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Builder
    public Alarm(
            Long id,
            Long courtId,
            String content,
            Long userId,
            AlarmType alarmType
    ){
        checkArgument(userId > -1, "userId 0미만은 허용하지 않습니다.");
        checkArgument(alarmType != null, "alarmType는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");
        if(alarmType.equals(AlarmType.LOUDSPEAKER)){
            checkArgument(courtId != null, "확성기 정보에서의 court는 null을 허용하지 않습니다.");
        }

        this.id = id;
        this.courtId = courtId;
        this.userId = userId;
        this.alarmType = alarmType;
        this.content = content;
    }

    private Alarm(
            Long courtId,
            Long userId,
            AlarmType alarmType
    ){
        checkArgument(userId > -1, "userId 0미만은 허용하지 않습니다.");
        checkArgument(alarmType != null, "alarmType는 null을 허용하지 않습니다.");

        this.courtId = courtId;
        this.userId = userId;
        this.alarmType = alarmType;
    }

    public static Alarm createAlarmForFollowing(
            Long courtId,
            Long userId,
            String nickName
    ){
        Alarm alarm = new Alarm(courtId, userId, AlarmType.FOLLOWING_ALARM);
        alarm.createContentForFollowing(nickName);
        return alarm;
    }

    public static Alarm createAlarmForLoudSpeaker(
            Long courtId,
            Long userId,
            int startTime,
            String courtName
    ){
        Alarm alarm = new Alarm(courtId, userId, AlarmType.LOUDSPEAKER);
        alarm.createContentForLoudSpeaker(startTime, courtName);
        return alarm;
    }


    public void updateContent(String content){
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");
        this.content = content;
    }

    private void createContentForFollowing(String nicknameOfUserFollowed){
        checkArgument(isNotEmpty(nicknameOfUserFollowed), "팔로운된 사용자 닉네임은 빈값을 허용하지 않습니다.");
        this.content =  String.format("%s가 당신을 팔로우하였습니다.",nicknameOfUserFollowed);
    }

    private void createContentForLoudSpeaker(int startTime, String courtName){
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");
        checkArgument(isNotEmpty(courtName), "농구장 이름은 빈값을 허용하지 않습니다.");
        this.content =  String.format("%d시에 시작하는 %s에서 사람을 구합니다.",startTime, courtName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return Objects.equal(id, alarm.id) && Objects.equal(courtId, alarm.courtId) && Objects.equal(userId, alarm.userId) && Objects.equal(content, alarm.content) && alarmType == alarm.alarmType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, courtId, userId, content, alarmType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Alarm{");
        sb.append("id=").append(id);
        sb.append(", courtId=").append(courtId);
        sb.append(", userId=").append(userId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", alarmType=").append(alarmType);
        sb.append('}');
        return sb.toString();
    }
}
