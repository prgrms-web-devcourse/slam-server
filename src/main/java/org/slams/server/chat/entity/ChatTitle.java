package org.slams.server.chat.entity;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/03.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatTitle")
public class ChatTitle extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "court_id", nullable = false)
    private Court court;

    @Builder
    public ChatTitle(
            Long id,
            Court court
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.id = id;
        this.court = court;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatTitle chatTitle = (ChatTitle) o;
        return Objects.equal(id, chatTitle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatTitle{");
        sb.append("id=").append(id);
        sb.append(", court=").append(court);
        sb.append('}');
        return sb.toString();
    }
}
