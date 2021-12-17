package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

/**
 * Created by yunyun on 2021/12/17.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_loudspeaker_contents")
public class ChatLoudSpeakerContent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable = false)
    private int startTime;


    @Builder
    public ChatLoudSpeakerContent(Long id, int startTime){
        this.id = id;
        this.startTime = startTime;
    }

    private ChatLoudSpeakerContent(int startTime){
        this.startTime = startTime;
    }

    public static ChatLoudSpeakerContent of(int startTime){
        return new ChatLoudSpeakerContent(startTime);
    }
}
