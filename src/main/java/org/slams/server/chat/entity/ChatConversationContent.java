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


    @Builder
    public ChatConversationContent(
            Long id,
            String content

    ){
        checkArgument(id != null, "id는 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "대화내용은 빈값이 될 수 없습니다. ");
        this.id = id;
        this.content = content;
    }

    private ChatConversationContent(String content){
        checkArgument(isNotEmpty(content), "대화내용은 빈값이 될 수 없습니다. ");
        this.content = content;
    }

    public static ChatConversationContent of(String content){
        return new ChatConversationContent(content);
    }

}
