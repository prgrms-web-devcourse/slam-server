package org.slams.server.websocket;

/**
 * Created by yunyun on 2021/12/13.
 */


public class MessageResponse {
    private final Long userId;
    private final String message;
    private final MessageType messageType;

    public MessageResponse(Long userId, String message, MessageType messageType){
        this.userId = userId;
        this.message = message;
        this.messageType = messageType;
    }

    public Long getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageResponse{");
        sb.append("userId=").append(userId);
        sb.append(", message='").append(message).append('\'');
        sb.append(", messageType=").append(messageType);
        sb.append('}');
        return sb.toString();
    }
}
