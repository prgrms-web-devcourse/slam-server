package org.slams.server.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunyun on 2021/12/13.
 */
public class ReplyHandler extends TextWebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<WebSocketSession> sessions = new ArrayList<>();
    Map<String, WebSocketSession> userSession = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("session: {session}");
        sessions.add(session);
        userSession.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        logger.info("session: {session}, message: {message}");
        for (WebSocketSession sess : sessions) {
            sess.sendMessage(new TextMessage(String.format("id >> {0}, message >> {1}",session.getId(), message.getPayload())));
        }
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("connection closed: session >> {session}, status >> {status}");
    }
}
