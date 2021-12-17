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
public class NotificationResponse implements Comparable<NotificationResponse>{

    private final NotificationType type;

    @JsonProperty("followerInfo")
    private final FollowerInfo followerInfo;

    @JsonProperty("loudspeakerInfo")
    private final LoudspeakerInfo loudspeakerInfo;

    @JsonProperty("isRead")
    private final boolean isRead;

    @JsonProperty("isClicked")
    private final boolean isClicked;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    private NotificationResponse(
            NotificationType type,
            FollowerInfo followerInfo,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        this.type = type;
        this.followerInfo = followerInfo;
        this.loudspeakerInfo = loudspeakerInfo;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static NotificationResponse createForFollowNotification(
            NotificationType type,
            FollowerInfo followerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime created,
            LocalDateTime updated
    ){
        return new NotificationResponse(type, followerInfo, null, isRead, isClicked, created, updated);
    }

    public static NotificationResponse createForLoudspeakerNotification(
            NotificationType type,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime created,
            LocalDateTime updated
    ){
        return new NotificationResponse(type, null, loudspeakerInfo, isRead, isClicked, created, updated);
    }


    @Override
    public int compareTo(NotificationResponse target) {
        if(getCreatedAt() == null || target.getCreatedAt()==null){
            return 0;
        }
        return getCreatedAt().compareTo(target.getCreatedAt());
    }
}
