package org.slams.server.chat.convertor;

import org.slams.server.chat.dto.ChatContentsRequest;
import org.slams.server.chat.dto.ChatContentsResponse;
import org.slams.server.chat.dto.ConversationInfo;
import org.slams.server.chat.dto.LoudSpeakerInfo;
import org.slams.server.chat.entity.ChatContents;
import org.slams.server.chat.entity.ChatType;
import org.slams.server.chat.exception.InvalidChatTypeException;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
        if (chatContents.getChatType().equals(ChatType.CONVERSATION)){
            return ChatContentsResponse.builder()
                    .courtId(chatContents.getCourt().getId())
                    .createdAt(chatContents.getCreatedAt())
                    .updatedAt(chatContents.getCreatedAt())
                    .conversationInfo(ConversationInfo.builder()
                            .content(chatContents.getChatConversationContent().getContent())
                            .userId(chatContents.getChatConversationContent().getUserId())
                            .build()
                    )
                    .build();
        }
        if (chatContents.getChatType().equals(ChatType.LOUDSPEAKER)){
            return ChatContentsResponse.builder()
                    .courtId(chatContents.getCourt().getId())
                    .createdAt(chatContents.getCreatedAt())
                    .updatedAt(chatContents.getUpdateAt())
                    .loudSeapkerInfo(LoudSpeakerInfo.builder()
                            .startTime(chatContents.getChatLoudSpeakerContent().getStartTime())
                            .build()
                    )
                    .build();
        }

        throw new InvalidChatTypeException("유효한 chat type이 아닙니다.");
    }



}
