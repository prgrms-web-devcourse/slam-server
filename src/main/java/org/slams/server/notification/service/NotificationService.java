package org.slams.server.notification.service;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.notification.Exception.InvalidCourtStartTimeException;
import org.slams.server.notification.Exception.NotificationNotFoundException;
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
import java.util.Optional;

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

    public NotificationResponse saveForLoudSpeakerNotification(LoudspeakerNotificationRequest request, Long userId){

        /** ?????? ????????? ?????? **/
        /** ???????????? ?????? ????????????
        var reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ReservationNotFoundException("???????????? ?????? ???????????????."));
        **/
        if(LocalDateTime.now().getHour() - request.getStartTime() < 0 && LocalDateTime.now().getHour() - request.getStartTime() <= 1) {
            throw new InvalidCourtStartTimeException("?????? ?????? 1?????? ???????????? ????????? ????????? ???????????? ??? ????????????.");
        }

        Court court = courtRepository
                .findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException("?????? ????????? ???????????? ????????????."));

        LoudSpeakerNotification loudSpeakerNotification = LoudSpeakerNotification.of(
                court,
                request.getStartTime(),
                userId
        );

        return notificationConvertor.toDto(notificationRepository.save(
                NotificationIndex.createLoudSpeakerNoti(userId, loudSpeakerNotificationRepository.save(loudSpeakerNotification))
        ));
    }

    public NotificationResponse saveForFollowNotification(FollowNotificationRequest request, Long userId){
        Optional<FollowNotification> followNotificationForChecked = followNotificationRepository.findOneByReceiverIdAndUserId(request.getReceiverId(), userId);

        User creator = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("???????????? ?????? ???????????? ???????????? ?????? ????????? ?????????."));

        FollowNotification followNotification = FollowNotification.of(
                creator,
                request.getReceiverId(),
                userId
        );

        return notificationConvertor.toDto(notificationRepository.save(
                NotificationIndex.createFollowNoti(request.getReceiverId(), followNotificationRepository.save(followNotification), userId)
        ));
    }

    public List<NotificationResponse> findAllByUserId(Long userId, CursorPageRequest cursorRequest){

        return notificationConvertor.toDtoList(cursorPageForFindAllByUserId(userId, cursorRequest));
    }

    public List<NotificationIndex> cursorPageForFindAllByUserId(Long userId, CursorPageRequest cursorRequest){
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

        // ??? ?????? ??? ???
        if (ids.size()-1 < 0) {
            return null;
        }else{
            // ????????? ????????? ?????? ??????
            if (cursorRequest.getSize() > ids.size()){
                // ????????? ????????? ??? ???
                return null;
            }else {
                // ????????? ???????????? ?????? ???
                return ids.get(ids.size()-1);
            }
        }
    }

    @Transactional
    public void updateIsClickedStatus(UpdateIsClickedStatusRequest request, Long userId){
        /** version2????????? isClicked??? isRead??? ????????? ?????????. ????????? version1????????? ?????? ???????????? ??????. ??? ??? isClicked??? ?????????. **/
        notificationRepository.updateIsClicked(userId, request.isStatus());
        notificationRepository.updateIsRead(userId, request.isStatus());
    }

    @Transactional
    public void deleteFollowNotification(FollowNotificationRequest request, Long userId){
        notificationRepository.deleteByReceiverIdAndUserId(request.getReceiverId(), userId);
        followNotificationRepository.deleteByReceiverIdAndUserId(request.getReceiverId(), userId);
    }

    public NotificationIndex findByReceiverIdCreatorId(Long receiverId, Long creatorId){
        return notificationRepository.findByReceiverIdAndCreatorId(receiverId, creatorId);
    }

}
