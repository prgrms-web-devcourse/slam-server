package org.slams.server.court.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.error.exception.ErrorCode;
import org.slams.server.common.utils.AwsS3Uploader;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.request.RequestParamVo;
import org.slams.server.court.dto.request.TimeEnum;
import org.slams.server.court.dto.response.*;
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
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    public List<CourtReservationResponseDto> findCourtReservations(Long courtId, String date, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.slams.server.user.exception.UserNotFoundException(
                        MessageFormat.format("가입한 사용자를 찾을 수 없습니다. id : {0}", userId)));


        // court 검색
        Court court=courtRepository.findById(courtId)
                .orElseThrow(() -> new CourtNotFoundException(ErrorCode.NOT_EXIST_COURT.getMessage()));

        LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startLocalDateTime=dateTime.atStartOfDay();
        LocalDateTime endLocalDateTime= dateTime.atTime(23,59,59);


        log.info("localDateTIme"+startLocalDateTime.toString());
        log.info("endDateTime"+endLocalDateTime.toString());


        return reservationRepository.findAllByCourtAndDate(courtId,startLocalDateTime,endLocalDateTime).stream()
                .map(CourtReservationResponseDto::new)
                .collect(Collectors.toList());

    }


    @Transactional
    public List<CourtByDateByBoundaryResponseDto> findByDateByBoundary(RequestParamVo requestParamVo) {

        String date=requestParamVo.getDate();
        String time=requestParamVo.getTime().toUpperCase();


        LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startLocalDateTime;
        LocalDateTime endLocalDateTime;

        TimeEnum timeEnum = TimeEnum.valueOf(time);
        switch (timeEnum) {
            case DAWN:
                startLocalDateTime=dateTime.atTime(0,0,0);
                endLocalDateTime=dateTime.atTime(5,59,59);
                break;
            case MORNING:
                startLocalDateTime=dateTime.atTime(6,0,0);
                endLocalDateTime=dateTime.atTime(11,59,59);
                break;
            case AFTERNOON:
                startLocalDateTime=dateTime.atTime(12,0,0);
                endLocalDateTime=dateTime.atTime(17,59,59);
                break;
            case NIGHT:
                startLocalDateTime=dateTime.atTime(18,0,0);
                endLocalDateTime=dateTime.atTime(23,59,59);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + timeEnum);
        }


        List<String> latitude = requestParamVo.getLatitude();


        List<Double> latitudes = changeValue(requestParamVo.getLatitude());
        Collections.sort(latitudes);
        List<Double> longitudes = changeValue(requestParamVo.getLongitude());
        Collections.sort(longitudes);


        log.info("localDateTIme"+startLocalDateTime.toString());
        log.info("endDateTime"+endLocalDateTime.toString());
        log.info("latitudes"+latitudes.toString());
        log.info("longitudes"+longitudes.toString());
        double startLatitude=latitudes.get(0);
        double endLatitude=latitudes.get(1);

        double startLongitude=longitudes.get(0);
        double endLongitude=longitudes.get(1);

        // 위도 경도로 코트 찾고
        List<Court> byBoundary = courtRepository.findByBoundary(startLatitude, endLatitude, startLongitude, endLongitude);


        List<CourtByDateByBoundaryResponseDto> courtByDateByBoundaryResponseDtoList=new ArrayList<>();
        for (Court court:byBoundary) {
            Long courtId=court.getId();
            Long reservations = reservationRepository.findByDate(startLocalDateTime, endLocalDateTime, courtId);
//            Long reservations = reservationRepository.findByDate(courtId);

            log.info("courtId:"+courtId);
            log.info("reservations:"+reservations);

            courtByDateByBoundaryResponseDtoList.add(new CourtByDateByBoundaryResponseDto(court,reservations));
        }

        return courtByDateByBoundaryResponseDtoList;

    }



    public List<Double> changeValue(List<String> value) {
        List<Double> doubleValue=new ArrayList<>();

        for (String val:value) {
            doubleValue.add(Double.valueOf(val));
        }

        return doubleValue;
    }

}
