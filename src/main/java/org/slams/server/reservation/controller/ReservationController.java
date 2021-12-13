package org.slams.server.reservation.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpdateResponseDto;
import org.slams.server.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    // 경기장 예약하기
    @PostMapping("/{userId}")
    public ResponseEntity<ReservationInsertResponseDto> insert(@RequestBody ReservationInsertRequestDto request, @PathVariable Long userId) {

        // 여기에 추가로 header 토큰 정보가 들어가야 함.
        // id로 추가하게 해야 함.
        // header를 통해 토큰 검증
//        System.err.println("id"+id);


        return new ResponseEntity<ReservationInsertResponseDto>(reservationService.insert(request, userId), HttpStatus.CREATED);
    }


    // 경기장 예약 변경하기
    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationUpdateResponseDto> update(@RequestBody ReservationUpdateRequestDto requestDto, @PathVariable Long reservationId) {

        return new ResponseEntity<ReservationUpdateResponseDto>(reservationService.update(requestDto,reservationId), HttpStatus.CREATED);

    }






}
