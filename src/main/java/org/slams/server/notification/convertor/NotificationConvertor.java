package org.slams.server.notification.convertor;

import org.slams.server.notification.dto.NotificationRequest;
import org.slams.server.notification.dto.NotificationResponse;
import org.slams.server.notification.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yunyun on 2021/12/12.
 */

@Component
public class NotificationConvertor {
    public NotificationResponse toDto(Notification notificationEntity){
        return NotificationResponse.builder()
                .notificationId(notificationEntity.getId())
                .createdAt(notificationEntity.getCreatedAt())
                .updatedAt(notificationEntity.getUpdateAt())
                .message(notificationEntity.getContent())
                .notificationType(notificationEntity.getNotificationType())
                .build();
    }

    public List<NotificationResponse> toDtoList(List<Notification> notificationEntityList){
        List<NotificationResponse> notificationResponses = new ArrayList<>();

        for(Notification notification : notificationEntityList){
            notificationResponses.add(toDto(notification));
        }
        return notificationResponses;
    }


}
