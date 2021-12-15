package org.slams.server.notification.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slams.server.notification.dto.SocketRequest;
import org.slams.server.notification.dto.SocketResponse;
import org.slams.server.notification.dto.UserRequest;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void objectTest(UserRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {

        var nativeHeaders =  (Map) headerAccessor.getMessageHeaders().get("nativeHeaders");
        String token = nativeHeaders.get("token").toString().replace("[", "").replace("]","");
        Long testUserId = jwt.verify(token).getUserId();

        // token 유효성 검사
        UserRequest userRequest = new UserRequest();
        userRequest.setUserId(message.getUserId());
        websoket.convertAndSend(
                "/topic/"+message.getUserId(),
                userRequest
        );
    }
}
