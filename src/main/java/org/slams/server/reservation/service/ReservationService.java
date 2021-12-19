package org.slams.server.reservation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.follow.entity.Follow;
import org.slams.server.follow.repository.FollowRepository;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.*;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.exception.ForbiddenException;
import org.slams.server.reservation.exception.ReservationNotFoundException;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.exception.UserNotFoundException;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;
    private final FollowRepository followRepository;



    @Transactional
    public ReservationInsertResponseDto insert(ReservationInsertRequestDto request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_MEMBER.getMessage()));


        Court court=courtRepository.findById(request.getCourtId())
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));


        Reservation reservation = request.insertRequestDtoToEntity(request);
        reservation.addReservation(court,user);

        //Todo 동일한 유저가 같은 코트를 예약할 수 없다.
//        reservationRepository.get

        reservationRepository.save(reservation);
        return new ReservationInsertResponseDto(reservation);


    }


    @Transactional
    public ReservationUpdateResponseDto update(ReservationUpdateRequestDto requestDto, Long reservationId, Long userId) {

        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new ReservationNotFoundException(ErrorCode.NOT_EXIST_RESERVATION.getMessage()));

        // 해당 유저만 수정 가능
        if (!userId.equals(reservation.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.NOT_FORBIDDEN_RESERVATION.getMessage());
        }

        reservation.update(requestDto);
        return new ReservationUpdateResponseDto(reservation);


    }

    @Transactional
    public ReservationDeleteResponseDto delete(Long reservationId, Long userId) {
        Reservation reservation= reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_RESERVATION.getMessage()));

        if (!userId.equals(reservation.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.NOT_FORBIDDEN_RESERVATION.getMessage());
        }

        reservationRepository.delete(reservation);
        return new ReservationDeleteResponseDto(reservation);
    }

    @Transactional
    public List<ReservationUpcomingResponseDto> findUpcoming(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_MEMBER.getMessage()));

        // user -> reservation -> court 조회
        LocalDateTime localDateTime=LocalDateTime.now();

        return reservationRepository.findByUserByNow(userId,localDateTime).stream()
                .map(ReservationUpcomingResponseDto::new)
                .collect(Collectors.toList());
    }


    // getDetailByReservationByUser
    @Transactional
    public List<ReservationResponseDto> getDetailByReservationByUser(Long userId, Long courtId, String startTime, String endTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_MEMBER.getMessage()));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);


        // reservationId -> User 검색 ->
        List<Reservation> byReservation = reservationRepository.findByReservation(courtId, sTime, eTime);

        List<ReservationResponseDto> reservationResponseDtoList=new ArrayList<>();

        for (Reservation rs:byReservation) {
            User joinUser = rs.getUser();
            if (user.getId() != joinUser.getId()) {
                Boolean isFollow = followRepository.existsByFollowerAndFollowing(user, rs.getUser());
                if (isFollow) {
                    Optional<Follow> follow=followRepository.findByFollowerAndFollowing(user,rs.getUser());
                    reservationResponseDtoList.add(new ReservationResponseDto(joinUser, isFollow,follow.get()));
                }
            }
        }

        return reservationResponseDtoList;
    }



}
