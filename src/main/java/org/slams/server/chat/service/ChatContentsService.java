package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.convertor.ChatContentConvertor;
import org.slams.server.chat.dto.request.ChatContentsRequest;
import org.slams.server.chat.dto.response.subDto.ChatContentType;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.entity.*;
import org.slams.server.chat.repository.ChatContentsRepository;
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

    public List<ChatContentsResponse> findChatContentsListByCourtOrderByCreatedAt(Long courtId, CursorPageRequest cursorRequest){
        return chatContentConvertor.toDtoList(
                cursorPageForFindAllByUserId(courtId, cursorRequest)
        );
    }

    @Transactional
    public ChatContents saveChatConversationContent(ChatContentsRequest request, Long userId){
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 농구장이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 작성자는 존재하지 않는 사용자입니다."));

        ChatContents chatContents = ChatContents.createConversationContent(
                ChatContentType.CONVERSATION,
                court,
                user,
                ChatConversationContent.of(
                        request.getContent()
                )
        );
        chatContentsRepository.save(chatContents);
        courtChatroomMappingRepository.updateUpdatedAtByCourtId(request.getCourtId());
        return chatContents;
    }

    @Transactional
    public ChatContents saveChatLoudSpeakerContent(LoudspeakerNotificationRequest request, Long userId){
        Court court = courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 농구장이 존재하지 않습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 작성자는 존재하지 않는 사용자입니다."));

        ChatContents chatContents = ChatContents.createLoudspeakerContent(
                ChatContentType.LOUDSPEAKER,
                court,
                user,
                ChatLoudSpeakerContent.of(
                        request.getStartTime()
                )
        );
        chatContentsRepository.save(chatContents);
        courtChatroomMappingRepository.updateUpdatedAtByCourtId(request.getCourtId());
        return chatContents;
    }

    public List<ChatContents> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                chatContentsRepository.findAllByUserIdByCreated(userId, pageable):
                chatContentsRepository.findAllByUserIdMoreThenLastIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }

    public ChatContentsResponse sendChatContent(ChatContents chatContents){
        return chatContentConvertor.toDto(chatContents);
    }
}
