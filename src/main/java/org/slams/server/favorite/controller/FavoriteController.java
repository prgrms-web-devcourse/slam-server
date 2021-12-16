package org.slams.server.favorite.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.service.CourtService;
import org.slams.server.favorite.dto.request.FavoriteInsertRequestDto;
import org.slams.server.favorite.dto.response.FavoriteDeleteResponseDto;
import org.slams.server.favorite.dto.response.FavoriteInsertResponseDto;
import org.slams.server.favorite.service.FavoriteService;
import org.slams.server.reservation.dto.response.ReservationDeleteResponseDto;
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
@RequestMapping(value = "/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final Jwt jwt;

    @PostMapping()
    public ResponseEntity<FavoriteInsertResponseDto> insert(@RequestBody FavoriteInsertRequestDto favoriteInsertRequestDto , HttpServletRequest request) {

        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        return new ResponseEntity<FavoriteInsertResponseDto>(favoriteService.insert(favoriteInsertRequestDto, userId), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Map<String,Object>> getAll(HttpServletRequest request) {

        // 여기에 추가로 header 토큰 정보가 들어가야 함.
        // 내가 즐겨찾기 한 코트를 찾아야 함.
        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        Map<String,Object>result=new HashMap<>();
        result.put("favorites",favoriteService.getAll(userId));

        return ResponseEntity.ok().body(result);
    }


    @DeleteMapping("{favoriteId}")
    public ResponseEntity<FavoriteDeleteResponseDto> delete(@PathVariable Long favoriteId, HttpServletRequest request) {
        TokenGetId token=new TokenGetId(request,jwt);
        Long userId=token.getUserId();

        return new ResponseEntity<FavoriteDeleteResponseDto>(favoriteService.delete(userId, favoriteId), HttpStatus.ACCEPTED);

    }







}
