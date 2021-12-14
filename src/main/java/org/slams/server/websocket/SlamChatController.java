package org.slams.server.websocket;

import org.slams.server.websocket.MessageRequest;
import org.slams.server.websocket.MessageResponse;
import org.slams.server.websocket.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * Created by yunyun on 2021/12/13.
 */

@Controller
public class SlamChatController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SimpMessagingTemplate websoket;

    public SlamChatController(SimpMessagingTemplate websoket){
        this.websoket = websoket;
    }
    //@MessageMapping("/chat")
    @SendTo("/topic/chat")
    public MessageResponse testChat(@Payload MessageRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info(message.toString());
        String token = (String) headerAccessor.getHeader("token");
        /** token parsing **/
        // userId 추출
        Long testUserId = 1L;
        // token 유효성 검사

        return new MessageResponse(testUserId, message.getMessage(), MessageType.LOUDSPEAKER);
    }

    @MessageMapping("/follow")
    @SendTo("/topic/follow")
    public MessageResponse testFollow(@Payload MessageRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info(message.toString());
        String token = (String) headerAccessor.getHeader("token");
        /** token parsing **/
        // userId 추출
        Long testUserId = 1L;
        // token 유효성 검사

        return new MessageResponse(testUserId, message.getMessage(), MessageType.FOLLOW);
    }

    @MessageMapping("/teston")
    //@SendTo("/topic/teston")
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
    //@SendTo("/topic/teston")
    //public void convertAndSendTest(MessageRequest message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
    public void convertAndSendTest(String message, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        logger.info("들어옴");
        logger.info(message.toString());

        String token = (String) headerAccessor.getHeader("token");
        /** token parsing **/

        // userId 추출
        String testUserId = token;
        // token 유효성 검사

        websoket.convertAndSend("/topic/"+testUserId, "success");
    }

}
