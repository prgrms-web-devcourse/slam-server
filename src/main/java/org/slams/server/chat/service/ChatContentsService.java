package org.slams.server.chat.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.dto.ChatContentsRequest;
import org.slams.server.chat.dto.ChatContentsResponse;
import org.slams.server.chat.entity.ChatContents;
import org.slams.server.chat.repository.ChatContentsRepository;
import org.slams.server.common.api.CursorPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyun on 2021/12/16.
 */

@Service
@RequiredArgsConstructor
public class ChatContentsService {

    private final ChatContentsRepository chatContentsRepository;


    // 최신순으로 chat 대화내용 조회
    // 대화내용 저장
    // websocker으로 대회내용 전송



    public List<ChatContentsResponse> findChatContentsListByCourtOrderByCreatedAt(Long courtId, CursorPageRequest cursorRequest){
        return null;
    }

    public void saveChatContents(ChatContentsRequest request){

    }

    // 전송할 때 쓰는 것 converter 대체

    public List<ChatContents> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                chatContentsRepository.findAllByUserIdByCreated(userId, pageable):
                chatContentsRepository.findAllByUserIdMoreThenLastIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }
}
