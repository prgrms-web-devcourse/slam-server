package org.slams.server.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.slams.server.notification.entity.NotificationType;

import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
public class NotificationResponse {

    private final Long id;

    private final NotificationType type;

    @JsonProperty("followerInfo")
    private final FollowerInfo followerInfo;

    @JsonProperty("loudspeakerInfo")
    private final LoudspeakerInfo loudspeakerInfo;

    private final Boolean isRead;

    private final Boolean isClicked;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    private NotificationResponse(
            Long id,
            NotificationType type,
            FollowerInfo followerInfo,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        this.id = id;
        this.type = type;
        this.followerInfo = followerInfo;
        this.loudspeakerInfo = loudspeakerInfo;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NotificationResponse createForFollowNotification(
            Long id,
            NotificationType type,
            FollowerInfo followerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        return new NotificationResponse(id, type, followerInfo, null, isRead, isClicked, createdAt, updatedAt);
    }

    public static NotificationResponse createForLoudspeakerNotification(
            Long id,
            NotificationType type,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        return new NotificationResponse(id, type, null, loudspeakerInfo, isRead, isClicked, createdAt, updatedAt);
    }

}
