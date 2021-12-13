package org.slams.server.court.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtDetailResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService=courtService;
    }


    @PostMapping("/{id}/new")
    public ResponseEntity<CourtInsertResponseDto> insert(@RequestBody CourtInsertRequestDto request, @PathVariable Long id, Principal principal) {




        // 여기에 추가로 header 토큰 정보가 들어가야 함.
        // id로 추가하게 해야 함.

//        return ResponseEntity.ok(courtService.insert(request, id));
        return new ResponseEntity<CourtInsertResponseDto>(courtService.insert(request, id), HttpStatus.CREATED);
    }


    // 전체 코트 조회
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String,Object>> getAll() {

        // 여기에 추가로 header 토큰 정보가 들어가야 함.

        Map<String,Object>result=new HashMap<>();
        result.put("courts",courtService.findAll());

        return ResponseEntity.ok().body(result);
    }


    @GetMapping("/detail/{courtId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CourtDetailResponseDto> getDetail(@PathVariable Long courtId) {

        // 여기에 추가로 header 토큰 정보가 들어가야 함.
        return ResponseEntity.ok().body(courtService.findDetail(courtId));
    }





}
