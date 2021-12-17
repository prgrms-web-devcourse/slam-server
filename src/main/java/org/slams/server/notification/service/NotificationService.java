package org.slams.server.notification.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.Exception.InvalidCourtStartTime;
import org.slams.server.notification.convertor.NotificationConvertor;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.notification.dto.request.UpdateIsClickedStatusRequest;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.FollowNotification;
import org.slams.server.notification.entity.LoudSpeakerNotification;
import org.slams.server.notification.entity.NotificationIndex;
import org.slams.server.notification.entity.NotificationType;
import org.slams.server.notification.repository.FollowNotificationRepository;
import org.slams.server.notification.repository.LoudSpeakerNotificationRepository;
import org.slams.server.notification.repository.NotificationIndexRepository;
import org.slams.server.reservation.exception.ReservationNotFoundException;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by yunyun on 2021/12/08.
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationIndexRepository notificationRepository;
    private final FollowNotificationRepository followNotificationRepository;
    private final LoudSpeakerNotificationRepository loudSpeakerNotificationRepository;
    private final UserRepository userRepository;
    private final NotificationConvertor notificationConvertor;
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void saveForLoudSpeakerNotification(LoudspeakerNotificationRequest request, Long userId){
        var reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않은 예약입니다."));

        if(LocalDateTime.now().getHour() - request.getStartTime() < 0 && LocalDateTime.now().getHour() - request.getStartTime() <= 1) {
            throw new InvalidCourtStartTime("경기 시작 1시간 이전에만 확성기 기능을 사용하실 수 있습니다.");
        }
        Court court = courtRepository
                .findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 코트가 존재하지 않습니다."));

        LoudSpeakerNotification loudSpeakerNotification = LoudSpeakerNotification.of(
                court,
                request.getStartTime(),
                userId,
                NotificationType.LOUDSPEAKER
        );

        notificationRepository.save(
                NotificationIndex.of(loudSpeakerNotification.getId(), userId)
        );

        loudSpeakerNotificationRepository.save(
                loudSpeakerNotification
        );

    }

    @Transactional
    public void saveForFollowNotification(FollowNotificationRequest request, Long userId){
        User receiver = userRepository
                .findById(request.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("팔로우한 해당 사용자는 존재하지 않는 사용자 입니다."));

        FollowNotification followNotification = FollowNotification.of(
                receiver,
                userId,
                NotificationType.FOLLOWING
        );
        NotificationIndex notification = notificationRepository.save(
                NotificationIndex.of(followNotification.getId(), userId)
        );

        followNotificationRepository.save(
                followNotification
        );

    }

    public List<NotificationResponse> findAllByUserId(Long userId, CursorPageRequest cursorRequest){
        List<String> messageIds = cursorPageForFindAllByUserId(userId, cursorRequest);
        List<FollowNotification> followNotificationList = followNotificationRepository.findAllByNotificationIds(messageIds);
        List<LoudSpeakerNotification> loudSpeakerNotificationList = loudSpeakerNotificationRepository.findAllByNotificationIds(messageIds);

        return notificationConvertor.mergeListForFollowNotificationAndLoudspeakerNotification(
                notificationConvertor.toDtoListForFollowNotification(followNotificationList),
                notificationConvertor.toDtoListForLoudspeakerNotification(loudSpeakerNotificationList));
    }

    public List<String> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                notificationRepository.findMessageIdByUserByCreated(userId, pageable) :
                notificationRepository.findMessageIdByUserLessThanAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);
    }

    public Long findNotificationLastId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        List<Long> ids = cursorRequest.getIsFirst() ?
                notificationRepository.findIdByUserByCreated(userId, pageable) :
                notificationRepository.findIdByUserLessThanAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);

        // 빈 배열 일 때
        if (ids.size()-1 < 0) {
            return null;
        }else{
            // 마지막 데이터 인지 확인
            if (cursorRequest.getSize() > ids.size()){
                // 마지막 데이터 일 때
                return null;
            }else {
                // 마지막 데이터가 아닐 때
                return ids.get(ids.size()-1);
            }

        }

    }

    @Transactional
    public void updateIsClickedStatus(UpdateIsClickedStatusRequest request, Long userId){
        followNotificationRepository.updateIsClicked(userId, request.isStatus());
        loudSpeakerNotificationRepository.updateIsClicked(userId, request.isStatus());

        /** version2에서는 isClicked와 isRead의 의미가 다르다. 그러나 version1에서는 같은 기능으로 진행. 둘 다 isClicked로 의미됨. **/
        followNotificationRepository.updateIsRead(userId, request.isStatus());
        loudSpeakerNotificationRepository.updateIsRead(userId, request.isStatus());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getTop10Notification(Long userId){
        PageRequest pageable = PageRequest.of(0, 10);
        List<String> messageIdList = notificationRepository.findMessageIdByUserByCreated(userId, pageable);

        List<FollowNotification> followNotificationList = followNotificationRepository.findAllByNotificationIds(messageIdList);
        List<LoudSpeakerNotification> loudSpeakerNotificationList = loudSpeakerNotificationRepository.findAllByNotificationIds(messageIdList);

        return notificationConvertor.mergeListForFollowNotificationAndLoudspeakerNotification(
            notificationConvertor.toDtoListForFollowNotification(followNotificationList),
            notificationConvertor.toDtoListForLoudspeakerNotification(loudSpeakerNotificationList));
    }

}
