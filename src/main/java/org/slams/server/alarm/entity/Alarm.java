package org.slams.server.alarm.entity;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;
import org.slams.server.user.entity.User;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "court_id", referencedColumnName = "id", nullable = false)
    private Court court;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User user;

    private long userId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;


    @Builder
    public Alarm(
            Long id,
            Court court,
            long userId,
            String content,
            AlarmType alarmType
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(alarmType==null, "alarmType는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");

        this.id = id;
        this.court = court;
        this.userId = userId;
        this.content = content;
        this.alarmType = alarmType;
    }

    public void updateContent(String content){
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alarm alarm = (Alarm) o;
        return userId == alarm.userId && Objects.equal(id, alarm.id) && Objects.equal(court, alarm.court) && Objects.equal(content, alarm.content) && alarmType == alarm.alarmType;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, court, userId, content, alarmType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Alarm{");
        sb.append("id=").append(id);
        sb.append(", court=").append(court);
        sb.append(", userId=").append(userId);
        sb.append(", content='").append(content).append('\'');
        sb.append(", alarmType=").append(alarmType);
        sb.append('}');
        return sb.toString();
    }
}
