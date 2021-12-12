package org.slams.server.notification.dto;

import lombok.Builder;
import lombok.Getter;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.entity.NotificationType;

import java.time.LocalDateTime;

/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
public class NotificationResponse {
    private final Long alarmId;
    private final String message;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final NotificationType notificationType;

    @Builder
    public NotificationResponse(Long alarmId, String message, NotificationType notificationType, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.alarmId = alarmId;
        this.message = message;
        this.notificationType = notificationType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
