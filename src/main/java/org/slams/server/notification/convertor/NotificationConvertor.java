package org.slams.server.notification.convertor;

import org.slams.server.notification.Exception.InvalidNotificationTypeException;
import org.slams.server.notification.dto.response.CourtInfo;
import org.slams.server.notification.dto.response.FollowerInfo;
import org.slams.server.notification.dto.response.LoudspeakerInfo;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.entity.NotificationType;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/12.
 */

@Component
public class NotificationConvertor {

    public List<NotificationResponse> toDtoList(List<Notification> notifications){
        /** null 유효성 검사 추가 **/
        // followNotificationList

        return notifications.stream()
                .map(v -> toDto(v))
                .collect(Collectors.toList());
    }

    public NotificationResponse toDto(Notification notification){
        if (notification.getNotificationType().equals(NotificationType.LOUDSPEAKER)){
            return NotificationResponse.createForLoudspeakerNotification(
                    notification.getId(),
                    NotificationType.LOUDSPEAKER,
                    LoudspeakerInfo.builder()
                            .courtInfo(CourtInfo.builder()
                                    .id(notification.getLoudSpeakerNotification().getCourt().getId())
                                    .name(notification.getLoudSpeakerNotification().getCourt().getName())
                                    .texture(notification.getLoudSpeakerNotification().getCourt().getTexture())
                                    .longitude(notification.getLoudSpeakerNotification().getCourt().getLongitude())
                                    .latitude(notification.getLoudSpeakerNotification().getCourt().getLatitude())
                                    .image(notification.getLoudSpeakerNotification().getCourt().getImage())
                                    .basketCount(notification.getLoudSpeakerNotification().getCourt().getBasketCount())
                                    .build())
                            .startTime(notification.getLoudSpeakerNotification().getStartTime())
                            .build(),
                    notification.isRead(),
                    notification.isClicked(),
                    notification.getCreatedAt(),
                    notification.getUpdateAt()
            );
        }

        if (notification.getNotificationType().equals(NotificationType.FOLLOWING)){
            return NotificationResponse.createForFollowNotification(
                    notification.getId(),
                    NotificationType.FOLLOWING,
                    FollowerInfo.builder()
                            .userId(notification.getFollowNotification().getCreator().getId())
                            .userImage(notification.getFollowNotification().getCreator().getProfileImage())
                            .userNickname(notification.getFollowNotification().getCreator().getNickname())
                            .build(),
                    notification.isRead(),
                    notification.isClicked(),
                    notification.getCreatedAt(),
                    notification.getUpdateAt()
            );
        }

        throw new InvalidNotificationTypeException("존재하지 않는 공지 타입입니다.");
    }
//
//    public List<NotificationResponse> toDtoListForLoudspeakerNotification(List<LoudSpeakerNotification> loudSpeakerNotificationList){
//        /** null 유효성 검사 추가 **/
//        // followNotificationList
//
//        return loudSpeakerNotificationList.stream()
//                .map(v -> toDtoForLoudNotification(v))
//                .collect(Collectors.toList());
//    }
//
//    public NotificationResponse toDtoForFollowNotification(FollowNotification followNotification){
//        /** null 유효성 검사 추가 **/
//        //followNotification.getNotification();
//        //followNotification.getFollower();
//
//        return NotificationResponse.createForFollowNotification(
//                followNotification.getNotificationType(),
//                FollowerInfo.builder()
//                        .userId(followNotification.getCreator().getId())
//                        .userImage(followNotification.getCreator().getProfileImage())
//                        .userNickname(followNotification.getCreator().getNickname())
//                        .build(),
//                followNotification.isRead(),
//                followNotification.isClicked(),
//                followNotification.getCreatedAt(),
//                followNotification.getUpdateAt()
//        );
//    }
//
//    public NotificationResponse toDtoForLoudNotification(LoudSpeakerNotification loudSpeakerNotification){
//
//        return NotificationResponse.createForLoudspeakerNotification(
//                loudSpeakerNotification.getNotificationType(),
//                LoudspeakerInfo.builder()
//                        .courtInfo(CourtInfo.builder()
//                                .id(loudSpeakerNotification.getCourt().getId())
//                                .basketCount(loudSpeakerNotification.getCourt().getBasketCount())
//                                .image(loudSpeakerNotification.getCourt().getImage())
//                                .latitude(loudSpeakerNotification.getCourt().getLatitude())
//                                .longitude(loudSpeakerNotification.getCourt().getLongitude())
//                                .name(loudSpeakerNotification.getCourt().getName())
//                                .texture(loudSpeakerNotification.getCourt().getTexture())
//                                .build()
//                        )
//                        .startTime(loudSpeakerNotification.getStartTime())
//                        .build(),
//                loudSpeakerNotification.isRead(),
//                loudSpeakerNotification.isClicked(),
//                loudSpeakerNotification.getCreatedAt(),
//                loudSpeakerNotification.getUpdateAt()
//        );
//    }
//
//    /** created로 정렬 **/
//    public List<NotificationResponse> mergeListForFollowNotificationAndLoudspeakerNotification(
//            List<NotificationResponse> followNotificationList,
//            List<NotificationResponse> loudSpeakerNotificationList
//    ){
//        followNotificationList.addAll(loudSpeakerNotificationList);
//        if(followNotificationList.size() > 0){
//            Collections.sort(followNotificationList);
//            return followNotificationList;
//        }
//        return Collections.emptyList();
//    }


}
