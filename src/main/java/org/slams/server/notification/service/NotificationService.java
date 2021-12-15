package org.slams.server.notification.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.convertor.NotificationConvertor;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.entity.NotificationType;
import org.slams.server.notification.repository.FollowNotificationRepository;
import org.slams.server.notification.repository.LoudSpeakerNotificationRepository;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yunyun on 2021/12/08.
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FollowNotificationRepository followNotificationRepository;
    private final LoudSpeakerNotificationRepository loudSpeakerNotificationRepository;
    private final UserRepository userRepository;
    private final NotificationConvertor notificationConvertor;
    private final CourtRepository courtRepository;

    @Transactional
    public void saveForLoudSpeakerNotification(LoudspeakerNotificationRequest request, Long userId){
        Court court = courtRepository
                .findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 코트가 존재하지 않습니다."));

        Notification notification = notificationRepository.save(
                Notification.of(
                        NotificationType.LOUDSPEAKER,
                        userId
                )
        );

        loudSpeakerNotificationRepository.save(
                LoudSpeakerNotification.of(court, request.getStartTime(), notification)
        );

    }

    @Transactional
    public void saveForFollowNotification(FollowNotificationRequest request, Long userId){
        User receiver = userRepository
                .findById(request.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("팔로우한 해당 사용자는 존재하지 않는 사용자 입니다."));

        Notification notification = notificationRepository.save(
                Notification.of(
                        NotificationType.FOLLOWING_ALARM,
                        userId
                )
        );

        followNotificationRepository.save(
                FollowNotification.of(receiver, notification)
        );

    }

//    public List<NotificationResponse> findAllByUserId(Long userId, CursorPageRequest cursorRequest){
//        List<Long> messageIds = cursorPageForFindAllByUserId(userId, cursorRequest);
//
//
//        List<FollowNotification> followNotificationList = followNotificationRepository.findAllByNotificationIds(messageIds);
//        List<LoudSpeakerNotification> loudSpeakerNotificationList = loudSpeakerNotificationRepository.findAllByNotificationIds(messageIds);
//
//    }

    public List<Long> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                notificationRepository.findAllByUserByCreated(userId, pageable) :
                notificationRepository.findAllByUserMoreThenAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }

//    public void saveForBaseNotification()

//    public void saveForLoudSpeaker(NotificationRequest alarmDto){
//        alarmRepository.save(
//                Notification.createNotificationForLoudSpeaker(
//                        alarmDto.getCourtId(),
//                        alarmDto.getUserId(),
//                        alarmDto.getStartTime(),
//                        alarmDto.getCourtName()
//                )
//        );
//    }

//    public void saveForFollowing(Long userId, Long followingUserId){
//        User userFollowed = userRepository.findById(followingUserId).orElseThrow(()-> new RuntimeException("데이터 없음"));
//        alarmRepository.save(
//                Notification.createNotificationForFollowing(
//                        null,
//                        userId,
//                        userFollowed.getNickname()
//                )
//        );
//    }
//
//    public List<NotificationResponse> findAllByUserId(Long userId, CursorRequest cursorRequest){
//        List<Notification> alarmList = cursorPageForFindAllByUserId(userId, cursorRequest);
//        return notificationConvertor.toDtoList(alarmList);
//    }
//
//    public List<Notification> cursorPageForFindAllByUserId(Long userId, CursorRequest cursorRequest){
//        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
//        return cursorRequest.isFirst() ?
//                alarmRepository.findAllByUserByCreated(userId, pageable) :
//                alarmRepository.findAllByUserMoreThenAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);
//    }
}
