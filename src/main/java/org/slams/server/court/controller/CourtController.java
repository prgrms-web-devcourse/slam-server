package org.slams.server.court.controller;


import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.api.ApiResponse;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.service.CourtService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/courts")
public class CourtController {

    private final CourtService courtService;

    public CourtController(CourtService courtService) {
        this.courtService=courtService;
    }


    // 사용자에 의한 코트 추가
    @PostMapping("/{id}/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> insert(@RequestBody CourtInsertRequestDto request, @PathVariable Long id) {
        courtService.insert(request, id);
        return ApiResponse.ok(null);
    }

    // 전체 코트 조회
//    @GetMapping
//    public ApiResponse<Page<PostDetailResponse>> getAll(Pageable pageable) {
//        return ApiResponse.ok(postService.findAll(pageable));
//    }





}
