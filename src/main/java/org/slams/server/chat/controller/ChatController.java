package org.slams.server.chat.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.dto.response.ChatroomResponse;
import org.slams.server.chat.dto.response.DeleteUserChatRoomResponse;
import org.slams.server.chat.service.ChatContentsService;
import org.slams.server.chat.service.ChatroomMappingService;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yunyun on 2021/12/18.
 */

@RestController
@RequestMapping(value = "/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatroomMappingService chatroomMappingService;
    private final ChatContentsService chatContentsService;
    private final Jwt jwt;


    @GetMapping("/list")
    public ResponseEntity<CursorPageResponse<List<ChatroomResponse>>> findUserChatRoomByUserId(
            CursorPageRequest cursorRequest,
            HttpServletRequest request
    ){
        Long userId = new TokenGetId(request,jwt).getUserId();

        return ResponseEntity.ok(new CursorPageResponse<>(
                chatroomMappingService.findUserChatRoomByUserId(userId, cursorRequest),
                chatroomMappingService.findLastId(userId, cursorRequest)
                )
        );
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<CursorPageResponse<List<ChatContentsResponse>>> findChatContentByChatRoomId(
            @PathVariable Long courtId,
            CursorPageRequest cursorRequest,
            HttpServletRequest request
    ){
        Long userId = new TokenGetId(request,jwt).getUserId();
        return ResponseEntity.ok(new CursorPageResponse<>(
               chatContentsService.findChatContentsListByCourtOrderByCreatedAt(courtId, cursorRequest),
                chatContentsService.findLastId(courtId, cursorRequest)
                )
        );
    }

    @PostMapping("/court/{courtId}")
    public ResponseEntity<ChatroomResponse> createUserChatRoom(
            @PathVariable Long courtId,
            HttpServletRequest request
    ){
        Long userId = new TokenGetId(request,jwt).getUserId();
        //
        ChatroomResponse chatroomResponse = chatroomMappingService.saveUserChatRoom(userId, courtId);

        return ResponseEntity.status(HttpStatus.CREATED).body(chatroomResponse);
    }

    @DeleteMapping("/court/{courtId}")
    public ResponseEntity<DeleteUserChatRoomResponse> deleteUserChatRoom(
            @PathVariable Long courtId,
            HttpServletRequest request
            ){
        Long userId = new TokenGetId(request,jwt).getUserId();
        System.out.println(userId);
        System.out.println(courtId);
        chatroomMappingService.deleteUserChatRoomByCourtId(courtId, userId);

        return ResponseEntity.ok(DeleteUserChatRoomResponse.builder()
                .courtId(courtId)
                .build()
        );
    }


}
