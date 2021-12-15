package org.slams.server.notification.controller;

import org.slams.server.notification.dto.SocketRequest;
import org.slams.server.notification.dto.SocketResponse;
import org.slams.server.notification.dto.UserRequest;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yunyun on 2021/12/15.
 */

@Controller
public class WebSocketController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate websoket;

    private final Jwt jwt;

    public WebSocketController(SimpMessagingTemplate websoket,Jwt jwt){
        this.websoket = websoket;
        this.jwt = jwt;
    }
    //@MessageMapping("/chat")
//    @SendTo("/topic/chat")
//    public MessageResponse testChat(@Payload MessageRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        logger.info(message.toString());
//        String token = (String) headerAccessor.getHeader("token");
//        /** token parsing **/
//        // userId 추출
//        Long testUserId = 1L;
//        // token 유효성 검사
//
//        return new MessageResponse(testUserId, message.getMessage(), MessageType.LOUDSPEAKER);
//    }

//    @MessageMapping("/follow")
//    @SendTo("/topic/follow")
//    public MessageResponse testFollow(@Payload MessageRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
//        logger.info(message.toString());
//        String token = (String) headerAccessor.getHeader("token");
//        /** token parsing **/
//        // userId 추출
//        Long testUserId = 1L;
//        // token 유효성 검사
//
//        return new MessageResponse(testUserId, message.getMessage(), MessageType.FOLLOW);
//    }

    @MessageMapping("/teston")
    public void testNone() throws Exception {
        logger.info("들어옴");
        //logger.info(message.toString());
        /** token parsing **/
        // userId 추출
        Long testUserId = 1L;
        // token 유효성 검사

        websoket.convertAndSend("/topic/teston", "success");
    }

    @MessageMapping("/chat")
    public void convertAndSendTest(UserRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("들어옴");
        logger.info(message.toString());

        String token = (String) headerAccessor.getHeader("token");
        /** token parsing **/

        // userId 추출
        String testUserId = token;
        // token 유효성 검사

        websoket.convertAndSend("/topic/teston", message.getUserId());
    }

    @MessageMapping("/object")
    public void objectTest(UserRequest message, ServletServerHttpRequest request) throws Exception {
        logger.info("들어옴");
        logger.info(message.toString());

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();


        String token = httpServletRequest.getParameter("token");
        //String token = (String) headerAccessor.getHeader("token");
        /** token parsing **/

        logger.info( token);

        // userId 추출
        String[] tokenString = token.split(" ");
        if (!tokenString[0].equals("Bearer")) {
            throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
        }


        Long testUserId = jwt.verify(tokenString[1]).getUserId();
        // token 유효성 검사
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId(message.getUserId());
        websoket.convertAndSend(
                "/topic/"+message.getUserId(),
                userRequest
        );
    }
}
