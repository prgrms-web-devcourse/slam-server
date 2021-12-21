package org.slams.server.reservation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.follow.dto.FollowerResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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

//        return reservationRepository.findByUserByNow(userId,localDateTime).stream()
//                .map(ReservationUpcomingResponseDto::new)
//                .collect(Collectors.toList());


        // 코드 수정 (count 잘못 세어지는거 코드 수정)
        List<Reservation> reservationList = reservationRepository.findByUserByNow(userId, localDateTime);
        List<ReservationUpcomingResponseDto> reservationUpcomingResponseDtoList=new ArrayList<>();

        for (Reservation reservation :reservationList) {

            // todo. 여기서 DB 한번 더 뒤지면서 카운트를 센다.
            Long reservationSize = reservationRepository.findByDate(reservation.getStartTime(), reservation.getEndTime(), reservation.getCourt().getId());

            ReservationUpcomingResponseDto reservationUpcomingResponseDto=new ReservationUpcomingResponseDto(reservation,reservationSize);
            reservationUpcomingResponseDtoList.add(reservationUpcomingResponseDto);
        }

        return reservationUpcomingResponseDtoList;

    }


    // getDetailByReservationByUser
    @Transactional
    public List<ReservationResponseDto> getDetailByReservationByUser(Long userId, Long courtId, String startTime, String endTime) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_MEMBER.getMessage()));


        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime sTime = LocalDateTime.parse(startTime, formatter);
        LocalDateTime eTime = LocalDateTime.parse(endTime, formatter);


        // reservationId -> User 검색 ->
        List<Reservation> byReservation = reservationRepository.findByReservation(courtId, sTime, eTime);
        log.info("reservationCount:"+byReservation.size());

        List<ReservationResponseDto> reservationResponseDtoList=new ArrayList<>();
        Boolean isFollow=false;

        for (Reservation rs:byReservation) {
            User joinUser = rs.getUser();

            if (user.getId() != joinUser.getId()) {
                isFollow = followRepository.existsByFollowerAndFollowing(user, rs.getUser());
                if (isFollow) {
                    Optional<Follow> follow=followRepository.findByFollowerAndFollowing(user,rs.getUser());
                    reservationResponseDtoList.add(new ReservationResponseDto(joinUser, isFollow,follow.get()));
                }
                else {
                    reservationResponseDtoList.add(new ReservationResponseDto(joinUser, isFollow));
                }

            }
        }

        return reservationResponseDtoList;
    }



    @Transactional
    public CursorPageResponse<List<ReservationExpiredResponseDto>> findExpired(Long userId, CursorPageRequest cursorPageRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));

        LocalDateTime localDateTime=LocalDateTime.now();

//        reservationRepository.findByUserByExpiredOrderByDesc(userId,localDateTime);

        PageRequest pageable = PageRequest.of(0, cursorPageRequest.getSize());
        List<Reservation> reservations = cursorPageRequest.getIsFirst() ?
                reservationRepository.findByUserByExpiredOrderByDesc(userId, localDateTime,pageable) :
                reservationRepository.findByUserByAndIdLessThanExpiredOrderByDesc(cursorPageRequest.getLastId(), pageable);

        List<ReservationExpiredResponseDto> reservationExpiredResponseDtoList = new ArrayList<>();

//        int reservationSize= reservationExpiredResponseDtoList.size();

        for (Reservation reservation : reservations) {

            // todo. 여기서 한번 더 뒤지면서 reservationSize를 센다.
            Long reservationSize = reservationRepository.findByDate(reservation.getStartTime(), reservation.getEndTime(), reservation.getCourt().getId());
            reservationExpiredResponseDtoList.add(
                    ReservationExpiredResponseDto.toResponse(
                            reservation, reservation.getCourt(), reservation.getCreatedAt(), reservation.getUpdateAt(),reservationSize)
            );
        }

        Long lastId = reservations.size() < cursorPageRequest.getSize() ? null : reservations.get(reservations.size() - 1).getId();

        return new CursorPageResponse<>(reservationExpiredResponseDtoList, lastId);
    }



}
