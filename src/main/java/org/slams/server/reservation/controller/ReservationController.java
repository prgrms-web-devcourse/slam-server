package org.slams.server.reservation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.request.ReservationUpdateRequestDto;
import org.slams.server.reservation.dto.response.ReservationDeleteResponseDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.dto.response.ReservationUpdateResponseDto;
import org.slams.server.reservation.service.ReservationService;
import org.slams.server.user.oauth.jwt.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final Jwt jwt;


    // 경기장 예약하기
    @PostMapping()
    public ResponseEntity<ReservationInsertResponseDto> insert(@Valid @RequestBody ReservationInsertRequestDto requestDto, HttpServletRequest request) {

        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        return new ResponseEntity<ReservationInsertResponseDto>(reservationService.insert(requestDto, userId), HttpStatus.CREATED);
    }


    // 경기장 예약 변경하기
    @PatchMapping("/{reservationId}")
    public ResponseEntity<ReservationUpdateResponseDto> update(@RequestBody ReservationUpdateRequestDto requestDto, @PathVariable Long reservationId, HttpServletRequest request) {

        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        return new ResponseEntity<ReservationUpdateResponseDto>(reservationService.update(requestDto,reservationId,userId), HttpStatus.ACCEPTED);

    }


    // 경기장 예약 취소하기
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ReservationDeleteResponseDto> update(@PathVariable Long reservationId, HttpServletRequest request) {

        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        return new ResponseEntity<ReservationDeleteResponseDto>(reservationService.delete(reservationId,userId), HttpStatus.ACCEPTED);
    }

    // 다가올 예약 목록 조회
    // /api/v1/reservations/upcoming
    @GetMapping("/upcoming")
    public ResponseEntity<Map<String,Object>>getUpcoming(HttpServletRequest request) {
        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();


        Map<String,Object>result=new HashMap<>();
        result.put("reservations",reservationService.findUpcoming(userId));
        return ResponseEntity.ok().body(result);

    }



}
