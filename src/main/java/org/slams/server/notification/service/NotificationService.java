package org.slams.server.notification.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.exception.FollowNotFoundException;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.notification.convertor.NotificationConvertor;
import org.slams.server.notification.dto.request.FollowNotificationRequest;
import org.slams.server.notification.dto.request.LoudspeakerNotificationRequest;
import org.slams.server.notification.dto.request.UpdateIsClickedStatusRequest;
import org.slams.server.notification.dto.response.NotificationResponse;
import org.slams.server.notification.entity.LoudSpeaker;
import org.slams.server.notification.entity.Notification;
import org.slams.server.notification.repository.LoudspeakerRepository;
import org.slams.server.notification.repository.NotificationRepository;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by yunyun on 2021/12/08.
 */

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final LoudspeakerRepository loudSpeakerNotificationRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final NotificationConvertor notificationConvertor;
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;

    public NotificationResponse saveForLoudSpeakerNotification(LoudspeakerNotificationRequest request, Long receiverId, Long sendId){

        /** 예약 도메인 관련 **/
        /** 테스트를 위해 주석처리
        var reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않은 예약입니다."));
        **/

        /** start time 과 end time 보낼지, start time과 time block으로 보낼지 결정해야 함. 전자라면, start time < end time 요효성 검사해야함 **/

        Court court = courtRepository
                .findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("해당 코트가 존재하지 않습니다."));
        User sender = userRepository.findById(sendId)
                .orElseThrow(() -> new UserNotFoundException("공지를 보내는 이는 존재하지 않는 사용지 입니다."));

        LoudSpeaker loudSpeakerNotification = LoudSpeaker.of(
                sender,
                court,
                request.getStartTime(),
                request.getEndTime()
        );

        return notificationConvertor.toDto(notificationRepository.save(
                Notification.createLoudSpeaker(receiverId, loudSpeakerNotificationRepository.save(loudSpeakerNotification))
        ));
    }

    public NotificationResponse saveForFollowNotification(FollowNotificationRequest request, Long userId){

        User creator = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("팔로우한 해당 사용자는 존재하지 않는 사용자 입니다."));

        User receiver = userRepository
                .findById(request.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException("팔로우 당한 사용자는 존재하지 않는 사용자 입니다."));

        /** follow service 에서 follow 저장하는 로직의 리턴 값을 저장된 객체로 요청해보기. 지금 리턴값은 void 임
         * 불필요하게 user 정보를 들고 와서 저장하게 됨.
         * **/


        Follow follow = followRepository
                .findByFollowerAndFollowing(creator, receiver)
                .orElseThrow(() -> new FollowNotFoundException("존재하지 않는 follow 정보 입니다."));

        return notificationConvertor.toDto(notificationRepository.save(
                Notification.createFollow(request.getReceiverId(), follow)
        ));
    }

    public List<NotificationResponse> findAllByUserId(Long userId, CursorPageRequest cursorRequest){

        return notificationConvertor.toDtoList(cursorPageForFindAllByUserId(userId, cursorRequest));
    }

    public List<Notification> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
        PageRequest pageable = PageRequest.of(0, cursorRequest.getSize());
        return cursorRequest.getIsFirst() ?
                notificationRepository.findAllByUserByCreated(userId, pageable) :
                notificationRepository.findAllByUserLessThanAlarmIdByCreated(userId, cursorRequest.getLastId(), pageable);
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
        /** version2에서는 isClicked와 isRead의 의미가 다르다. 그러나 version1에서는 같은 기능으로 진행. 둘 다 isClicked로 의미됨. **/
        notificationRepository.updateIsClicked(userId, request.isStatus());
        notificationRepository.updateIsRead(userId, request.isStatus());
    }

    @Transactional
    public void deleteFollowNotification(FollowNotificationRequest request, Long userId){
        notificationRepository.deleteByReceiverIdAndSendIdOnFollowNotification(request.getReceiverId(), userId);
    }

//    public Notification findByReceiverIdCreatorId(Long receiverId, Long creatorId){
//        return notificationRepository.findByReceiverIdAndCreatorId(receiverId, creatorId);
//    }

}
