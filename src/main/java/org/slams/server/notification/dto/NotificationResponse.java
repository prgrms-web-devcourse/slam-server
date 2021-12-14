package org.slams.server.notification.dto;

import lombok.Builder;
import lombok.Getter;
import org.slams.server.notification.entity.NotificationType;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by yunyun on 2021/12/08.
 */

@Getter
public class NotificationResponse {
    private final NotificationType type;
    private final FollowerInfo followerInfo;
    private final LoudspeakerInfo loudspeakerInfo;
    private boolean isRead;
    private boolean isClicked;

    public NotificationResponse(
            NotificationType type,
            FollowerInfo followerInfo,
            LoudspeakerInfo loudspeakerInfo,
            boolean isRead,
            boolean isClicked){
        this.type = type;
        this.followerInfo = followerInfo;
        this.loudspeakerInfo = loudspeakerInfo;
        this.isRead = isRead;
        this.isClicked = isClicked;
    }

}
