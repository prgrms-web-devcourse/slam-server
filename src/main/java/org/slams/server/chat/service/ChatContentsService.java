package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.convertor.ChatContentConvertor;
import org.slams.server.chat.dto.request.CreateChatContentsRequest;
import org.slams.server.chat.dto.response.subDto.ChatContentType;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.entity.*;
import org.slams.server.chat.repository.ChatContentsRepository;
import org.slams.server.chat.repository.ChatConversationContentRepository;
import org.slams.server.chat.repository.ChatLoudSpeakerContentRepository;
import org.slams.server.chat.repository.CourtChatroomMappingRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */

@Service
@RequiredArgsConstructor
public class ChatContentsService {

    private final ChatContentsRepository chatContentsRepository;
    private final ChatContentConvertor chatContentConvertor;
    private final CourtRepository courtRepository;
    private final CourtChatroomMappingRepository courtChatroomMappingRepository;
    private final UserRepository userRepository;
    private final ChatConversationContentRepository chatConversationContentRepository;
    private final ChatLoudSpeakerContentRepository chatLoudSpeakerContentRepository;


    public List<ChatContentsResponse> findChatContentsListByCourtOrderByCreatedAt(Long courtId, CursorPageRequest cursorRequest){
        return chatContentConvertor.toDtoList(
                cursorPageForFindAllByUserId(courtId, cursorRequest)
        );
    }

    @Transactional
    public ChatContentsResponse saveChatConversationContent(CreateChatContentsRequest request, Long userId){
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 농구장이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 작성자는 존재하지 않는 사용자입니다."));

        ChatConversationContent chatConversationContent = chatConversationContentRepository.save(
                ChatConversationContent.of(request.getContent()));

        ChatContents chatContents = ChatContents.createConversationContent(
                ChatContentType.CONVERSATION,
                court,
                user,
                chatConversationContent
        );
        chatContentsRepository.save(chatContents);
        courtChatroomMappingRepository.updateUpdatedAtByCourtId(request.getCourtId());
        return chatContentConvertor.toDto(chatContents);
    }


    @Transactional
    public ChatContentsResponse saveChatLoudSpeakerContent(LoudspeakerNotificationRequest request, Long userId){
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 농구장이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 작성자는 존재하지 않는 사용자입니다."));

        ChatLoudSpeakerContent chatLoudSpeakerContent = chatLoudSpeakerContentRepository.save(
                ChatLoudSpeakerContent.of(request.getStartTime().getHour())
        );
        ChatContents chatContents = ChatContents.createLoudspeakerContent(
                ChatContentType.LOUDSPEAKER,
                court,
                user,
                chatLoudSpeakerContent
        );
        chatContentsRepository.save(chatContents);
        courtChatroomMappingRepository.updateUpdatedAtByCourtId(request.getCourtId());
        return chatContentConvertor.toDto(chatContents);
    }

    public List<ChatContents> cursorPageForFindAllByUserId(Long courtId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                chatContentsRepository.findAllByCourtIdByCreated(courtId, pageable):
                chatContentsRepository.findAllByCourtIdMoreThenLastIdByCreated(courtId, cursorRequest.getLastId(), pageable);
    }

    public ChatContentsResponse sendChatContent(ChatContents chatContents){
        return chatContentConvertor.toDto(chatContents);
    }

    public Long findLastId(Long courtId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        List<Long> ids = cursorRequest.getIsFirst() ?
                chatContentsRepository.findIdByCourtIdByCreated(courtId, pageable):
                chatContentsRepository.findIdByCourtIdMoreThenLastIdByCreated(courtId, cursorRequest.getLastId(), pageable);

        // 빈 배열 일 때
        if (ids.size() -1 < 0) {
            return null;
        }else{
            // 마지막 데이터 인지 확인
            if (cursorRequest.getSize() > ids.size()){
                // 마지막 데이터 일 때
                return null;
            }else {
                // 마지막 데이터가 아닐 때
                return ids.get(ids.size() - 1);
            }

        }

    }
}
