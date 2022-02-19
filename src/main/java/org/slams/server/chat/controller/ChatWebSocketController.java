package org.slams.server.chat.controller;

import org.slams.server.chat.dto.request.CreateChatContentsRequest;
import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.service.ChatContentsService;
import org.slams.server.common.utils.WebsocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by yunyun on 2021/12/18.
 */

@Controller
public class ChatWebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WebsocketUtil websocketUtil;
    private final ChatContentsService chatContentsService;
    private final SimpMessagingTemplate websocket;

    public ChatWebSocketController(
            WebsocketUtil websocketUtil,
            ChatContentsService chatContentsService,
            SimpMessagingTemplate websocket){

        this.websocketUtil = websocketUtil;
        this.chatContentsService = chatContentsService;
        this.websocket = websocket;
    }

    @MessageMapping("/chat")
    public void chat(CreateChatContentsRequest request, SimpMessageHeaderAccessor headerAccessor){
        Long userId = websocketUtil.findTokenFromHeader(headerAccessor);

        ChatContentsResponse chatContentsResponse = chatContentsService.saveChatConversationContent(request, userId);

        websocket.convertAndSend(
                String.format("/user/%d/chat", request.getCourtId()),
                chatContentsResponse
        );
    }

}
