package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;

import javax.persistence.*;

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

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "court_id", referencedColumnName = "id", nullable = false)
    private Court court;

    @Builder
    public CourtChatroomMapping(Long id, Court court){
        this.id = id;
        this.court = court;
    }

    private CourtChatroomMapping(Court court){
        this.court = court;
    }

    public static CourtChatroomMapping of(Court court){
        return new CourtChatroomMapping(court);
    }
}
