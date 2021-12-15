package org.slams.server.reservation.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.court.entity.Court;
import org.slams.server.court.exception.CourtNotFoundException;
import org.slams.server.court.exception.UserNotFoundException;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.ReservationDeleteResponseDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpdateResponseDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.exception.ForbiddenException;
import org.slams.server.reservation.exception.ReservationNotFoundException;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;



    @Transactional
    public ReservationInsertResponseDto insert(ReservationInsertRequestDto request, Long id) {
        // user검색후 없으면 반환
        // token으로 찾으면 getUser 필요없음
        User user = getUser(id);


        Court court=getCourt(request.getCourtId());
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



//        return new ReservationUpdateResponseDto(reservation);


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
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_MEMBER.getMessage()));
    }

    @Transactional
    public Court getCourt(Long courtId) {
        return courtRepository.findById(courtId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));
    }




//    @Transactional
//    public List<AllCourtResponseDto> findAll() {
//        return courtRepository.findAll().stream()
//                .map(AllCourtResponseDto::new)
//                .collect(Collectors.toList());
//    }

}
