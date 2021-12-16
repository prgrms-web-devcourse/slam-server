package org.slams.server.follow.controller;

import lombok.RequiredArgsConstructor;
import org.slams.server.common.api.CursorPageRequest;
import org.slams.server.common.api.CursorPageResponse;
import org.slams.server.follow.dto.FollowerResponse;
import org.slams.server.follow.dto.FollowingResponse;
import org.slams.server.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
@RestController
public class FollowController {

	private final FollowService followService;

	@GetMapping("/{userId}/followers")
	public ResponseEntity<CursorPageResponse<List<FollowerResponse>>> followerPage(
		@PathVariable Long userId, CursorPageRequest cursorPageRequest) {
		CursorPageResponse<List<FollowerResponse>> followerResponse = followService.followerPage(userId, cursorPageRequest);

		return ResponseEntity.ok(followerResponse);
	}

	@GetMapping("/{userId}/followings")
	public ResponseEntity<CursorPageResponse<List<FollowingResponse>>> followingPage(
		@PathVariable Long userId, CursorPageRequest cursorPageRequest) {
		CursorPageResponse<List<FollowingResponse>> followingResponse = followService.followingPage(userId, cursorPageRequest);

		return ResponseEntity.ok(followingResponse);
	}

}
