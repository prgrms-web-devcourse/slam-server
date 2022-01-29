package org.slams.server.notification.controller;

import org.slams.server.chat.dto.response.ChatContentsResponse;
import org.slams.server.chat.service.ChatContentsService;
import org.slams.server.common.utils.WebsocketUtil;
import org.slams.server.court.service.CourtService;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.follow.service.FollowService;
import org.slams.server.notification.Exception.TokenNotFountException;
import org.slams.server.notification.dto.WebSocketTestDto;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.service.NotificationService;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/15.
 */

@Controller
public class NotificationWebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NotificationService notificationService;
    private final SimpMessagingTemplate websocket;
    private final ReservationRepository reservationRepository;
    private final FollowService followService;
    private final WebsocketUtil websocketUtil;
    private final ChatContentsService chatContentsService;

    public NotificationWebSocketController(
            SimpMessagingTemplate websocket,
            NotificationService notificationService,
            ReservationRepository reservationRepository,
            FollowService followService,
            WebsocketUtil websocketUtil,
            ChatContentsService chatContentsService){
        this.websocket = websocket;
        this.notificationService = notificationService;
        this.reservationRepository = reservationRepository;
        this.followService = followService;
        this.websocketUtil = websocketUtil;
        this.chatContentsService = chatContentsService;
    }

    @MessageMapping("/object")
    public void objectTest(WebSocketTestDto message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Long testUserId = websocketUtil.findTokenFromHeader(headerAccessor);

        // token 유효성 검사
        WebSocketTestDto userRequest = new WebSocketTestDto();
        userRequest.setUserId(message.getUserId());
        websocket.convertAndSend(
                "/topic/"+message.getUserId(),
                userRequest
        );
    }

    @MessageMapping("/follow")
    public void saveFollowNotification(
            FollowNotificationRequest message,
            SimpMessageHeaderAccessor headerAccessor
            ){
        Long userId = websocketUtil.findTokenFromHeader(headerAccessor);

        if(userId.equals(message.getReceiverId())){
            throw new RuntimeException("자기 자신을 팔로우 할 수 없습니다.");
        }

        followService.follow(userId, message.getReceiverId());
        NotificationResponse notification = notificationService.saveForFollowNotification(message, userId);

        websocket.convertAndSend(
                String.format("/user/%d/notification", message.getReceiverId()),
                notification
                );
    }

    @MessageMapping("/followcancel")
    public void deleteFollowNotification(
            FollowNotificationRequest message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        Long userId = websocketUtil.findTokenFromHeader(headerAccessor);

        followService.unfollow(userId, message.getReceiverId());
        notificationService.deleteFollowNotification(message, userId);
    }

    @MessageMapping("/loudspeaker")
    public void saveLoudSpeakerAndThenSending(
            LoudspeakerNotificationRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ){
        Long sendId = websocketUtil.findTokenFromHeader(headerAccessor);
        List<Long> receiverIds = reservationRepository.findBeakerIdByCourtId(request.getCourtId());

        for (Long receiverId : receiverIds){
            if (receiverId.equals(sendId)){
                continue;
            }
            NotificationResponse notification = notificationService.saveForLoudSpeakerNotification(request, receiverId, sendId);
            websocket.convertAndSend(
                    String.format("/user/%d/notification", receiverId),
                    notification
            );
        }

        ChatContentsResponse chatContentsResponse = chatContentsService.saveChatLoudSpeakerContent(request, sendId);
        websocket.convertAndSend(
                String.format("/user/%d/chat", request.getCourtId()),
                chatContentsResponse
        );
    }


}
