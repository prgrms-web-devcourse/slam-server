package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.dto.ChatRoomResponse;
import org.slams.server.chat.repository.UserChatRoomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */

@Service
@RequiredArgsConstructor
public class ChatRoomMappingService {

    private final UserChatRoomMappingRepository userChatRoomMappingRepository;

    // chat 룸 목록 조회
    // 챗룸 추가
    // 챗룸 삭제

    public void saveChatRoom(){

    }

    public List<ChatRoomResponse> findChatRoomsByCourt(Long userId, CursorPageRequest cursorRequest){
        return null;
    }

    public void deleteEnteredChatRoomByChatRoomId(){

    }
}
