package org.slams.server.notification.convertor;

import org.slams.server.notification.dto.response.CourtInfo;
import org.slams.server.notification.dto.response.FollowerInfo;
import org.slams.server.notification.dto.response.LoudspeakerInfo;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.springframework.stereotype.Component;

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
                followNotification.getNotificationType(),
                FollowerInfo.builder()
                        .userId(followNotification.getReceiver().getId())
                        .userImage(followNotification.getReceiver().getProfileImage())
                        .userNickname(followNotification.getReceiver().getNickname())
                        .build(),
                followNotification.isRead(),
                followNotification.isClicked(),
                followNotification.getCreatedAt(),
                followNotification.getUpdateAt()
        );
    }

    public NotificationResponse toDtoForLoudNotification(LoudSpeakerNotification loudSpeakerNotification){

        return NotificationResponse.createForLoudspeakerNotification(
                loudSpeakerNotification.getNotificationType(),
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
                loudSpeakerNotification.isRead(),
                loudSpeakerNotification.isClicked(),
                loudSpeakerNotification.getCreatedAt(),
                loudSpeakerNotification.getUpdateAt()
        );
    }

    /** created로 정렬 **/
    public List<NotificationResponse> mergeListForFollowNotificationAndLoudspeakerNotification(
            List<NotificationResponse> followNotificationList,
            List<NotificationResponse> loudSpeakerNotificationList
    ){
        followNotificationList.addAll(loudSpeakerNotificationList);
        if(followNotificationList.size() > 0){
            Collections.sort(followNotificationList);
            return followNotificationList;
        }
        return Collections.emptyList();
    }


}
