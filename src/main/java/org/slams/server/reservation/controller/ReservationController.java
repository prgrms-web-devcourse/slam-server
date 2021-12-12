package org.slams.server.reservation.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.reservation.dto.request.ReservationInsertRequestDto;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.reservation.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/reservations")
public class ReservationController {

    private ReservationService reservationService;


    // 경기장 예약하기
    @PostMapping()
    public ResponseEntity<ReservationInsertResponseDto> insert(@RequestBody ReservationInsertRequestDto request, @PathVariable Long id) {

        // 여기에 추가로 header 토큰 정보가 들어가야 함.
        // id로 추가하게 해야 함.
        // header를 통해 토큰 검증


        return new ResponseEntity<ReservationInsertResponseDto>(reservationService.insert(request, id), HttpStatus.CREATED);
    }


    // 전체 코트 조회
//    @GetMapping("/all")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<Map<String,Object>> getAll() {
//
//        // 여기에 추가로 header 토큰 정보가 들어가야 함.
//
//        Map<String,Object>result=new HashMap<>();
//        result.put("courts",courtService.findAll());
//
//        return ResponseEntity.ok().body(result);
//    }


}
