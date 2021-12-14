package org.slams.server.websocket;

/**
 * Created by yunyun on 2021/12/13.
 */


public class MessageRequest {
    private final Long courtId;
    private final String message;
    private final MessageType messageType;

    public MessageRequest(Long courtId, String message, MessageType messageType){
        this.courtId = courtId;
        this.message = message;
        this.messageType = messageType;
    }

    public Long getCourtId() {
        return courtId;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageRequest{");
        sb.append("courtId=").append(courtId);
        sb.append(", message='").append(message).append('\'');
        sb.append(", messageType=").append(messageType);
        sb.append('}');
        return sb.toString();
    }
}
