package org.slams.server.chat.convertor;

import org.slams.server.chat.dto.response.ChatroomResponse;
import org.slams.server.chat.entity.UserChatroomMapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/16.
 */

@Component
public class ChatroomMappingConvertor {

    public List<ChatroomResponse> toDtoList(List<UserChatroomMapping> userChatroomMappingList){
        return userChatroomMappingList.stream()
                .map(v -> toDto(v))
                .collect(Collectors.toList());
    }

    public ChatroomResponse toDto(UserChatroomMapping userChatroomMapping){
        return ChatroomResponse.builder()
                .chatRoomId(userChatroomMapping.getCourtChatroomMapping().getId())
                .courtName(userChatroomMapping.getCourtChatroomMapping().getCourt().getName())
                .userChatRoomId(userChatroomMapping.getId())
                .createdAt(userChatroomMapping.getCreatedAt())
                .updatedAt(userChatroomMapping.getCourtChatroomMapping().getUpdateAt())
                .build();
    }
}
