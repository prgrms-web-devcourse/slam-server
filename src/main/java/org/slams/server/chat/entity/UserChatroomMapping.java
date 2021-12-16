package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "court_id", referencedColumnName = "id", nullable = false)
    private Court court;


    private UserChatroomMapping(User user, Court court){
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");
        checkArgument(user==null, "user는 null을 허용하지 않습니다.");
        this.user = user;
        this.court = court;
    }

    @Builder
    public UserChatroomMapping(
            Long id,
            User user,
            Court court
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");
        checkArgument(user==null, "user는 null을 허용하지 않습니다.");

        this.id = id;
        this.user = user;
        this.court = court;
    }

    public static UserChatroomMapping of(User user, Court court){
        return new UserChatroomMapping(user, court);
    }
}
