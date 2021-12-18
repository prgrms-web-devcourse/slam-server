package org.slams.server.management.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.court.dto.request.NewCourtRequest;
import org.slams.server.court.dto.response.NewCourtResponse;
import org.slams.server.court.service.NewCourtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/management")
public class ManagementController {

	private final NewCourtService newCourtService;

	@GetMapping("/newCourts")
	public ResponseEntity<CursorPageResponse<List<NewCourtResponse>>> getNewCourtsInStatus(
		@RequestParam String status, CursorPageRequest cursorPageRequest) {
		CursorPageResponse<List<NewCourtResponse>> newCourtResponse
			= newCourtService.getNewCourtsInStatus(status, cursorPageRequest);

		return ResponseEntity.ok(newCourtResponse);
	}

	@PatchMapping("/newCourt/accept")
	public ResponseEntity<NewCourtResponse> accept(@RequestBody NewCourtRequest newCourtRequest) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
			.body(newCourtService.acceptNewCourt(newCourtRequest.getNewCourtId()));
	}

	@PatchMapping("/newCourt/deny")
	public ResponseEntity<NewCourtResponse> deny(@RequestBody NewCourtRequest newCourtRequest) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
			.body(newCourtService.denyNewCourt(newCourtRequest.getNewCourtId()));
	}

}
