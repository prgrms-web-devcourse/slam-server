package org.slams.server.chat.entity;

import lombok.*;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;
import org.slams.server.user.entity.User;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/03.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_chatroom_mapping")
public class UserChatroomMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "id", nullable = false)
    private CourtChatroomMapping courtChatroomMapping;

    @Column
    private Long courtId;

    private UserChatroomMapping(User user, CourtChatroomMapping courtChatroomMapping, Long courtId){
        checkArgument(courtChatroomMapping != null, "courtChatroomMapping는 null을 허용하지 않습니다.");
        checkArgument(user != null, "user는 null을 허용하지 않습니다.");
        this.user = user;
        this.courtChatroomMapping = courtChatroomMapping;
        this.courtId = courtId;
    }

    @Builder
    public UserChatroomMapping(
            Long id,
            User user,
            CourtChatroomMapping courtChatroomMapping,
            Long courtId
    ){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(courtChatroomMapping != null, "courtChatroomMapping 는 null을 허용하지 않습니다.");
        checkArgument(user != null, "user는 null을 허용하지 않습니다.");

        this.id = id;
        this.user = user;
        this.courtChatroomMapping = courtChatroomMapping;
        this.courtId = courtId;
    }

    public static UserChatroomMapping of(User user, CourtChatroomMapping courtChatroomMapping, Long courtId){
        return new UserChatroomMapping(user, courtChatroomMapping, courtId);
    }
}
