package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.convertor.ChatroomMappingConvertor;
import org.slams.server.chat.dto.ChatroomResponse;
import org.slams.server.chat.entity.UserChatroomMapping;
import org.slams.server.chat.repository.UserChatroomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */

@Service
@RequiredArgsConstructor
public class ChatroomMappingService {

    private final UserChatroomMappingRepository userChatRoomMappingRepository;
    private final ChatroomMappingConvertor chatroomMappingConvertor;

    // chat 룸 목록 조회
    // 챗룸 추가
    // 챗룸 삭제

    public void saveChatRoom(){

    }

    public List<ChatroomResponse> findChatRoomByCourt(Long userId, CursorPageRequest cursorRequest){
        return chatroomMappingConvertor.toDtoList(
                cursorPageForFindAllByUserId(userId, cursorRequest)
        );
    }

    public void deleteEnteredChatRoomByChatRoomId(){

    }

    public List<UserChatroomMapping> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                userChatRoomMappingRepository.findAllByUserIdByCreated(userId, pageable):
                userChatRoomMappingRepository.findAllByUserIdMoreThenLastIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }
}
