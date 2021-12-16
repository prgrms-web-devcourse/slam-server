package org.slams.server.court.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.utils.AwsS3Uploader;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.AllCourtResponseDto;
import org.slams.server.court.dto.response.CourtDetailResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.dto.response.CourtReservationResponseDto;
import org.slams.server.court.entity.Court;
import org.slams.server.court.entity.NewCourt;
import org.slams.server.court.repository.CourtRepository;
import org.slams.server.court.repository.NewCourtRepository;
import org.slams.server.favorite.dto.response.FavoriteSelectResponseDto;
import org.slams.server.reservation.entity.Reservation;
import org.slams.server.reservation.repository.ReservationRepository;
import org.slams.server.user.entity.User;
import org.slams.server.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slams.server.court.exception.*;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Service
public class CourtService {

    private final CourtRepository courtRepository;
    private final UserRepository userRepository;
    private final NewCourtRepository newCourtRepository;
    private final AwsS3Uploader awsS3Uploader;
    private final ReservationRepository reservationRepository;


    @Transactional
    public List<AllCourtResponseDto> findAll() {
        return courtRepository.findAll().stream()
                .map(AllCourtResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public CourtDetailResponseDto findDetail(Long courtId) {

        return courtRepository.findById(courtId)
                .map(CourtDetailResponseDto::new)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));
    }


    @Transactional
    public Court getCourt(Long CourtId) {
        return courtRepository.findById(CourtId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));

    }


    @Transactional
    public List<CourtReservationResponseDto> findCourtReservations(Long courtId, String date, Long userId) {

        // User 검색
        User user = getUser(userId);
        // court 검색
        Court court=getCourt(courtId);

        LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startLocalDateTime=dateTime.atStartOfDay();
        LocalDateTime endLocalDateTime= dateTime.atTime(23,59,59);
//        log.info(startLocalDateTime);



        log.info("localDateTIme"+startLocalDateTime.toString());
        log.info("endDateTime"+endLocalDateTime.toString());

//        return reservationRepository.findAllByCourtAndDate(reqLocalDateTime,courtId).stream()
//                .map(CourtReservationResponseDto::new)
//                .collect(Collectors.toList());

        return reservationRepository.findAllByCourtAndDate(courtId,startLocalDateTime,endLocalDateTime).stream()
                .map(CourtReservationResponseDto::new)
                .collect(Collectors.toList());

//                return reservationRepository.findAllByCourtAndDate(reqLocalDateTime,courtId).stream()
//                .map(CourtReservationResponseDto::new)
//                .collect(Collectors.toList());

    }










}
