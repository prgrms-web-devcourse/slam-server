package org.slams.server.chat.convertor;

import org.slams.server.chat.dto.response.subDto.*;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.entity.ChatContents;
import org.slams.server.chat.exception.InvalidChatTypeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/16.
 */

@Component
public class ChatContentConvertor {

    public List<ChatContentsResponse> toDtoList(List<ChatContents> chatContentsList){
            return chatContentsList.stream()
                    .map(v -> toDto(v))
                    .collect(Collectors.toList());
    }

    public ChatContentsResponse toDto(ChatContents chatContents){
        if (chatContents == null){
            throw new NullPointerException("chatContents는 null을 허용하지 않습니다.");
        }
        if (chatContents.getChatContentType().equals(ChatContentType.CONVERSATION)){
            return ChatContentsResponse.builder()
                    .court(Court.builder()
                            .id(chatContents.getCourt().getId())
                            .name(chatContents.getCourt().getName())
                            .build()
                    )
                    .creator(Creator.builder()
                            .id(chatContents.getUser().getId())
                            .nickname(chatContents.getUser().getNickname())
                            .image(chatContents.getUser().getProfileImage())
                            .build()
                    )
                    .conversation(Conversation.builder()
                            .content(chatContents.getChatConversationContent().getContent())
                            .build()
                    )
                    .createdAt(chatContents.getCreatedAt())
                    .updatedAt(chatContents.getUpdateAt())
                    .build();
        }
        if (chatContents.getChatContentType().equals(ChatContentType.LOUDSPEAKER)){
            return ChatContentsResponse.builder()
                    .court(Court.builder()
                            .id(chatContents.getCourt().getId())
                            .name(chatContents.getCourt().getName())
                            .build()
                    )
                    .creator(Creator.builder()
                            .id(chatContents.getUser().getId())
                            .nickname(chatContents.getUser().getNickname())
                            .image(chatContents.getUser().getProfileImage())
                            .build()
                    )
                    .loudSpeaker(LoudSpeaker.builder()
                            .startTime(chatContents.getChatLoudSpeakerContent().getStartTime())
                            .build())
                    .createdAt(chatContents.getCreatedAt())
                    .updatedAt(chatContents.getUpdateAt())
                    .build();
        }

        throw new InvalidChatTypeException("유효한 chat type이 아닙니다.");
    }



}
