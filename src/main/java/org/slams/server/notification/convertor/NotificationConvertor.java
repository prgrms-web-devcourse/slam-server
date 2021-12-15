package org.slams.server.notification.convertor;

import org.slams.server.notification.dto.response.CourtInfo;
import org.slams.server.notification.dto.response.FollowerInfo;
import org.slams.server.notification.dto.response.LoudspeakerInfo;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.slams.server.notification.entity.Notification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/12.
 */

@Component
public class NotificationConvertor {

    public List<NotificationResponse> toDtoListForFollowNotification(List<FollowNotification> followNotificationList){
        /** null 유효성 검사 추가 **/
        // followNotificationList

        return followNotificationList.stream()
                .map(v -> toDtoForFollowNotification(v))
                .collect(Collectors.toList());

    }

    public List<NotificationResponse> toDtoListForLoudspeakerNotification(List<LoudSpeakerNotification> loudSpeakerNotificationList){
        /** null 유효성 검사 추가 **/
        // followNotificationList

        return loudSpeakerNotificationList.stream()
                .map(v -> toDtoForLoudNotification(v))
                .collect(Collectors.toList());

    }

    public NotificationResponse toDtoForFollowNotification(FollowNotification followNotification){
        /** null 유효성 검사 추가 **/
        //followNotification.getNotification();
        //followNotification.getFollower();

        return NotificationResponse.createForFollowNotification(
                followNotification.getNotification().getNotificationType(),
                FollowerInfo.builder()
                        .userId(followNotification.getFollower().getId())
                        .userImage(followNotification.getFollower().getProfileImage())
                        .userNickname(followNotification.getFollower().getNickname())
                        .build(),
                followNotification.getNotification().isRead(),
                followNotification.getNotification().isClicked(),
                followNotification.getNotification().getCreatedAt(),
                followNotification.getNotification().getUpdateAt()
        );
    }

    public NotificationResponse toDtoForLoudNotification(LoudSpeakerNotification loudSpeakerNotification){

        return NotificationResponse.createForLoudspeakerNotification(
                loudSpeakerNotification.getNotification().getNotificationType(),
                LoudspeakerInfo.builder()
                        .courtInfo(CourtInfo.builder()
                                .id(loudSpeakerNotification.getCourt().getId())
                                .basketCount(loudSpeakerNotification.getCourt().getBasketCount())
                                .image(loudSpeakerNotification.getCourt().getImage())
                                .latitude(loudSpeakerNotification.getCourt().getLatitude())
                                .longitude(loudSpeakerNotification.getCourt().getLongitude())
                                .name(loudSpeakerNotification.getCourt().getName())
                                .texture(loudSpeakerNotification.getCourt().getTexture())
                                .build()
                        )
                        .startTime(loudSpeakerNotification.getStartTime())
                        .build(),
                loudSpeakerNotification.getNotification().isRead(),
                loudSpeakerNotification.getNotification().isClicked(),
                loudSpeakerNotification.getNotification().getCreatedAt(),
                loudSpeakerNotification.getNotification().getUpdateAt()
        );
    }

    /** created로 정렬 **/
    public List<NotificationResponse> mergeListForFollowNotificationAndLoudspeakerNotification(
            List<NotificationResponse> followNotificationList,
            List<NotificationResponse> loudSpeakerNotificationList
    ){
        if(followNotificationList.addAll(loudSpeakerNotificationList)){
            Collections.sort(followNotificationList);
            return followNotificationList;
        }
        return Collections.emptyList();
    }


}
