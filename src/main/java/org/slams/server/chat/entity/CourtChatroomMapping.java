package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/17.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "court_chatroom_mapping")
public class CourtChatroomMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "court_id", referencedColumnName = "id", nullable = false)
    private Court court;

    @Builder
    public CourtChatroomMapping(Long id, Court court){
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");

        this.id = id;
        this.court = court;
    }

    private CourtChatroomMapping(Court court){
        checkArgument(court != null, "court 정보는 null을 허용하지 않습니다.");

        this.court = court;
    }

    public static CourtChatroomMapping of(Court court){
        return new CourtChatroomMapping(court);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CourtChatroomMapping{");
        sb.append("id=").append(id);
        sb.append(", court=").append(court);
        sb.append('}');
        return sb.toString();
    }
}
