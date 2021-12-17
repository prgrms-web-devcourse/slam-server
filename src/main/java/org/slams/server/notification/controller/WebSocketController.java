package org.slams.server.notification.controller;

import org.slams.server.court.service.CourtService;
import org.slams.server.notification.Exception.TokenNotFountException;
import org.slams.server.notification.dto.WebSocketTestDto;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
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
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final NotificationService notificationService;
    private final CourtService courtService;
    private final SimpMessagingTemplate websoket;
    private final ReservationRepository reservationRepository;

    private final Jwt jwt;

    public WebSocketController(
            SimpMessagingTemplate websoket,
            Jwt jwt,
            NotificationService notificationService,
            CourtService courtService,
            ReservationRepository reservationRepository){
        this.websoket = websoket;
        this.jwt = jwt;
        this.notificationService = notificationService;
        this.courtService = courtService;
        this.reservationRepository = reservationRepository;
    }

    @MessageMapping("/object")
    public void objectTest(WebSocketTestDto message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Long testUserId = findTokenFromHeader(headerAccessor);

        // token 유효성 검사
        WebSocketTestDto userRequest = new WebSocketTestDto();
        userRequest.setUserId(message.getUserId());
        websoket.convertAndSend(
                "/topic/"+message.getUserId(),
                userRequest
        );
    }

    @MessageMapping("/follow")
    public void saveFollowNotification(
            FollowNotificationRequest message,
            SimpMessageHeaderAccessor headerAccessor
            ){
        Long userId = findTokenFromHeader(headerAccessor);

        /** follow 데이터 저장 추가해야함**/

        String messageId = notificationService.saveForFollowNotification(message, userId);

        websoket.convertAndSend(
                String.format("/user/%d/notification", message.getReceiverId()),
                notificationService.findOneByIdInFollowNotification(messageId)
                );
    }

    @MessageMapping("/followcancel")
    public void deleteFollowNotification(
            FollowNotificationRequest message,
            SimpMessageHeaderAccessor headerAccessor
    ){
        Long userId = findTokenFromHeader(headerAccessor);

        /** follow 데이터 삭제 추가해야함**/

        notificationService.deleteFollowNotification(message, userId);
    }

    @MessageMapping("/loudspeaker")
    public void saveLoudSpeakerAndThenSending(
            LoudspeakerNotificationRequest request,
            SimpMessageHeaderAccessor headerAccessor
    ){
        Long userId = findTokenFromHeader(headerAccessor);
        List<Long> bookerIds = reservationRepository.findBeakerIdByCourtId(request.getCourtId());

        for (Long bookId : bookerIds){
            String messageId = notificationService.saveForLoudSpeakerNotification(request, bookId);
            websoket.convertAndSend(
                    String.format("/user/%d/notification", bookId),
                    notificationService.findOneByIdLoudspeakerNotification(messageId)
            );
        }
    }

    private Long findTokenFromHeader(SimpMessageHeaderAccessor headerAccessor){
        var nativeHeaders =  (Map) headerAccessor.getMessageHeaders().get("nativeHeaders");
        assert nativeHeaders != null;
        if (nativeHeaders.containsKey("token")) {
            String token = nativeHeaders.get("token").toString().replace("[", "").replace("]","");
            if(token.length()>64){
                return jwt.verify(token).getUserId();
            }else{
                throw new InvalidTokenException("유효한 토큰 형식이 아닙니다.");
            }
        }else{
            throw new TokenNotFountException("헤더에 토큰이 존재하지 않습니다.");
        }


    }
}
