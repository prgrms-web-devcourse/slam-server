package org.slams.server.court.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.api.ApiResponse;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.AllCourtResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.service.CourtService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService=courtService;
    }


    // 사용자에 의한 코트 추가
//    @PostMapping("/{id}/new")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiResponse<?> insert(@RequestBody CourtInsertRequestDto request, @PathVariable Long id) {
//        courtService.insert(request, id);
//        return ApiResponse.ok(null);
//    }

//    // 전체 코트 조회
//    @GetMapping("/all")
//    @ResponseStatus(HttpStatus.OK)
//    public ApiResponse<List<AllCourtResponseDto>> getAll() {
//        return ApiResponse.ok(courtService.findAll());
//    }



    @PostMapping("/{id}/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CourtInsertResponseDto> insert(@RequestBody CourtInsertRequestDto request, @PathVariable Long id) {
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





}
