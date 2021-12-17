package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.chat.dto.response.ChatContentType;
import org.slams.server.common.BaseEntity;
import org.slams.server.court.entity.Court;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

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
    @Enumerated(EnumType.STRING)
    private ChatContentType chatContentType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "court_id", nullable = false, referencedColumnName = "id")
    private Court court;

    @OneToOne
    @JoinColumn(name = "conversation_id")
    private ChatConversationContent chatConversationContent;

    @OneToOne
    @JoinColumn(name = "loudspeaker_id")
    private ChatLoudSpeakerContent chatLoudSpeakerContent;

    @Builder
    public ChatContents(
            Long id,
            ChatContentType chatContentType,
            Court court,
            ChatConversationContent conversationContent,
            ChatLoudSpeakerContent chatLoudSpeakerContent
    ){
        checkArgument(id==null, "id는 null을 허용하지 않습니다.");
        checkArgument(chatContentType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.id = id;
        this.chatContentType = chatContentType;
        this.court = court;
        this.chatConversationContent = conversationContent;
        this.chatLoudSpeakerContent = chatLoudSpeakerContent;
    }

    private ChatContents(
            ChatContentType chatContentType,
            Court court,
            ChatConversationContent chatConversationContent
    ){
        checkArgument(chatContentType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.chatContentType = chatContentType;
        this.court = court;
        this.chatConversationContent = chatConversationContent;
    }

    private ChatContents(
            ChatContentType chatContentType,
            Court court,
            ChatLoudSpeakerContent chatLoudSpeakerContent
    ){
        checkArgument(chatContentType==null, "chatType는 null을 허용하지 않습니다.");
        checkArgument(court==null, "court는 null을 허용하지 않습니다.");

        this.chatContentType = chatContentType;
        this.court = court;
        this.chatLoudSpeakerContent = chatLoudSpeakerContent;
    }

    public static ChatContents createConversationContent(
            ChatContentType chatContentType,
            Court court,
            ChatConversationContent conversationContent
    ){
        return new ChatContents(chatContentType, court, conversationContent);
    }

    public static ChatContents createLoudspeakerContent(
            ChatContentType chatContentType,
            Court court,
            ChatLoudSpeakerContent chatLoudSpeakerContent
    ){
        return new ChatContents(chatContentType, court, chatLoudSpeakerContent);
    }

}
