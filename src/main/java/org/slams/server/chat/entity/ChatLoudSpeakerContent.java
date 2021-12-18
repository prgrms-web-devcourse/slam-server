package org.slams.server.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.slams.server.common.BaseEntity;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;

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
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.id = id;
        this.startTime = startTime;
    }

    private ChatLoudSpeakerContent(int startTime){
        checkArgument(0<= startTime && startTime<25, "경기 시작시간은 0이상 24시이하만 가능합니다.");

        this.startTime = startTime;
    }

    public static ChatLoudSpeakerContent of(int startTime){
        return new ChatLoudSpeakerContent(startTime);
    }
}
