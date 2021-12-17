package org.slams.server.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import org.slams.server.notification.entity.NotificationType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
@JsonNaming(value = PropertyNamingStrategies.KebabCaseStrategy.class)
public class NotificationResponse implements Comparable<NotificationResponse>{
    private final NotificationType type;
    private final FollowerInfo followerInfo;
    private final LoudspeakerInfo loudspeakerInfo;

    @JsonProperty("isRead")
    private final boolean isRead;

    @JsonProperty("isClicked")
    private final boolean isClicked;

    private final LocalDateTime created;
    private final LocalDateTime updated;


    private NotificationResponse(
            NotificationType type,
            FollowerInfo followerInfo,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked,
            LocalDateTime created,
            LocalDateTime updated
    ){
        this.type = type;
        this.followerInfo = followerInfo;
        this.loudspeakerInfo = loudspeakerInfo;
        this.isRead = isRead;
        this.isClicked = isClicked;
        this.created = created;
        this.updated = updated;
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
        if(getCreated() == null || target.getCreated()==null){
            return 0;
        }
        return getCreated().compareTo(target.getCreated());
    }
}
