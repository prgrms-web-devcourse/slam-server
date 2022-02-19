package org.slams.server.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.slams.server.common.dto.Follow;
import org.slams.server.notification.entity.NotificationType;

import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {

    private final Long id;

    private final NotificationType type;

    private final Follow follow;

    private final Loudspeaker loudspeaker;

    private final Boolean isRead;

    private final Boolean isClicked;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    private NotificationResponse(
            Long id,
            NotificationType type,
            Follow follow,
            Loudspeaker loudspeaker,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        this.id = id;
        this.type = type;
        this.follow = follow;
        this.loudspeaker = loudspeaker;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NotificationResponse createForFollowNotification(
            Long id,
            NotificationType type,
            Follow follow,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        return new NotificationResponse(id, type, follow, null, isRead, isClicked, createdAt, updatedAt);
    }

    public static NotificationResponse createForLoudspeakerNotification(
            Long id,
            NotificationType type,
            Loudspeaker loudspeaker,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        return new NotificationResponse(id, type, null, loudspeaker, isRead, isClicked, createdAt, updatedAt);
    }

}
