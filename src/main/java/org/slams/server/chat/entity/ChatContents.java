package org.slams.server.chat.entity;

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
@Table(name = "chat_contents")
public class ChatContents extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String content;

    private long userId;

    private long courtId;

    @Builder
    public ChatContents(
            Long id,
            String content,
            long userId,
            long courtId
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(courtId < 0, "courtId 0미만은 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");

        this.id = id;
        this.content = content;
        this.userId = userId;
        this.courtId = courtId;
    }

    private ChatContents(String content, long userId, long courtId){
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(courtId < 0, "courtId 0미만은 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");

        this.content = content;
        this.userId = userId;
        this.courtId = courtId;
    }

    public static ChatContents of(String content, long userId, long courtId){
        return new ChatContents(content, userId, courtId);
    }

}
