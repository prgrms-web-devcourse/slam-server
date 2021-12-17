package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.convertor.ChatroomMappingConvertor;
import org.slams.server.chat.dto.request.ChatRoomRequest;
import org.slams.server.chat.dto.response.ChatroomResponse;
import org.slams.server.chat.entity.CourtChatroomMapping;
import org.slams.server.chat.entity.UserChatroomMapping;
import org.slams.server.chat.repository.CourtChatroomMappingRepository;
import org.slams.server.chat.repository.UserChatroomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
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
    private final CourtChatroomMappingRepository courtChatroomMappingRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    // chat 룸 목록 조회
    // 챗룸 추가
    // 챗룸 삭제

    /** 채팅방 생성 -> 농구장 생성시 함께 생성됨 **/
    public void saveChatRoom(Long courtId){
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new CourtNotFoundException("해당 농구장이 존재하지 않습니다."));

        courtChatroomMappingRepository.save(
                CourtChatroomMapping.of(court)
        );
    }

    /** 채팅방 최초 입장 **/
    public void saveChatRoomForEachUser(Long userId, ChatRoomRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("해당 사용자는 존재하지 않습니다."));
        CourtChatroomMapping courtChatroomMapping = courtChatroomMappingRepository.findByCourtId(request.getCourtId());
        userChatRoomMappingRepository.save(
                UserChatroomMapping.of(user, courtChatroomMapping)
        );
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
