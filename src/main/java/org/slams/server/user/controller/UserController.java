package org.slams.server.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slams.server.user.dto.request.ExtraUserInfoRequest;
import org.slams.server.user.dto.request.ProfileImageRequest;
import org.slams.server.user.dto.response.ExtraUserInfoResponse;
import org.slams.server.user.dto.response.MyProfileResponse;
import org.slams.server.user.dto.response.ProfileImageResponse;
import org.slams.server.user.exception.InvalidTokenException;
import org.slams.server.user.oauth.jwt.Jwt;
import org.slams.server.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;
	private final Jwt jwt;

	@GetMapping("/me")
	public ResponseEntity<Void> login(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String[] tokenString = authorization.split(" ");
		if (!tokenString[0].equals("Bearer")) {
			throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
		}

		Jwt.Claims claims = jwt.verify(tokenString[1]);

//		String userNickname = userService.findUserNickname(claims.getUserId());
//		log.info(userNickname);

		return ResponseEntity.ok().build();
	}

	@GetMapping(value = "/myprofile", produces = "application/json; charset=utf-8;")
	public ResponseEntity<MyProfileResponse> getMyInfo(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String[] tokenString = authorization.split(" ");
		if (!tokenString[0].equals("Bearer")) {
			throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
		}

		Jwt.Claims claims = jwt.verify(tokenString[1]);

		MyProfileResponse myProfileResponse = userService.getMyInfo(claims.getUserId());

		return ResponseEntity.ok(myProfileResponse);
	}

	@PostMapping("/me")
	public ResponseEntity<ExtraUserInfoResponse> addExtraUserInfo(HttpServletRequest request, @RequestBody ExtraUserInfoRequest extraUserInfoRequest) {
		String authorization = request.getHeader("Authorization");
		String[] tokenString = authorization.split(" ");
		if (!tokenString[0].equals("Bearer")) {
			throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
		}

		Jwt.Claims claims = jwt.verify(tokenString[1]);

		ExtraUserInfoResponse extraUserInfoResponse = userService.addExtraUserInfo(claims.getUserId(), extraUserInfoRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(extraUserInfoResponse);
	}

	@PutMapping("/my-profile/image")
	public ResponseEntity<ProfileImageResponse> updateUserProfileImage(HttpServletRequest request, @RequestBody ProfileImageRequest profileImageRequest) {
		String authorization = request.getHeader("Authorization");
		String[] tokenString = authorization.split(" ");
		if (!tokenString[0].equals("Bearer")) {
			throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
		}

		Jwt.Claims claims = jwt.verify(tokenString[1]);

		ProfileImageResponse profileImageResponse = userService.updateUserProfileImage(claims.getUserId(), profileImageRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(profileImageResponse);
	}

	@DeleteMapping("/myprofile/image")
	public ResponseEntity<ProfileImageResponse> deleteUserProfileImage(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String[] tokenString = authorization.split(" ");
		if (!tokenString[0].equals("Bearer")) {
			throw new InvalidTokenException("토큰 정보가 올바르지 않습니다.");
		}

		Jwt.Claims claims = jwt.verify(tokenString[1]);

		ProfileImageResponse profileImageResponse = userService.deleteUserProfileImage(claims.getUserId());

		return ResponseEntity.ok(profileImageResponse);
	}

}
