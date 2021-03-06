package org.slams.server.court.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slams.server.common.api.TokenGetId;
import org.slams.server.court.dto.request.CourtInsertRequestDto;
import org.slams.server.court.dto.request.CourtReservationRequestDto;
import org.slams.server.court.dto.request.RequestParamVo;
import org.slams.server.court.dto.response.CourtDetailResponseDto;
import org.slams.server.court.dto.response.CourtInsertResponseDto;
import org.slams.server.court.dto.response.CourtReservationResponseDto;
import org.slams.server.court.service.CourtService;
import org.slams.server.court.service.NewCourtService;
import org.slams.server.reservation.dto.response.ReservationInsertResponseDto;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slams.server.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/courts")
public class CourtController {

	private final CourtService courtService;
	private final NewCourtService newCourtService;
	private final Jwt jwt;

	@PostMapping("/new")
	public ResponseEntity<CourtInsertResponseDto> insert(@Valid @RequestBody CourtInsertRequestDto courtInsertRequestDto, HttpServletRequest request) {


		TokenGetId token = new TokenGetId(request, jwt);
		Long userId = token.getUserId();


		return new ResponseEntity<CourtInsertResponseDto>(newCourtService.insert(courtInsertRequestDto, userId), HttpStatus.CREATED);

	}


	@GetMapping("/detail/{courtId}/{date}/{time}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<CourtDetailResponseDto> getDetail(@PathVariable Long courtId, @PathVariable String date, @PathVariable String time) {

		// ????????? ????????? header ?????? ????????? ???????????? ???.
		return ResponseEntity.ok().body(courtService.findDetail(courtId, date, time));
	}


	@GetMapping("/{courtId}/reservations/{date}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Map<String, Object>> getReservationCourts(@PathVariable Long courtId, @PathVariable String date, HttpServletRequest request) {

		TokenGetId token = new TokenGetId(request, jwt);
		Long userId = token.getUserId();


		log.info("userId" + userId);


		Map<String, Object> result = new HashMap<>();
		result.put("courtId", courtId);
		result.put("date", date);
		result.put("reservations", courtService.findCourtReservations(courtId, date, userId));


		// ????????? ????????? header ?????? ????????? ???????????? ???.
		return ResponseEntity.ok().body(result);
	}


	// ???????????? ??????, ??????, ??????????????? ????????? ????????????
	//    /api/v1/courts/date=&{date}&time=${time}&start=${latitude}%${longtitude}&end=${latitude}%${longtitude}
	@GetMapping()
	public ResponseEntity<Map<String, Object>> getAllByDateByBoundary(
		@NotNull RequestParamVo requestParamVo, HttpServletRequest request) {

		Map<String, Object> result = new HashMap<>();
		result.put("courts", courtService.findByDateByBoundary(requestParamVo));

		return ResponseEntity.ok(result);
	}


}
