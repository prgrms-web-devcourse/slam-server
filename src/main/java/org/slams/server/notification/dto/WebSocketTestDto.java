package org.slams.server.notification.dto;

/**
 * Created by yunyun on 2021/12/15.
 */


public class WebSocketTestDto {
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebSocketTestDto{");
        sb.append("userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
