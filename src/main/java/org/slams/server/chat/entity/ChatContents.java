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
@Table(name = "chat_contents")
public class ChatContents extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column
    private ChatType chatType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "court_id", nullable = false, referencedColumnName = "id")
    private Court court;

    @OneToOne
    @JoinColumn(name = "conversation_id")
    private ChatConversationContents chatConversationContents;

    @OneToOne
    @JoinColumn(name = "loudspeaker_id")
    private ChatLoudSpeakerContents chatLoudSpeakerContents;

    @Builder
    public ChatContents(
            Long id,
            ChatType chatType,
            Court court,
            ChatConversationContents conversationContents,
            ChatLoudSpeakerContents chatLoudSpeakerContents
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(chatType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.id = id;
        this.chatType = chatType;
        this.court = court;
        this.chatConversationContents = conversationContents;
        this.chatLoudSpeakerContents = chatLoudSpeakerContents;
    }

    private ChatContents(
            ChatType chatType,
            Court court,
            ChatConversationContents conversationContents
    ){
        checkArgument(chatType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.chatType = chatType;
        this.court = court;
        this.chatConversationContents = conversationContents;
    }

    private ChatContents(
            ChatType chatType,
            Court court,
            ChatLoudSpeakerContents chatLoudSpeakerContents
    ){
        checkArgument(chatType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.chatType = chatType;
        this.court = court;
        this.chatLoudSpeakerContents = chatLoudSpeakerContents;
    }

    public static ChatContents createConversationContents(
            ChatType chatType,
            Court court,
            ChatConversationContents conversationContents
    ){
        return new ChatContents(chatType, court, conversationContents);
    }

    public static ChatContents createLoudspeakerContents(
            ChatType chatType,
            Court court,
            ChatLoudSpeakerContents chatLoudSpeakerContents
    ){
        return new ChatContents(chatType, court, chatLoudSpeakerContents);
    }

}
