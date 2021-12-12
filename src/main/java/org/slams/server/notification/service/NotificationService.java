package org.slams.server.notification.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.notification.convertor.NotificationConvertor;
import org.slams.server.notification.dto.NotificationRequest;
import org.slams.server.notification.dto.NotificationResponse;
import org.slams.server.notification.dto.CursorRequest;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository alarmRepository;
    private final UserRepository userRepository;
    private final NotificationConvertor notificationConvertor;

    public void saveForLoudSpeaker(NotificationRequest alarmDto){
        alarmRepository.save(
                Notification.createNotificationForLoudSpeaker(
                        alarmDto.getCourtId(),
                        alarmDto.getUserId(),
                        alarmDto.getStartTime(),
                        alarmDto.getCourtName()
                )
        );
    }

    public void saveForFollowing(Long userId, Long followingUserId){
        User userFollowed = userRepository.findById(followingUserId).orElseThrow(()-> new RuntimeException("데이터 없음"));
        alarmRepository.save(
                Notification.createNotificationForFollowing(
                        null,
                        userId,
                        userFollowed.getNickname()
                )
        );
    }

    public List<NotificationResponse> findAllByUserId(Long userId, CursorRequest cursorRequest){
        List<Notification> alarmList = cursorPageForFindAllByUserId(userId, cursorRequest);
        return notificationConvertor.toDtoList(alarmList);
    }

    public List<Notification> cursorPageForFindAllByUserId(Long userId, CursorRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.isFirst() ?
                alarmRepository.findAllByUserByCreated(userId, pageable) :
                alarmRepository.findAllByUserMoreThenAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }
}
