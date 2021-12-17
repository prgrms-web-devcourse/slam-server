package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/17.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_conversation_contents")
public class ChatConversationContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private long userId;

    @Builder
    public ChatConversationContent(
            Long id,
            String content,
            long userId
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");

        this.id = id;
        this.content = content;
        this.userId = userId;
    }

    private ChatConversationContent(String content, long userId){
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");

        this.content = content;
        this.userId = userId;
    }

    public static ChatConversationContent of(String content, long userId){
        return new ChatConversationContent(content, userId);
    }

}
