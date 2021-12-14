package org.slams.server.chat.entity;

import com.google.common.base.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Table(name = "chatDetails")
public class ChatDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "chat_title_id", referencedColumnName = "id", nullable = false)
    private ChatTitle chatTitle;

//    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
//    private User user;

    private long userId;

    @Builder
    public ChatDetails(
            Long id,
            String content,
            ChatTitle chatTitle,
            long userId
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(userId < 0, "userId 0미만은 허용하지 않습니다.");
        checkArgument(chatTitle==null, "chatTitle은 null을 허용하지 않습니다.");
        checkArgument(isNotEmpty(content), "content는 빈값을 허용하지 않습니다.");
        this.id = id;
        this.content = content;
        this.chatTitle = chatTitle;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDetails that = (ChatDetails) o;
        return userId == that.userId && Objects.equal(id, that.id) && Objects.equal(content, that.content) && Objects.equal(chatTitle, that.chatTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, content, chatTitle, userId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChatDetails{");
        sb.append("id=").append(id);
        sb.append(", content='").append(content).append('\'');
        sb.append(", chatTitle=").append(chatTitle);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
